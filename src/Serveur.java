import java.net.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

public class Serveur {
	/**
	 * Verrou du Set Clients
	 */
	private static final Object ClientLock = new Object();

	public static Set<Client> Clients = new HashSet<Client>();

	/**
	 * Methode d'ajout des Diffuseurs a la collection
	 * 
	 * @param Diff
	 *            Diffuseur a ajouter
	 * @return Boolean disant si le diffuseur a ete ajoute ou non
	 */
	public static boolean ajoute(Client Diff) {
		synchronized (ClientLock) {
			boolean b = Serveur.Clients.add(Diff);
			if (!b) {
				System.out.println("Le client " + Diff.getId()
						+ " n'a pas pu etre ajouter");
				return false;
			} else {

				return true;
			}
		}
	}

	/**
	 * Methode de verification si le diffuseur est deja dans la liste
	 * 
	 * @param Diff
	 *            Client a verifier
	 * @return boolean true si deja dans la liste, false sinon
	 */
	public static boolean contains(Client Diff) {
		for (Client i : Clients) {
			if (i.getId().equals(Diff.getId())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;

		try {
			server = new ServerSocket(5555);
			System.out.println("Serveur connecte");
			System.out.println("IP: " + GeneralFonctions.getIp());
			System.out.println("port: 5555");
			try {
				while (true) {
					System.out.println("En attente de connexion");

					Socket connexion = server.accept();
					Thread t = new ServeurThread(connexion);
					t.start();

				}

			} catch (IOException e) {
				System.err.println("On n'arrive pas a accepter la socket");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("On en peut pas ecouter sur le port 5555");
			System.exit(1);
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				System.err.println("On ne peut pas ecouter sur le port 5555");
				System.exit(1);
			}
		}
	}

}
