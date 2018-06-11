package fr.jeux.pendu;

import java.io.BufferedReader;
import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Dictionnaire {

	static final boolean	DEBUG = true ;
	String	langue ;
	int		nbMots ;
	String[]	Mots ;
	
	
	public Dictionnaire(String fichier) {
	    boolean finLecture ;
	    BufferedReader reader = null;
	    String ligne;
	    nbMots = 0;
	    
	    try {
	        Gdx.app.log("INFO", "Debut lecture dico - "+fichier);
	         	
	    	reader = new BufferedReader(Gdx.files.internal(fichier).reader());

	        if (DEBUG) System.out.println("Lecture de la première ligne pour obtenir le nom du dico ...") ;
	        langue = reader.readLine();	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)

	        if (DEBUG) System.out.println("Lecture de la deuxième ligne pour obtenir le nombre de mots ...") ;
	        nbMots = Integer.parseInt(reader.readLine());	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)

	        if (DEBUG) System.out.println("Il y a "+nbMots+" mots. Création du tableau pour les stocker...") ;
	        
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
	            if (DEBUG) {
	                System.out.println("Fin de fichier !");
	            }
	        } else {
	        	Mots[i++] = ligne;
	        }
	    }
	}
	
	public String choisitMotAuHasard() {
		return Mots[(int) (Math.random() * nbMots)];
	}
	
	public String getLangue() { return langue ; } ;
	
	public static String[][] getListeDictionnaires(String cheminDictionnaires) {
		FileHandle[]	listeRepertoire ;
		String fichier ;
		String[][] listeDictionnaires ;
	    BufferedReader reader = null;

		
		listeRepertoire = Gdx.files.internal(cheminDictionnaires).list() ;
		if (DEBUG) 	Gdx.app.log("Liste dictionnaire", listeRepertoire.length + " dictionnaires trouvés");
		listeDictionnaires = new String[listeRepertoire.length][3] ;
		for (int i = 0 ; i < listeRepertoire.length ; i++) {
			fichier = listeRepertoire[i].toString() ;
			try {
				reader = new BufferedReader(Gdx.files.internal(fichier).reader());
				listeDictionnaires[i][0] = reader.readLine() ;	//Lit le nom du dictionnaire
				listeDictionnaires[i][1] = reader.readLine() ;	//Lit le nombre de mots
				listeDictionnaires[i][2] = fichier;				//Stocke le chemin d'accès
				if (DEBUG) 	Gdx.app.log("Liste dictionnaire", "Dictionnaire trouvé : "+listeDictionnaires[i][0]+" avec "+listeDictionnaires[i][1]+" mots");
				reader.close();
			} catch (Exception e) {
		        reader = null ;
		        Gdx.app.log("BUG", "Erreur dans la lecture du fichier de dictionnaire "+fichier);
			}
		}
		
		return listeDictionnaires ;
	}
}
