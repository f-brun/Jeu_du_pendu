package fr.jeux.pendu;

/**
 * Classe englobante des highscores. Permet de regrouper les highscores pour différents niveau et dictionnaires dans une seule structure
 * @author Florent Brun
 *
 */
public class Highscores {
	private Highscore[][] highscores ;	//Tableau des highscores des différents niveaux et dictionnaires
	
	public Highscores(Niveau[] niveaux, String[] dicos) {
		highscores = new Highscore[niveaux.length][dicos.length] ;
		for (int i = 0 ; i < niveaux.length ; i++) {
			for (int j = 0 ; j < dicos.length ; j++) {
				highscores[i][j] = new Highscore(niveaux[i],dicos[j]) ;
			}
		}
	}
	
	public Highscore getHighscoreActuel() {
		return highscores[Pendu.niveau.getNumero()][Pendu.dictionnaires.getDictionnaireActuel().getNumero()] ;
	}

	public Highscore getHighscore(int noNiveau, int noDico) {
		return highscores[noNiveau][noDico] ;
	}
}
