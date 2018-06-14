package fr.jeux.pendu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BarreMinuteur extends ProgressBar {

	private static final Color couleurRouge = Color.RED ;
	private static final Color couleurOrange = Color.ORANGE ;
	private static final Color couleurJaune = Color.YELLOW ;
	private static final Color couleurVert = Color.GREEN ;
	private long tempsDebut ;
	private long tempsFin ;
	private long tempsPause ;	//Heure de debut de la pause
	private long dureePause ;	//Duree de la pause
	
	private float duree ;
	private float	pourcentRouge, pourcentOrange, pourcentJaune ;
	private boolean running ;	//Indique que le chrono tourne
	private boolean enPause ;	//Indique que le chrono est sur pause
	
	public BarreMinuteur(float dureeMinuteur, float R, float O, float J, boolean vertical,Skin skin) {
		super(0,dureeMinuteur,0.1f,vertical,skin) ;
		super.setRound(false);
		tempsDebut = tempsFin = tempsPause = dureePause = 0 ;
		pourcentRouge = R ;
		pourcentOrange = O ;
		pourcentJaune = J ;
		duree = dureeMinuteur ;
		running = false ;
		this.setValue(dureeMinuteur) ;	//On part avec une jauge pleine
	}
	
	public void depart() {
		if (!running) {
			tempsDebut = System.currentTimeMillis() ;
			tempsFin = tempsPause = dureePause = 0 ;
			running = true ;
			this.setValue(duree) ;	//On part avec une jauge pleine
			this.miseAJour() ;
		}
	}
	
	public float stop() {
		tempsFin = System.currentTimeMillis() ;
		running = false ;
		this.miseAJour() ;
		float ecoule = (tempsFin - tempsDebut - dureePause)/1000 ;
		if (ecoule > duree) return 0 ;		//Si le temps est �coul� on retourne 0
		return (duree - ecoule)/duree ;		//Sinon on retourne la fraction de la dur�e du minuteur qui s'est �coul�
	}

	public void pause() {
		if (running && !enPause) {
			tempsPause = System.currentTimeMillis() ;
			running = false ;
			enPause = true ;
			this.miseAJour() ;
		}
	}

	public void resume() {
		if (!running && enPause) {
			dureePause += System.currentTimeMillis() - tempsPause ;
			enPause = false ;
			running = true ;
			this.miseAJour() ;
		}
	}


	public void reset (float duree,float pourcentRouge, float pourcentOrange, float pourcentJaune) {
		this.duree = duree ;
		this.pourcentRouge = pourcentRouge ;
		this.pourcentOrange = pourcentOrange ;
		this.pourcentJaune = pourcentJaune ;
    	this.setRange(0, duree);		//D�finit les valeurs min et max de la progressbar
		this.reset();
	}
	
	public void reset() {
		tempsDebut = tempsFin = tempsPause = 0 ;
		this.miseAJour() ;
	}

/** Mise � jour du minuteur. Renvoi le pourcentage du temps restant et met � jour l'affichage en changeant la couleur si n�cessaire
 * 
 * @return pourcentage du temps restant ou 0 si le temps est �coul�
 */
	public float miseAJour() {
		float ecoule ;
		float pourcentage ;
		ecoule = (running) ? ((System.currentTimeMillis() - tempsDebut)/1000) : ((tempsFin - tempsDebut)/1000) ;
		pourcentage = (duree - ecoule)/duree ;	//Pourcentage du temps restant
		pourcentage = (pourcentage < 0) ? 0 : pourcentage ;	//On met � z�ro si le pourcentage est n�gatif
		if (pourcentage <= pourcentRouge) {
			this.setColor(couleurRouge);
		}
		else if (pourcentage <= pourcentOrange) {
			this.setColor(couleurOrange);
		}
		else if (pourcentage <= pourcentJaune) {
			this.setColor(couleurJaune);
		}
		else this.setColor(couleurVert);
		this.setValue(pourcentage*duree) ;	//On actualise la valeur de la progressbar
		return pourcentage ;	//pourcentage du temps restant ou 0 si le temps est �coul�
	}
}
