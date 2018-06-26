package fr.jeux.pendu;

import com.badlogic.gdx.Gdx;

public class Score {
	
	public static final String[] NOM_ITEMS_SCORE = {"Niveau", "Nom", "Score", "Mots", "Temps", "Dictionnaire" } ;
	public static final int NO_NIVEAU = 1 ;
	public static final int NOM_JOUEUR = 2 ;
	public static final int SCORE = 3 ;
	public static final int NB_MOTS_DEVINES = 4 ;
	public static final int TEMPS = 5 ;
	public static final int NOM_DICTIONNAIRE = 6 ;
	
	public int	niveau ;
	public String	joueur ;
	public int	score ;
	public int	nbMotsDevines ;
	public long	temps ;
	public String	dictionnaire ;
	
	public Score(int niveau, String dictionnaire) {
		this.joueur = " " ;	//Chaine vide mais contenant au moins un caractère sinon le parsing du score plante
		this.niveau = niveau ;
		this.score = 0 ;
		this.nbMotsDevines = 0 ;
		this.temps = 0 ;
		this.dictionnaire = dictionnaire ;
	}
	
	public Score (int niveau, String joueur, int score, int nbMots, long temps, String dictionnaire) {
		this.joueur = joueur ;
		this.niveau = niveau ;
		this.score = score ;
		this.nbMotsDevines = nbMots ;
		this.temps = temps ;
		this.dictionnaire = dictionnaire ;
	}
	
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
			case NOM_DICTIONNAIRE :
				return this.dictionnaire ;
			default :
				Gdx.app.log("ERROR", "Numéro d'item de score non existant : "+noItem);
				return "" ;
		}
	}
}
