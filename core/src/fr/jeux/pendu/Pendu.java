package fr.jeux.pendu;


import java.io.BufferedReader;
import java.io.FileNotFoundException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fr.jeux.pendu.screens.* ;

public class Pendu extends Game {

    public static boolean DEBUG = true ;
    public static final int TAILLE_BUFFER = 256*1024 ;   //Taille du buffer de lecture du dictionnaire
    public static final String CHEMIN_SKIN = "skin/freezing-ui.json" ;
    public static final String POLICE_MOTS = "Consolas.fnt" ;
    public static final float DUREE_AFFICHAGE_GAGNE = 1.5f  ;  //Délai avant d'afficher l'écran de victoire (pour qu'on ai le temps de voir le mot complété)
    public static final int LARGEUR_MIN_BOUTONS_LETTRES = 30 ; //Largeur minimale des boutons représentant les lettres
    public static final int LARGEUR_MAX_BOUTONS_LETTRES = 160 ; //Largeur maximale des boutons représentant les lettres
    public static final int HAUTEUR_MIN_BOUTONS_LETTRES = 15 ; //Hauteur minimale des boutons représentant les lettres
    public static final int HAUTEUR_MAX_BOUTONS_LETTRES = 60 ; //Hauteur maximale des boutons représentant les lettres
    public static int largeurEcran ;
    public static int hauteurEcran ;
    public static BitmapFont policeMots ;
    public static Label.LabelStyle styleMots ;

    //Références aux différents écrans
    public static EcranJeu ecranJeu ;
    public static EcranAccueil ecranAccueil ;
    public static EcranGagne ecranGagne ;
    public static EcranPerdu ecranPerdu ;
    
    public static Table tPartie ;  //Table contenant la partie
    public static Table tFin ;  //Table contenant l'écran de fin (gagné ou perdu)
    public static Skin skin ; //Skin utilisée par l'UI
    public static final int nbLettresParLigne = 5 ;	//Nombre de bouton lettre par ligne
    public static Image affichagePendu;  //Elément d'UI contenant l'image du pendu
    public static String motADeviner ;
    public static String motDevine ;
    public static Label lMotDevine; //texte du mot à deviner
    public static String lettresProposes ;
    public static int niveau ;		//Niveau de difficulté sélectionné
    public static int nbErreurs ;	//Nombre de mauvaises lettres proposées
    public static int nbErreursMax ;	//Nombre de mauvaises lettres proposées
    public static final int NB_IMAGES = 12 ;
    public static final String IMAGE_GAGNE = "images/gagne.gif" ;   //Image affichée en cas de victoire
    public static final String IMAGE_PERDU = "images/perdu.jpg" ;   //Image affichée en cas de défaite
    public static final float ATTENTE_FIN_PARTIE = 8f ;    //Nombre de secondes où on affiche l'image de fin de partie avant de revenir au menu
    public static Texture[] imagePendu = new Texture[NB_IMAGES] ;	//Stocke les images successives de pendu
    public static final String CHEMIN_FICHIERS = "images/" ;  //Chemin vers les fichiers de données
    public static final String PREFIXE_FICHIERS_IMAGES = "pendu" ;	//Préfixe des fichiers représentants le pendu (suivis de xx où xx est le numéro du fichier)
    public static final String FICHIER_DICTIONNAIRE = "liste_filtree.txt" ; //Fichier contenant les mots à deviner
    public static String[] listeMots ; // Liste des mots à deviner
    public static int nombreMotsDico ; //Nombre de mots dans le dictionnaire des mots à deviner
    public static int score ;   //score = nb de mots devinnés d'affilé	
	

	
    public void create() {
        score = 0 ;
        niveau = 1 ;
        largeurEcran = Gdx.graphics.getWidth();
        hauteurEcran = Gdx.graphics.getHeight();

        //Charge et définit les polices et skin d'affichage
        skin = new Skin(Gdx.files.internal(CHEMIN_SKIN)) ;
        policeMots = new BitmapFont(Gdx.files.internal(POLICE_MOTS));
        styleMots = new Label.LabelStyle();
        styleMots.font = policeMots ;

        //Au départ aucun écran n'est crée
        ecranJeu = null ;
        ecranPerdu = null ;
//        ecranGagne = null ;
        
        //Chargement des éléments en mémoire
        ChargeImagesPendu();		//Les images de la pendaison progressive
        try {
        	RempliListeMot(FICHIER_DICTIONNAIRE);		//Et les mots de la liste
        } catch (FileNotFoundException e) {
        	System.out.println("catch #1 :" + e.toString());
        }
        
        this.setScreen(new EcranAccueil(this));	//Bascule sur l'écran d'accueil
    }

    void ChargeImagesPendu() {
    	int i;	//Compteur d'image indice de tableau
    	for (i = 0; i < NB_IMAGES; i++) {
	        if (i < 10) {
	            imagePendu[i] = new Texture(Gdx.files.internal(CHEMIN_FICHIERS + PREFIXE_FICHIERS_IMAGES + "0" + i + ".png"));
	        }
	        if (i > 9) {
	            imagePendu[i] = new Texture(Gdx.files.internal(CHEMIN_FICHIERS + PREFIXE_FICHIERS_IMAGES + i + ".png"));
	        }
    	}
    }

	void RempliListeMot(String fichier) throws FileNotFoundException {
	    boolean finLecture ;
	    String ligne;
	    nombreMotsDico = 0;
	    BufferedReader reader = null;
	    FileHandle handle ;
	    handle = Gdx.files.internal(fichier) ;
	    if (handle!=null) {
	        ligne = handle.readString();
	    }
	    try {
	        Gdx.app.log("INFO", "Debut lecture dico");
	         	
	    	reader = new BufferedReader(Gdx.files.internal(fichier).reader(),TAILLE_BUFFER);
	        
	        if (DEBUG) {
	            System.out.println("Lecture de la première ligne pour obtenir le nombre de mots ...");
	        }
	        nombreMotsDico = Integer.parseInt(reader.readLine());	//Récupère le nombre de mots à lire (la première ligne du fichier contient le nombre de mots)
	        if (DEBUG) {
	            System.out.println("Il y a "+nombreMotsDico+" mots. Création du tableau pour les stocker...");
	        }
	    } catch (Exception e) {
	        reader = null ;
	    }
	
	    listeMots = new String[nombreMotsDico] ;		//Allocation du tableau pour stocker les mots
	    finLecture = false ;
	    int i = 0 ;
	    while (!finLecture && (i < nombreMotsDico)) {
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
	        	listeMots[i++] = ligne;
	        }
	    }
	}
   
    
    public void render () {
		super.render();
	}

    public int getLargeurEcran() { 	return largeurEcran ; }
    public int getHauteurEcran() { 	return hauteurEcran ; }
    public boolean getDebugState() {  return DEBUG ; }
    public Skin getSkin() { return skin ; }
    public Texture[] getImagesPendu() { return imagePendu ; }
    public int getNbLettresParLigne() {return nbLettresParLigne ; }
    public EcranAccueil getEcranAccueil() { return ecranAccueil ; }
    public EcranJeu getEcranJeu() { return ecranJeu ; }
    public static EcranGagne getEcranGagne() { return ecranGagne ; }
    public EcranPerdu getEcranPerdu() { return ecranPerdu ; }
    public int getScore() { return score; }
    public int getNiveau() { return niveau ; }
    
    public void setLargeurEcran(int l) { largeurEcran = l ; }
    public void setHauteurEcran(int h) { hauteurEcran = h ; }
    public void setEcranAccueil(EcranAccueil e) { ecranAccueil = e ; }
    public void setEcranJeu(EcranJeu e) { ecranJeu = e ; }
    public static void setEcranGagne(EcranGagne e) { ecranGagne = e ; }
    public void setEcranPerdu(EcranPerdu e) { ecranPerdu = e ; }
    public void setScore(int s) { score = s ; }
    public void setNiveau(int n) { niveau = n ; }
    
	public void dispose () {
	}
}
