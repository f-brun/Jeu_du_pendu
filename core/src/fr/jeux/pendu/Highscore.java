package fr.jeux.pendu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;

public class Highscore {
	private static final String BASE_FICHIER_HIGHSCORE = "Highscores/Highscore_" ;
	public static final int NB_HIGHSCORES_PAR_CATEGORIE = 15 ;
	public static final String DELIMITEUR_ENREGISTREMENTS = ";" ;	
	public static final String CARACTERE_REMPLACEMENT = "_" ;		//Caractere de remplacement si le nom du joueur contient le délimiteur
	public static final int LONGUEUR_MAX_NOM_JOUEUR = 15 ;		//Nombre max de caracteres dans le nom du joueur
	
	private BufferedWriter	writer ;
	private BufferedReader	reader ;
	private Niveau	niveau ;
	private String dictionnaire ;
	private int	nbScores ;
	private Score[] meilleursScores ;
	
	private String nomFichierHighscores ;
	
	public Highscore(Niveau niveau, String dictionnaire) {
		this.niveau = niveau ;
		this.dictionnaire = dictionnaire ;
		this.nomFichierHighscores = BASE_FICHIER_HIGHSCORE+niveau.getDenomination()+"_"+dictionnaire ;
		meilleursScores = new Score[NB_HIGHSCORES_PAR_CATEGORIE] ;
		nbScores = litFichierHighscore(nomFichierHighscores) ;
		for (int i = nbScores ; i < NB_HIGHSCORES_PAR_CATEGORIE ; i++ )	meilleursScores[i] = new Score(niveau.getNumero(), dictionnaire) ;	//On met à 0 les scores suivants jusqu'à remplir le tableau
	}
	
	private int litFichierHighscore(String fichier) {
		StringTokenizer chaineDecoupee ;
		
		try {
			reader = new BufferedReader(Gdx.files.local(fichier).reader()) ;	//Ouvre le fichier en lecture
		}
		catch (Exception e) { reader = null ;}
		
		if (reader == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en lecture le fichier de highscore : "+fichier);
			return 0 ;
		}

		int nbScoresLus = 0 ;
		String ligne = null ;
		do {
			try {
				ligne = reader.readLine() ;
			} catch (IOException e) { e.printStackTrace(); }

			if (ligne != null) {
				chaineDecoupee = new StringTokenizer(ligne,DELIMITEUR_ENREGISTREMENTS) ;
				if (chaineDecoupee.hasMoreTokens()) {
					meilleursScores[nbScoresLus] = new Score(
						Integer.parseInt(chaineDecoupee.nextToken()),											//Numéro du niveau
						(chaineDecoupee.hasMoreTokens()) ? chaineDecoupee.nextToken() : "Inconnu",				//Nom du joueur
						(chaineDecoupee.hasMoreTokens()) ? Integer.parseInt(chaineDecoupee.nextToken()) : 0,	//Score
						(chaineDecoupee.hasMoreTokens()) ? Integer.parseInt(chaineDecoupee.nextToken()) : 0,	//Nb de mots devinnes
						(chaineDecoupee.hasMoreTokens()) ? Long.parseLong(chaineDecoupee.nextToken()) : 999999,	//Temps de jeu
						(chaineDecoupee.hasMoreTokens()) ? chaineDecoupee.nextToken() : "Inconnu") ;			//Dictionnaire
					nbScoresLus++ ;
				}
			}
		} while ( (ligne != null) && (nbScoresLus < NB_HIGHSCORES_PAR_CATEGORIE) ) ;
		
		try { reader.close();} catch (IOException e) {e.printStackTrace();	}
		return nbScoresLus ;
	}
	
	public void ecritScore(Score score) {

		try {
			writer = new BufferedWriter(Gdx.files.local(nomFichierHighscores).writer(true)) ;	//Ouvre le fichier en écriture en mode append
		}
		catch  (Exception e) { e.printStackTrace(); writer = null ;}
			
		if (writer == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en ecriture le fichier de highscore : "+nomFichierHighscores);
			return ;
		}

		//Fabrique la ligne a insérer dans le fichier de highscore
		String ligne = "" ;
		for (int i = 0 ; i < Score.NOM_ITEMS_SCORE.length-1 ; i++) {
			ligne += score.getStringItemScore(i) + DELIMITEUR_ENREGISTREMENTS ;	//On ajoute un à un les constituants du score
		}
		ligne += score.getStringItemScore(Score.NOM_ITEMS_SCORE.length-1) ;	//On rajoute le dernier item sans délimiteur qui suive
		
		try {
			writer.write(ligne);
			writer.newLine();
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }

	}

	public void ecritScores() {
		Score score ;
		String ligne ;
		
		try {
			writer = new BufferedWriter(Gdx.files.local(nomFichierHighscores).writer(false)) ;	//Ouvre le fichier en écriture en mode overwrite
		}
		catch  (Exception e) { e.printStackTrace(); writer = null ;}
			
		if (writer == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en ecriture le fichier de highscore : "+nomFichierHighscores);
			return ;
		}

		for (int i = 0 ; i < meilleursScores.length ; i++) {
			score = meilleursScores[i] ;
			score.joueur = score.joueur.replaceAll(DELIMITEUR_ENREGISTREMENTS, CARACTERE_REMPLACEMENT) ;	//Remplace les occurences du délimiteur dans le nom du joueur

			//Fabrique la ligne a insérer dans le fichier de highscore
			ligne = "" ;
			for (int j = 0 ; j < Score.NOM_ITEMS_SCORE.length-1 ; j++) {
				ligne += score.getStringItemScore(j) + DELIMITEUR_ENREGISTREMENTS ;	//On ajoute un à un les constituants du score
			}
			ligne += score.getStringItemScore(Score.NOM_ITEMS_SCORE.length-1) ;	//On rajoute le dernier item sans délimiteur qui suive
			
			try {
				writer.write(ligne);
				writer.newLine();
			} catch (IOException e) { e.printStackTrace(); }
		}

		try {
			writer.close();
		} catch (IOException e) { e.printStackTrace();	}
	}	
	
	
	/**
	 * Fonction qui évalue un score et s'il est dans les highscores, l'insère dans la table des scores
	 * @param score	Score à évaluer
	 * @return position du score dans la table (0 = 1ere position) ou -1 si pas dans les highscores
	 */
	public int insereScore(Score score) {
		int i ;
		for (i = meilleursScores.length - 1 ; i > 0 ; i--) {	//On part de la fin
			if ( (score.score > meilleursScores[i].score)
			  || (score.score == meilleursScores[i].score && score.nbMotsDevines > meilleursScores[i].nbMotsDevines)
			  || (score.score == meilleursScores[i].score && score.nbMotsDevines == meilleursScores[i].nbMotsDevines && score.temps <= meilleursScores[i].temps) ) {	//Si le nouveau score est meilleur
				meilleursScores[i] = meilleursScores[i-1] ;		//On recopie pour decaler les highscores et faire de la place pour ce nouveau record
			}
			else if (i > 0) {	//Si ce score est moins bon
				if (i < (meilleursScores.length-1)) 	meilleursScores[i+1] = score ;	//Si on est quand même dans le tableau, on s'inscrit à la bonne place
				break ;		//Et on sort de la boucle
			}
		}
		if (i == 0) {	//Si i = 0, il reste a comparer avec le premier 
			if ( (score.score > meilleursScores[i].score)
			  || (score.score == meilleursScores[i].score && score.nbMotsDevines > meilleursScores[i].nbMotsDevines)
			  || (score.score == meilleursScores[i].score && score.nbMotsDevines == meilleursScores[i].nbMotsDevines && score.temps <= meilleursScores[i].temps) ) {	//Si ce score est meilleur
				meilleursScores[i] = score ;	//On prend la place du premier (l'ancien premier a déjà été inscrit en 2° position)
				ecritScores() ;
				return 0 ;
			}
			else meilleursScores[1] = score ;	//Sinon on est en deuxième position
		}
		if (i < meilleursScores.length-1) {
			return i+1 ;	//Si on est dans les records, on renvoit la place correspondante 
		}
		else return -1 ;	//Sinon on renvoit -1 pour indiquer qu'on est pas dans les highscores
	}

	public Score[] getMeilleursScores() { return meilleursScores ; }

	public void setHighscore(int numero, Score score) {
		meilleursScores[numero] = score ;
	}
	
	public Score getHighscore(int numero) { return meilleursScores[numero] ; }
	
	public Niveau getNiveau() { return this.niveau ; }
}

