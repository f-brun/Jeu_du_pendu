package fr.jeux.pendu;

public class Score {
	public String	joueur ;
	public int	niveau ;
	public int	score ;
	public long	temps ;
	public String	dictionnaire ;
	
	public Score(int niveau, String dictionnaire) {
		this.joueur = " " ;	//Chaine vide mais contenant au moins un caractère sinon le parsing du score plante
		this.niveau = niveau ;
		this.score = 0 ;
		this.temps = 0 ;
		this.dictionnaire = dictionnaire ;
	}
	
	public Score (int niveau, String joueur, int score, long temps, String dictionnaire) {
		this.joueur = joueur ;
		this.niveau = niveau ;
		this.score = score ;
		this.temps = temps ;
		this.dictionnaire = dictionnaire ;
	}
}
