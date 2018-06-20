package fr.jeux.pendu;

import java.io.BufferedReader;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Dictionnaire {

	static final boolean	DEBUG = false ;
	String	langue ;
	int		nbMots ;
	String[]	Mots ;
	
	
	public Dictionnaire(String fichier) {
	    boolean finLecture ;
	    BufferedReader reader = null;
	    String ligne;
	    nbMots = 0;
	    
	    try {
	    	if (DEBUG) Gdx.app.log("INFO", "Debut lecture dico - "+fichier);
	         	
	    	reader = new BufferedReader(Gdx.files.internal(fichier).reader());

	        if (DEBUG) Gdx.app.log("INFO", "Lecture de la premi�re ligne pour obtenir le nom du dico ...") ;
	        langue = reader.readLine();	//R�cup�re le nombre de mots � lire (la premi�re ligne du fichier contient le nombre de mots)

	        if (DEBUG) Gdx.app.log("INFO","Lecture de la deuxi�me ligne pour obtenir le nombre de mots ...") ;
	        nbMots = Integer.parseInt(reader.readLine());	//R�cup�re le nombre de mots � lire (la premi�re ligne du fichier contient le nombre de mots)

	        if (DEBUG) Gdx.app.log("INFO","Il y a "+nbMots+" mots. Cr�ation du tableau pour les stocker...") ;
	        
	    } catch (Exception e) {
	        reader = null ;
	    }
	
	    Mots = new String[nbMots] ;		//Allocation du tableau pour stocker les mots
	    
	    finLecture = false ;
	    int i = 0 ;
	    while (!finLecture && (i < nbMots)) {
	        ligne = null;
	        try {
	            ligne = reader.readLine();
	            //                 ligne = Normalizer.normalize(ligne, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", ""); //Retire les accents
	        } catch (Exception e) {
	            finLecture = true ;
	        }
	        if (ligne == null) {
	            finLecture = true ;
	            if (DEBUG) Gdx.app.log("INFO","Fin de fichier !");
	        } else {
	        	Mots[i++] = ligne;
	        }
	    }
	}
	
	public String choisitMotAuHasard() {
		return Mots[(int) (Math.random() * nbMots)];
	}
	
	public String getLangue() { return langue ; } ;
	public int getNbMots() { return nbMots ; } ;
	
	public static String[][] getListeDictionnaires(String cheminDictionnaires) {
		FileHandle[]	listeHandlesRepertoire ;
		String fichier ;
		String[][] listeDictionnaires ;
	    BufferedReader reader = null;

		
		listeHandlesRepertoire = Gdx.files.internal(cheminDictionnaires).list() ;
		String[] listeRepertoire = new String[listeHandlesRepertoire.length] ;
		for (int i = 0 ; i < listeHandlesRepertoire.length ; i++) listeRepertoire[i] = listeHandlesRepertoire[i].toString() ;	//Recopie les chemins dans le tableau de String
		if (DEBUG) 	Gdx.app.log("INFO", listeRepertoire.length + " dictionnaires trouv�s");
		Arrays.sort(listeRepertoire);							//Tri par ordre alphabetique pour les avoir dans le bon ordre

		listeDictionnaires = new String[listeRepertoire.length][3] ;
		for (int i = 0 ; i < listeRepertoire.length ; i++) {
			fichier = listeRepertoire[i] ;
			try {
				reader = new BufferedReader(Gdx.files.internal(fichier).reader());
				listeDictionnaires[i][0] = reader.readLine() ;	//Lit le nom du dictionnaire
				listeDictionnaires[i][1] = reader.readLine() ;	//Lit le nombre de mots
				listeDictionnaires[i][2] = fichier;				//Stocke le chemin d'acc�s
				if (DEBUG) 	Gdx.app.log("INFO", "Dictionnaire trouv� : "+listeDictionnaires[i][0]+" avec "+listeDictionnaires[i][1]+" mots");
				reader.close();
			} catch (Exception e) {
		        reader = null ;
		        Gdx.app.log("ERROR", "Erreur dans la lecture du fichier de dictionnaire "+fichier);
			}
		}
		
		return listeDictionnaires ;
	}
}
