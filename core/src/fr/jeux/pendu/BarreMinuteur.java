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
	private long tempsPause ;
	
	private float duree ;
	private float	pourcentRouge, pourcentOrange, pourcentJaune ;
	private boolean running ;
	
	public BarreMinuteur(float dureeMinuteur, float R, float O, float J, boolean vertical,Skin skin) {
		super(0,dureeMinuteur,0.1f,vertical,skin) ;
		super.setRound(false);
		tempsDebut = tempsFin = tempsPause = 0 ;
		pourcentRouge = R ;
		pourcentOrange = O ;
		pourcentJaune = J ;
		duree = dureeMinuteur ;
		running = false ;
		this.setValue(dureeMinuteur) ;	//On part avec une jauge pleine
	}
	
	public void depart() {
		tempsDebut = System.currentTimeMillis() ;
		tempsFin = tempsPause = 0 ;
		running = true ;
		this.setValue(duree) ;	//On part avec une jauge pleine
		this.miseAJour() ;
	}
	
	public float stop() {
		tempsFin = System.currentTimeMillis() ;
		running = false ;
		this.miseAJour() ;
		float ecoule = (tempsFin - tempsDebut)/1000 ;
		if (ecoule > duree) return 0 ;		//Si le temps est écoulé on retourne 0
		return (duree - ecoule)/duree ;		//Sinon on retourne la fraction de la durée du minuteur qui s'est écoulé
	}
	
	public void reset (float duree,float pourcentRouge, float pourcentOrange, float pourcentJaune) {
		this.duree = duree ;
		this.pourcentRouge = pourcentRouge ;
		this.pourcentOrange = pourcentOrange ;
		this.pourcentJaune = pourcentJaune ;
    	this.setRange(0, duree);		//Définit les valeurs min et max de la progressbar
		this.reset();
	}
	
	public void reset() {
		tempsDebut = tempsFin = tempsPause = 0 ;
	}

/** Mise à jour du minuteur. Renvoi le pourcentage du temps restant et met à jour l'affichage en changeant la couleur si nécessaire
 * 
 * @return pourcentage du temps restant ou 0 si le temps est écoulé
 */
	public float miseAJour() {
		float ecoule ;
		float pourcentage ;
		ecoule = (running) ? ((System.currentTimeMillis() - tempsDebut)/1000) : ((tempsFin - tempsDebut)/1000) ;
		pourcentage = (duree - ecoule)/duree ;	//Pourcentage du temps restant
		pourcentage = (pourcentage < 0) ? 0 : pourcentage ;	//On met à zéro si le pourcentage est négatif
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
		return pourcentage ;	//pourcentage du temps restant ou 0 si le temps est écoulé
	}
	
}
