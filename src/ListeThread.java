import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ListeThread extends Thread {

	public Socket sock;
	public Client c;

	public ListeThread(Socket sock, Client c) {
		this.sock = sock;
		this.c = c;
	}

	/**
	 * Methode d'envoie de la liste des clients connecte au serveur
	 */

	public String list() {
		String ListeC = "LI";
		synchronized (Serveur.Clients) {
			for (Client i : Serveur.Clients) {
				ListeC += i.getId();
				ListeC += "#";
			}

		}
		return ListeC;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
			boolean estCo = true;

			while (estCo) {

				String Liste = list();
				out.println(Liste);
				boolean envoie = false;
				int i = 0;

				while (envoie == false && i < 2) {
					envoie = out.checkError();
					Thread.sleep(1000);
					i++;
				}

				if (envoie == true) {
					estCo = false;
					synchronized (Serveur.Clients) {
						for (Client j : Serveur.Clients) {
							if (j.getId().equals(c.getId())) {
							
								Serveur.Clients.remove(j);
								break;
							}
						}
					}

					System.out.println("Le client " + c.getId()
							+ " est deconnecter");
					sock.close();
				}

				sleep(20000);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}

	}

}
