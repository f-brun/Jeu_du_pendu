package fr.jeux.pendu;

public class Chrono {

	private long tempsDebut ;
	private long tempsFin ;
	private long debutPause ;	//Heure de debut de la pause
	private long dureePause ;	//Duree de la pause
	private boolean running ;	//Indique que le chrono tourne
	private boolean enPause ;	//Indique que le chrono est sur pause
	
	public Chrono() {
		tempsDebut = tempsFin = debutPause = dureePause = 0 ;
		running = false ;
	}
	
	public void depart() {
		if (!running) {
			tempsDebut = System.currentTimeMillis() ;
			tempsFin = debutPause = dureePause = 0 ;
			running = true ;
			enPause = false ;
		}
	}
	
	public long stop() {
		tempsFin = System.currentTimeMillis() ;
		running = false ;
		if (enPause) {
			dureePause += tempsFin - debutPause ;	//Si on etait en pause, il faut calculer le temps de pause
			enPause = false ;						//et sortir de pause
		}
		return (tempsFin - tempsDebut - dureePause) ;	//renvoi la duree totale du chrono (hors pauses)
	}

	public void pause() {
		if (running && !enPause) {
			debutPause = System.currentTimeMillis() ;
			running = false ;
			enPause = true ;
		}
	}

	public void reprise() {
		if (!running && enPause) {
			dureePause += System.currentTimeMillis() - debutPause ;
			enPause = false ;
			running = true ;
		}
	}


	public void reset() {
		tempsDebut = tempsFin = debutPause = 0 ;
		running = enPause = false ;
	}
	
	public long soustraitDuree(long dureeASoustraire) {
		dureePause += dureeASoustraire ;	//Pour soustraire, on fait comme si on avait fait une pause de cette dur�e
		if (running) {
			return (System.currentTimeMillis() - tempsDebut - dureePause) ;
		}
		else return (tempsFin - tempsDebut - dureePause) ;	//renvoi la duree totale du chrono (hors pauses)
	}

	/**
	 * R�cup�re la dur�e �coul�e depuis le d�part du chono en tenant compte des pauses
	 * @return Temps �coul� en ms
	 */
	public long getDuree() {
		if (!running && !enPause) return (tempsFin - tempsDebut - dureePause) ;	//renvoi la dur�e totale du chrono (hors pauses)
		if (enPause) return (-tempsDebut + debutPause - dureePause) ;	//Si on est en pause le calcul est diff�rent
		return System.currentTimeMillis() - tempsDebut - dureePause ;	//Si on est en cours de chronom�trage, on calcule en fonction de l'instant actuel
	}
	
	/**
	 * Renvoi une chaine qui indique le temps �coul� sous la forme H:MM:SS (H peut �tre sup�rieur � 24) ou MM:SS ou M:SS ou SS ou S 
	 * @return temps ecoule au format H:MM:SS
	 */
	public String getDureeHMS() {
		int duree = (int) (getDuree()/1000) ;	//Recup�re la dur�e en secondes
		return toHMS(duree) ;
	}
	
	
	/**
	 * Converti une dur�e en seconde en une chaine sous la forme H:MM:SS (H peut �tre sup�rieur � 24) ou MM:SS ou M:SS ou SS ou S
	 * @param duree temps � convertir en secondes
	 * @return chaine au format H:MM:SS
	 */
	public static String toHMS(int duree) {
		String dureeHMS = "" ;
		int heures = duree/3600 ;
		int minutes = (duree%3600)/60 ;
		int secondes = duree%60 ;
		if (heures != 0 ) {
			dureeHMS = Integer.toString(heures) + ":" ;
			if (minutes < 10) dureeHMS += "0" ;
			dureeHMS +=  Integer.toString(minutes) + ":" ;
			if (secondes < 10) dureeHMS += "0" ;
			dureeHMS +=  Integer.toString(secondes) ;
		}
		else if (minutes != 0) {
			dureeHMS +=  Integer.toString(minutes) + ":" ;
			if (secondes < 10) dureeHMS += "0" ;
			dureeHMS +=  Integer.toString(secondes) ;
		}
		else {
			dureeHMS +=  Integer.toString(secondes) ;
		}
		return dureeHMS ;
	}
}