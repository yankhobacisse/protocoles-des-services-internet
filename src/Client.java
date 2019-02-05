import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{
    
	private String Id;
	private Socket soc;
	private int port;
	
	public Client(String Id, Socket soc, int port){
		this.Id=Id;
		this.soc=soc;
		this.port=port;
	}
	
	public String getId(){
		return Id;
	}
	
	public Socket getSocket(){
		return soc;
	}
	
	public int getPort(){
		return port;
	}
	
	public boolean same(Client cli){
		if(cli.Id==this.Id){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public boolean contains(Client cli){
		for(Client i: Serveur.Clients){
			if(i.getId().equals(cli.getId())){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
    	
    	
    	
		System.out.println("Entrez un identifiant:");
		Scanner sc = new Scanner(System.in);
		String Id=sc.nextLine();
		System.out.println("\nIdentifant: "+Id+"\n");
		System.out.println();
		System.out.println("Service TCP sortant: OK\n");
		System.out.println("Entrez l'adresse IP du serveur\n");
		String IP=sc.nextLine();

    	
        

        System.out.println ("Attente de connexion " +
                IP + " sur le port 5555.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(IP, 5555);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
            
            int portUDP=GeneralFonctions.randInt(2000, 65000);
            System.out.println("Vous jouerez sur le port "+portUDP);
            String message="CO"+portUDP+":"+Id;
            out.println(message);
            out.flush();
            String reponse=in.readLine();
            if(reponse.equals("OK")){
            	System.out.println("Vous avez ete enregistre aupres du serveur");
            }
            else if(reponse.equals("KO")){
            	System.out.println("Votre identifiant est deja pris veuillez en choisir un autre");
            	echoSocket.close();
            	return ;
            }    
            
        

        
        
        
        
         BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
		
         BufferedReader stdIn2 = new BufferedReader(
                 new InputStreamReader(System.in));
        
        ClientThread li=new ClientThread(echoSocket, in, stdIn, portUDP);
        Thread t=new Thread(li);
        t.start();

        ClientOut envoie=new ClientOut(echoSocket, stdIn2, out);
        Thread t1=new Thread(envoie);
        t1.start();
        System.out.println("Entrez un identifiant d'un joueur avec qui vous voulez jouez");

        
        
        
        } catch (UnknownHostException e) {
            System.err.println("L'IP " + IP+" n'existe pas");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Probleme d'envoie de l'enregistrement ");
            System.exit(1);
        }

          
       }

	}


