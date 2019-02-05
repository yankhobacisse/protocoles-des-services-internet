import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServeurThread extends Thread {

	protected Socket clientSocket;
	public static Client[] aenvoyer;

	public ServeurThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		aenvoyer = new Client[2];
	}

	public static void setClient(Client demande, Client recoit) {
		aenvoyer[0] = demande;
		aenvoyer[1] = recoit;
	}

	/**
	 * Methode de tri des messages
	 * 
	 * @param s
	 *            message a trier
	 * @return un entier representant le type de message
	 */
	public int triMessage(String s) {
		String test = s.substring(0, 2);

		if (test.equals("PR")) {
			return 1;
		} else if (test.equals("CO")) {
			return 2;

		} else if (test.equals("RA")) {
			return 3;
		} else {
			return 0;
		}
	}

	// Fonction qui gere le premier message envoyer par le client
	// renvoie true si c'est bon false sinon
	public boolean premier_envoie(String inputLine, BufferedReader in,
			PrintWriter out) {

		if (triMessage(inputLine) != 2) {
			out.println("KO");
			out.flush();
			return false;
		} else {
			int id=inputLine.indexOf(':',0);
			Client c = new Client(inputLine.substring(id+1, inputLine.length()),
					clientSocket, Integer.parseInt(inputLine.substring(2, id)));

			if (Serveur.contains(c)) {
				out.println("KO");
				out.flush();
				return false;
			} else {

				Serveur.ajoute(c);

				out.println("OK");
				out.flush();
				return true;
			}

		}
	}

	public void run() {
		System.out.println("Communication entrante avec un client");

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String inputLine = in.readLine();

			boolean co = premier_envoie(inputLine, in, out);

			if (co == false) {
				System.out.println("Un client a tenté de se connecter mais n'a pas reussi");
			} else {
				System.out.println("Un client s'est connecter\n");
				int id;
				id=inputLine.indexOf(':', 0);
				Client c = new Client(
						inputLine.substring(id+1, inputLine.length()),
						clientSocket, Integer.parseInt(inputLine
								.substring(2, id)));

				ListeThread l = new ListeThread(clientSocket, c); // Thread qui
																	// envoie la
																	// liste des
																	// clients
				Thread t = new Thread(l);
				t.start();

				while (true) {

					while ((inputLine = in.readLine()) == null) {

					}

					// try{
					int recu = triMessage(inputLine);

					if (recu == 1) { // si on recu PR
						int taille=0;
						for (Client i : Serveur.Clients) {
							taille++;
							if (inputLine.substring(2, inputLine.length()).equals(i.getId())) {
								taille=0;
								System.out.println("Mise en relation de "+ i.getId() + " avec " + c.getId());

								String aenvoie = "PR"
										+ c.getSocket().getInetAddress().getHostAddress()+ ":" + c.getPort()+":" + c.getId();

								try {
									PrintWriter out2 = new PrintWriter(i.getSocket().getOutputStream(),
											true);
									System.out.println("Envoie de la demande a "
													+ i.getId()); // on envoie
																	// la
																	// demande
																	// au
																	// deuxieme
																	// client
									out2.println(aenvoie);
									boolean b = out2.checkError();
									if (b == false) {
										System.out.println("Demande envoye");
									}
									Client d = new Client(i.getId(),
											i.getSocket(), i.getPort());
									setClient(c, d);
									System.out.println("en attente de reponse");

								} catch (IOException e) {
								}

							}

						}
						if(taille==Serveur.Clients.size()){
							String r="NO";
							out.println(r);
							taille=0;
						}

					}
					if (recu == 3) {

						PrintWriter out2 = new PrintWriter(aenvoyer[0]
								.getSocket().getOutputStream(), true);
						PrintWriter out3 = new PrintWriter(aenvoyer[1]
								.getSocket().getOutputStream(), true);

						String r;
						if (inputLine.substring(2, 3).equals("1")) {
							r = "RA"+ aenvoyer[1].getSocket().getInetAddress().getHostAddress()+ ":"+ aenvoyer[1].getPort();

							out2.println(r);
							out2.flush();
							out3.println("DEX");
							out3.flush();
							out2.println("DEO");
							out2.flush();

						} else {
							r = "NO";

							out2.println(r);
							out2.flush();
						}

					}

				}
			}

		} catch (IOException e) {

			System.err.println("Un client a ete deconnecte brutalement");

		}
	}

}
