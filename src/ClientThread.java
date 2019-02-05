import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Thread qui s'occupe de ce que recoit recoit le serveur
 * LI pour la liste et PR pour une demande de jeu.
 */

public class ClientThread extends Thread {

	public Socket sock;
	public BufferedReader in;
	public BufferedReader stdIn;
	public int port;

	public ClientThread(Socket sock, BufferedReader in, BufferedReader stdIn,
			int port) {
		this.sock = sock;
		this.in = in;
		this.stdIn = stdIn;
		this.port = port;
	}

	public void run() {
		try {
			String temp_recu = "";
			boolean ecout = true;
			while (ecout) {
				String recu;

				while ((recu = in.readLine()) == null) {

				}
				if (recu.substring(0, 2).equals("LI")) {
					String inLine = recu.substring(2, recu.length());
					System.out.println("La liste des clients disponible est: ");
					int position = 0;
					int index = 0;

					index = inLine.indexOf('#', position);
					while (index != -1) {
						System.out.println(inLine.substring(position, index));
						position = index + 1;
						index = inLine.indexOf('#', position);
					}

				} else if (recu.substring(0, 2).equals("PR")) {

					PrintWriter out = new PrintWriter(sock.getOutputStream(),
							true);
					
			System.out.println("recu: "+recu);
					int pos=0;
					int ind=0;
					ind=recu.indexOf(':',pos);
			System.out.println("ind: "+ind);
					pos=ind+1;
					ind=recu.indexOf(':', pos);
			System.out.println("ind2 "+ind);
					String id_recu = recu.substring(ind+1, recu.length());
			System.out.println("id_recu: "+id_recu);
					System.out.println(id_recu
							+ " souhaite jouer avec vous (il est trop nul)");
					System.out.println("Voulez vous accepter? (y/n)");
					temp_recu = recu;
					ClientOut c = new ClientOut(sock, stdIn, out);
					Thread t = new Thread(c);
					t.start();
					
				} else if (recu.substring(0, 2).equals("RA")) {
					System.out
							.println("Le joueur veut bien jouer avec vous!! Vous avez un nouvel ami virtuel");
					System.out.println("Fermeture du serveur");
					String de = in.readLine();
					String pion = de.substring(2, 3);
					int ind=recu.indexOf(':', 0);
					String IP_envoie = recu.substring(2, ind);
					int port_envoie = Integer.parseInt(recu.substring(ind+1, recu.length()));
					int port_ecoute = port;

					ClientUDP cgame = new ClientUDP(IP_envoie, port_ecoute,
							port_envoie, pion);
					Thread tgame = new Thread(cgame);
					tgame.start();
					ecout = false;
				} else if (recu.substring(0, 2).equals("NO")) {
					System.out
							.println("Le joueur ne veut pas jouer avec vous (il est surement moche)");

					PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
					
					ClientOut envoie=new ClientOut(sock, stdIn, out);
			        Thread t1=new Thread(envoie);
			        t1.start();
			       
				} else if (recu.substring(0, 2).equals("DE")) {
					System.out.println("Fermeture du serveur ");
					String pion = recu.substring(2, 3);
					int id=temp_recu.indexOf(':', 0);
					String IP_envoie = temp_recu.substring(2, id);
					int i=temp_recu.indexOf(':', id+1);
					int port_envoie = Integer.parseInt(temp_recu.substring(id+1,
							i));
					int port_ecoute = port;
					ClientUDP cgame = new ClientUDP(IP_envoie, port_ecoute,
							port_envoie, pion);
					Thread tgame = new Thread(cgame);
					tgame.start();
					ecout = false;
				}
			}
			sock.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
}
