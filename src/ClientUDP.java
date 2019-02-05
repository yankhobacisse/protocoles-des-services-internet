import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientUDP extends Thread {
	String IP_envoie;
	int port_ecoute;
	String pion;
	int port_envoie;
	String plateau[][];

	public ClientUDP(String IP_envoie, int port_ecoute, int port_envoie,
			String pion) {
		this.IP_envoie = IP_envoie;
		this.port_ecoute = port_ecoute;
		this.pion = pion;
		this.port_envoie = port_envoie;
		this.plateau = new String[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				plateau[i][j] = " ";
			}
		}
	}

	public void affiche_plateau() {
		System.out.println("    A   B   C ");
		System.out.println("---------------");
		for (int i = 0; i < 3; i++) {
			System.out.print((i + 1) + " | ");
			for (int j = 0; j < 3; j++) {
				System.out.print(plateau[i][j] + " | ");
			}
			System.out.println("");
			System.out.println("  -------------");
		}

	}

	/* renvoie 0 si la partie n'est pas finis et qu'iln'y a pas de gagnant */
	/* renvoie 1 si 'X' a gagne 2 si 'O' a gagne et 3 si il y a match nul */

	public int estGagnant() {
		//oublie de trois
		for (int i = 0; i < 3; i++) {
			if (plateau[i][0].equals(plateau[i][1])
					&& plateau[i][0].equals(plateau[i][2])) {
				if (plateau[i][0].equals("X")) {
					return 1;
				} else if (plateau[i][0].equals("O")) {
					return 2;
				}
			} else if (plateau[0][i].equals(plateau[1][i])
					&& plateau[0][i].equals(plateau[2][i])) {
				if (plateau[0][i].equals("X")) {
					return 1;
				} else if (plateau[0][i].equals("O")) {
					return 2;
				}

			}
		}

		if (plateau[0][0].equals(plateau[1][1])
				&& plateau[0][0].equals(plateau[2][2])) {
			if (plateau[0][0].equals("X")) {
				return 1;
			} else if (plateau[0][0].equals("O")) {
				return 2;
			}
		}
		if (plateau[2][0].equals(plateau[1][1])
				&& plateau[2][0].equals(plateau[0][2])) {
			if (plateau[2][0].equals("X")) {
				return 1;
			} else if (plateau[2][0].equals("O")) {
				return 2;
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (plateau[i][j].equals(" ")) {
					return 0;
				}
			}
		}

		return 3;
	}

	public static int convert(String a) {
		if (a.equals("A")) {
			return 0;
		} else if (a.equals("B")) {
			return 1;
		} else if (a.equals("C")) {
			return 2;
		}
		else{
			return -1;
		}
	}

	public boolean place_pion(String position, String pion) {
		int i, j;
		if(position.length()!=2){
			return false;
		}
		try{
		i = Integer.parseInt(position.substring(0, 1)) - 1;
		j = convert(position.substring(1, 2));
		if(((i<0) || (i>2)) || ((j<0) || (j>2)) ){
			return false;
		}
		if (plateau[i][j].equals(" ")) {
			plateau[i][j] = pion;
			return true;
		}
		return false;
	}catch(NumberFormatException e){
		return false;
	}
	}

	public void run() {
		
		int gagnant=0;
		String pion_adverse;
		boolean bon_deplacement = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("LETS THE GAME BEGIN");
		try {
			DatagramSocket socket_ecoute = new DatagramSocket(port_ecoute);
			DatagramSocket socket_envoie = new DatagramSocket(null);
			socket_envoie
					.connect(InetAddress.getByName(IP_envoie), port_envoie);

			byte[] data_recu = new byte[2];
			byte[] data_envoie;

			if (pion.equals("O")) {
				pion_adverse = "X";
			} else {
				System.out.println("C'est a votre adversaire de commencer");
				pion_adverse = "O";
				
				DatagramPacket paquet = new DatagramPacket(data_recu,
						data_recu.length);
				socket_ecoute.receive(paquet);
				String st = new String(paquet.getData()/*, 0, paquet.getLength()*/);
				System.out.println("recu: "+st);
				boolean te=this.place_pion(st, pion_adverse);
				System.out.println("bool: "+te);
			}

			while ((gagnant = this.estGagnant()) == 0) {
				this.affiche_plateau();
				System.out.println("C'est a vous de jouer");
				String placement = "";
				do {
					System.out.println("Entrez la case ou voulez jouer: (Exemple 1A)");
					placement = sc.nextLine();
					
					bon_deplacement = place_pion(placement, this.pion);
					this.affiche_plateau();
					if (bon_deplacement == false) {
						System.out.println("Deplacement impossible!! :O");
					}
				}while (bon_deplacement == false);
				bon_deplacement = false;
				placement = placement + "\n";
				data_envoie = placement.getBytes();
				DatagramPacket paquet_envoie = new DatagramPacket(data_envoie,
						data_envoie.length);
				socket_envoie.send(paquet_envoie);
				gagnant = this.estGagnant();
				
				if (gagnant == 0) {
					System.out.println("C'est a votre adversaire de jouer!!");
					
					DatagramPacket paquet = new DatagramPacket(data_recu,
							data_recu.length);
					socket_ecoute.receive(paquet);
					String st = new String(paquet.getData(), 0,
							paquet.getLength());
					System.out.println("recu: "+st);
					this.place_pion(st, pion_adverse);
				}
				
			}

			if (gagnant == 3) {
				System.out.println("Match nul!!! COMME VOUS DEUX");

			} else if (gagnant == 2) {
				if (this.pion.equals("O")) {
					System.out.println("TROP FORT T'AS GAGNER!!! \\O/");
				} else {
					System.out.println("... BOUUUUUUU t'as perdu");
				}
			} else if (gagnant == 1) {
				if (this.pion.equals("X")) {
					System.out.println("TROP FORT T'AS GAGNER!!! \\O/");
				} else {
					System.out.println("... BOUUUUUUU t'as perdu");
				}
			}

			socket_envoie.close();
			socket_ecoute.close();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}