package fr.jeux.pendu;

import java.io.BufferedReader;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Dictionnaires {

	//Définition des places des différents paramètres dans le tableau de la liste des dictionnaires
	public static final int NOM_DICO = 0 ;
	public static final int NB_MOTS_DICO = 1 ;
	public static final int CHEMIN_FICHIER_DICO = 2 ;
	
	public String[][] listeDictionnaires ;
	public Dictionnaire dictionnaireActuel ;
	
	
	public Dictionnaires(String cheminDictionnaires) {
		listeDictionnaires = getListeDictionnaires(cheminDictionnaires) ;
	}

	/**
	 * Ouvre le dictionnaire dont le numéro est fournit en entrée et en fait le dictionnaire courant
	 * le nom du fichier est déduit de la liste de dictionnaires obtenue à partir du répertoire des dictionnaires
	 * @param numeroDico numéro du dictionnaire dans le tableau listeDictionnaires[][]
	 * @return instance de Dictionnaire permettant l'accès au dictionnaire
	 */
	public Dictionnaire setDictionnaire(int numeroDico) {
		if (listeDictionnaires == null) {
			Gdx.app.log("ERROR", "Demande à accéder à un dictionnaire par son numéro alors que la liste des dictionnaires n'a pas été établie");
			return null ;
		}
		dictionnaireActuel = new Dictionnaire(numeroDico, listeDictionnaires[numeroDico][CHEMIN_FICHIER_DICO]) ;
		return dictionnaireActuel ;
	}
	
	public Dictionnaire getDictionnaireActuel() {
		return dictionnaireActuel ;
	}
	
	public String[][] getListeDictionnaires() {
		return listeDictionnaires ;
	}
	
	/**
	 * Renvoit les noms des différents dictionnaires trouvés
	 * @return tableau de String des noms des dictionnaires disponibles
	 */
	public String[] getNomsDictionnaires() {
		if (listeDictionnaires == null) {
			Gdx.app.log("ERROR", "Demande à accéder à la liste des noms des dictionnaires alors qu'elle n'a pas été établie");
			return null ;
		}
		String[] noms = new String[listeDictionnaires.length];
		for (int i = 0 ; i < listeDictionnaires.length ; i++) noms[i] = listeDictionnaires[i][NOM_DICO] ;
		return noms ;
	}
	
	public static String[][] getListeDictionnaires(String cheminDictionnaires) {
		FileHandle[]	listeHandlesRepertoire ;
		String fichier ;
		String[][] listeDictionnaires ;
	    BufferedReader reader = null;

		
		listeHandlesRepertoire = Gdx.files.internal(cheminDictionnaires).list() ;
		String[] listeRepertoire = new String[listeHandlesRepertoire.length] ;
		for (int i = 0 ; i < listeHandlesRepertoire.length ; i++) listeRepertoire[i] = listeHandlesRepertoire[i].toString() ;	//Recopie les chemins dans le tableau de String
		if (Pendu.DEBUG) 	Gdx.app.log("INFO", listeRepertoire.length + " dictionnaires trouvés");
		Arrays.sort(listeRepertoire);							//Tri par ordre alphabetique pour les avoir dans le bon ordre

		listeDictionnaires = new String[listeRepertoire.length][3] ;
		for (int i = 0 ; i < listeRepertoire.length ; i++) {
			fichier = listeRepertoire[i] ;
			try {
				reader = new BufferedReader(Gdx.files.internal(fichier).reader());
				listeDictionnaires[i][NOM_DICO] = reader.readLine() ;	//Lit le nom du dictionnaire
				listeDictionnaires[i][NB_MOTS_DICO] = reader.readLine() ;	//Lit le nombre de mots
				listeDictionnaires[i][CHEMIN_FICHIER_DICO] = fichier;				//Stocke le chemin d'accès
				if (Pendu.DEBUG) 	Gdx.app.log("INFO", "Dictionnaire trouvé : "+listeDictionnaires[i][0]+" avec "+listeDictionnaires[i][1]+" mots");
				reader.close();
			} catch (Exception e) {
		        reader = null ;
		        Gdx.app.log("ERROR", "Erreur dans la lecture du fichier de dictionnaire "+fichier);
			}
		}
		return listeDictionnaires ;
	}
}
