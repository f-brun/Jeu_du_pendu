package fr.jeux.pendu;

import java.io.BufferedReader;

import com.badlogic.gdx.Gdx;

public class Dictionnaire {

	static final boolean	DEBUG = false ;
	int numero ;
	String	langue ;
	int		nbMots ;
	String[]	Mots ;
	
	
	public Dictionnaire(int numero, String fichier) {
	    boolean finLecture ;
	    BufferedReader reader = null;
	    String ligne;
	    nbMots = 0;
	    this.numero = numero ;
	    
	    try {
	    	if (DEBUG) Gdx.app.log("INFO", "Debut lecture dico - "+fichier);
	         	
	    	reader = new BufferedReader(Gdx.files.internal(fichier).reader());

	        if (DEBUG) Gdx.app.log("INFO", "Lecture de la première ligne pour obtenir le nom du dico ...") ;
	        langue = reader.readLine();	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)

	        if (DEBUG) Gdx.app.log("INFO","Lecture de la deuxième ligne pour obtenir le nombre de mots ...") ;
	        nbMots = Integer.parseInt(reader.readLine());	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)

	        if (DEBUG) Gdx.app.log("INFO","Il y a "+nbMots+" mots. Création du tableau pour les stocker...") ;
	        
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
	
	public int getNumero() { return numero; } ;
	public String getLangue() { return langue ; } ;
	public int getNbMots() { return nbMots ; }
}
