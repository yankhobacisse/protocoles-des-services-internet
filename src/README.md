# Description serveur :

Les fichiers associés aux serveur sont :
   - Serveur.java
   - ServeurThread.java
   - ListeThread.java 

## Serveur.java :

Le serveur démarre et attend des connexions sur le port 5555. Si une connexion se fait, il lance un ServeurThread. C’est dans Serveur.java que se trouve la liste des clients qui sont connectés.

## ServeurThread.java :

Pour chaque client qui se connecte, on lance un ServeurThread. Le thread gère d’abord la première connexion (vérification de l’identifiant, ajout à a liste si accepte la connexion, démarrage de l’envoie de la liste).
Il attend ensuite des demandes du clients. Ce thread sert d’intermédiaire entre le client qui lui est associé et toute autre forme de communication (envoie de liste, demande entrante ou sortante d’autre client).
 
## ListeThread.java :

Lorsqu’un client s’est connecté, ce thread envoie régulièrement (toutes les 20 secondes) la liste des clients connectés. Si un client s’est déconnecter, l’envoie de la liste ne peut pas se faire et le client déconnecté est retiré de la liste.

# Description Client :

Chaque client est identifié à l’aide d’un identifiant. Un port lui est associé. 
Les fichiers associés aux clients sont les fichiers :
   - Client.java
   - ClientThread.java
   - ClientOut.java
   - ClientUDP.java

## Client.java :

C’est là ou le client va rentrer son identifiant ainsi que l’adresse IP du serveur auquel il veut se connecter. C’est aussi à ce moment que le programme tire un port aléatoire entre 2000 et 65000. Ce port servira lors du jeu pour communiquer en UDP. Ensuite il lance ClientThread et ClientOut

## ClientOut.java :

Ce thread gère ce que le client envoie : les requêtes de jeu au serveur ainsi que la réponse en cas de demande de jeu.

## ClientThread.java : 

Ce thread gère ce que le client reçoit : les demandes de jeu, la liste des joueurs et les réponses aux demande de jeu. 

## ClientUDP.java :

Lorsqu’une partie commence entre deux joueurs, les connexions avec le serveur sont coupés et les clients se connectent entre eux en UDP. La partie se déroule normalement jusqu’à la fin (gagnant ou égalité) puis la connexion se ferme et les clients se coupent. Chaque client gère ses propres coups avant de l’envoyer à son adversaire. 
