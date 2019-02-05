import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;


/**
 * Class de methodes general pour les entites
 */
public class GeneralFonctions {


	/**
	 * Methode de recuperation de l'IP de la machine
	 * @return String de l'IP de la machine
	 */
	public static String getIp(){
		String ipAddress = null;
		Enumeration<NetworkInterface> net = null;
		try {
			net = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		while(net.hasMoreElements()){
			NetworkInterface element = net.nextElement();
			Enumeration<InetAddress> addresses = element.getInetAddresses();
			while (addresses.hasMoreElements()){
				InetAddress ip = addresses.nextElement();
				if (ip instanceof Inet4Address){
					if (ip.isSiteLocalAddress()){
						ipAddress = ip.getHostAddress();
					}
				}
			}
		}
		String[] Parts = ipAddress.split("\\.");
		ipAddress = remplissage(Integer.parseInt(Parts[0]), 3)+"."+remplissage(Integer.parseInt(Parts[1]), 3)+"."+remplissage(Integer.parseInt(Parts[2]), 3)+"."+remplissage(Integer.parseInt(Parts[3]), 3);
		return ipAddress;
	}

	/**
	 * Methode de creation de string representant une IP de multidiffusion
	 * @return String de l'IP de multidiffusion
	 */
	public static String getRandomIPMultidif(){
		String ipAddress = "";
		int b1 = randInt(224, 239);
		int b2 = randInt(0, 255);
		int b3 = randInt(0, 255);
		int b4 = randInt(0, 255);
		ipAddress = remplissage(b1, 3)+"."+remplissage(b2, 3)+"."+remplissage(b3, 3)+"."+remplissage(b4, 3);
		return ipAddress;
	}

	/**
	 * Methode de creation d'entier aleatoire 
	 * @param min entier minimum
	 * @param max entier maximum
	 * @return entier aleatoire entre min et max
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/**
	 * methode de remplissage d'entier
	 * @param i entier a remplir
	 * @param t taille de la chaine representant l'entier
	 * @return chaine remplie
	 */
	public static String remplissage(int i, int t){
		String res = Integer.toString(i);
		if (res.length() <= t){
			while (res.length() < t){
				res = "0".concat(res);
			}
		}
		return res;
	}

	/**
	 * 
	 * @param IP adresse IP a remplir pour quelle soit de la forme xxx.xxx.xxx.xxx
	 * @return
	 */
/*	public static String rempliIP(String IP){
		int index=0;
		int position=1;
		String res="";
		int temp;
		String sub;
		
		index=IP.indexOf('.', position);
		for(int i=0;i<3; i++){
			sub=IP.substring(position, index);
			temp=Integer.parseInt(sub);
			if( temp>=100 && temp<=999 ){
				res+=sub+".";
				
			}
			else if(temp<=99 && temp>=10){
				res+="0"+sub+".";
			}
			else{
				res+="00"+sub+".";
			}
			position=index+1;
			index=IP.indexOf('.', position);
			
		}

		sub=IP.substring(position, IP.length());
		temp=Integer.parseInt(sub);
		if( temp>=100 && temp<=999 ){
			res+=sub;
			
		}
		else if(temp<=99 && temp>=10){
			res+="0"+sub;
		}
		else{
			res+="00"+sub;
		}
		
		return res;
	}
*/
	
	
	/**
	 * Methode de remplissage des chaines de caracteres
	 * @param s chaine a remplir
	 * @param t taille de la chaine finale
	 * @return chaine remplie
	 */
	/*public static String remplissage(String s, int t){
		String res;
		if (s.length() <= t){
			res = s;
			while (res.length() < t){
				res = res.concat("#");
			}
			return res;
		}
		else{
			res = s.substring(0, t);
		}
		return res;
	}


*/
/**
 * Methode qui retire les diese a la fin d'une chaine de caractere.
 * @param s chaine a vide
 * @return chaine sans les dieses  
 */
/* 
	public static String vider(String s){
			
			int i=8;
			while(s.substring(i-1,i).equals("#")){
				i--;
			}
			return s.substring(0,i);
	}



*/
}