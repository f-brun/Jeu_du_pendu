package fr.jeux.pendu;

public class Chrono {

	private long tempsDebut ;
	private long tempsFin ;
	private long tempsPause ;	//Heure de debut de la pause
	private long dureePause ;	//Duree de la pause
	private boolean running ;	//Indique que le chrono tourne
	private boolean enPause ;	//Indique que le chrono est sur pause
	
	public Chrono() {
		tempsDebut = tempsFin = tempsPause = dureePause = 0 ;
		running = false ;
	}
	
	public void depart() {
		if (!running) {
			tempsDebut = System.currentTimeMillis() ;
			tempsFin = tempsPause = dureePause = 0 ;
			running = true ;
		}
	}
	
	public long stop() {
		tempsFin = System.currentTimeMillis() ;
		running = false ;
		if (enPause) {
			dureePause += tempsFin - tempsPause ;	//Si on etait en pause, il faut calculer le temps de pause
			enPause = false ;						//et sortir de pause
		}
		return (tempsFin - tempsDebut - dureePause)/1000 ;	//renvoi la duree totale du chrono (hors pauses)
	}

	public void pause() {
		if (running && !enPause) {
			tempsPause = System.currentTimeMillis() ;
			running = false ;
			enPause = true ;
		}
	}

	public void reprise() {
		if (!running && enPause) {
			dureePause += System.currentTimeMillis() - tempsPause ;
			enPause = false ;
			running = true ;
		}
	}


	public void reset() {
		tempsDebut = tempsFin = tempsPause = 0 ;
	}
}