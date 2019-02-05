import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Thread qui gere ce que le client recoit.
 * Cas du RA
 * Cas du PR
 */



public class ClientOut extends Thread {

	public Socket sock;
	public BufferedReader stdIn;
	public PrintWriter out;
	public String s;

	public ClientOut(Socket sock, BufferedReader stdIn, PrintWriter out) {
		this.sock = sock;
		this.stdIn = stdIn;
		this.out = out;
		this.s = "";
	}

	public void run() {
		boolean continu=true;
		String reponse;
		while (continu) {
			try {
				String rep = "RA";
				reponse = stdIn.readLine();
				if (reponse.equals("y")) {
					rep += "1";
					out.println(rep);
					out.flush();
					System.out.println("La partie va bientot commence!!");
					continu=false;
					
				} else if (reponse.equals("n")) {
					rep += "0";
					out.println(rep);
					out.flush();
				} else {

					String aenvoi = "PR"
							+ reponse;
					out.println(aenvoi);
					out.flush();
					continu=false;
				}
			} catch (IOException e) {
			}
		}

	
	

	}
}
