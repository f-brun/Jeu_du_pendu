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
//	    FileHandle handle ;
	    String ligne;
	    nbMots = 0;
	    if (fichier.lastIndexOf(".") != 0) langue = fichier.substring(fichier.lastIndexOf("/")+1,fichier.lastIndexOf(".")) ;	//La langue est le nom de fichier sans extension
	    else langue = "Inconnu" ;
	    
	    try {
	        Gdx.app.log("INFO", "Debut lecture dico - "+langue);
	         	
	    	reader = new BufferedReader(Gdx.files.internal(fichier).reader());
	        
	        if (DEBUG) {
	            System.out.println("Lecture de la première ligne pour obtenir le nombre de mots ...");
	        }
	        nbMots = Integer.parseInt(reader.readLine());	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)
	        if (DEBUG) {
	            System.out.println("Il y a "+nbMots+" mots. Création du tableau pour les stocker...");
	        }
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
}
