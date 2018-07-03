package fr.jeux.pendu;

import com.badlogic.gdx.Gdx;

/**
 * Classe de gestion du score. Chaque instance stocke l'ensemble des informations li�es � un score (nom du joueur, dur�e de la partie, niveau, ...)
 * @author Florent Brun
 *
 */
public class Score {
	//Constantes servant � d�crire le contenu de la classe (les informations stock�es)
	public static final String[] NOM_ITEMS_SCORE = {"Niveau", "Nom", "Score", "Mots", "Temps", "Dictionnaire" } ;
	public static final int NO_NIVEAU = 1 ;
	public static final int NOM_JOUEUR = 2 ;
	public static final int SCORE = 3 ;
	public static final int NB_MOTS_DEVINES = 4 ;
	public static final int TEMPS = 5 ;
	public static final int NOM_DICTIONNAIRE = 6 ;
	public static final int TEMPS_HMS = 7 ;
	
	//Informations stock�es par la classe relative au score
	public int	niveau ;
	public String	joueur ;
	public int	score ;
	public int	nbMotsDevines ;
	public int	temps ;
	public String	dictionnaire ;
	
	/**
	 * Constructeur d'initialisation avec juste le niveau et le dictionnaire (sert � initialiser les highscores)
	 * @param niveau niveau auquel est r�alis� le score
	 * @param dictionnaire Liste de mot utilis�e pour faire ce score
	 */
	public Score(int niveau, String dictionnaire) {
		this.joueur = " " ;	//Chaine vide mais contenant au moins un caract�re sinon le parsing du score plante
		this.niveau = niveau ;
		this.score = 0 ;
		this.nbMotsDevines = 0 ;
		this.temps = 0 ;
		this.dictionnaire = dictionnaire ;
	}
	
	public Score (int niveau, String joueur, int score, int nbMots, int temps, String dictionnaire) {
		this.joueur = joueur ;
		this.niveau = niveau ;
		this.score = score ;
		this.nbMotsDevines = nbMots ;
		this.temps = temps ;
		this.dictionnaire = dictionnaire ;
	}
	
	/**
	 * Renvoie une chaine contenant l'information demand�e dans le score.
	 * @param noItem Index de l'information demand�e dans le score (utiliser les constantes d�finies par la classe pour s�lectionner le bon objet)
	 * @return Chaine contenant l'information demand�e
	 */
	public String getStringItemScore(int noItem) {
		switch (noItem) {
			case NOM_JOUEUR :
				return this.joueur ;
			case NO_NIVEAU :
				return Integer.toString(this.niveau) ;
			case SCORE :
				return Integer.toString(this.score) ;
			case NB_MOTS_DEVINES :
				return Integer.toString(this.nbMotsDevines) ;
			case TEMPS :
				return Long.toString(this.temps) ;
			case TEMPS_HMS :
				return Chrono.toHMS(this.temps) ; 
			case NOM_DICTIONNAIRE :
				return this.dictionnaire ;
			default :
				Gdx.app.log("ERROR", "Num�ro d'item de score non existant : "+noItem);
				return "" ;
		}
	}
	
	/**
	 * Renvoie une copie du score qui poss�de donc une r�f�rence et un espace m�moire propre
	 * @param original
	 * @return copie du score
	 */
	public Score copie() {
		return new Score(this.niveau, this.joueur, this.score, this.nbMotsDevines, this.temps, this.dictionnaire) ;
	}
}
