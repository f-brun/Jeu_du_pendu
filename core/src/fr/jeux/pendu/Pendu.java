package fr.jeux.pendu;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fr.jeux.pendu.screens.* ;

public class Pendu extends Game {

    public static boolean DEBUG = false ;
    public static final String CHEMIN_SKIN = "skin/freezing-ui.json" ;
    public static final String POLICE_MOTS = "Consolas.fnt" ;
    public static final float DUREE_AFFICHAGE_GAGNE = 1.5f  ;  //D�lai avant d'afficher l'�cran de victoire (pour qu'on ai le temps de voir le mot compl�t�)
    public static final int LARGEUR_MIN_BOUTONS_LETTRES = 30 ; //Largeur minimale des boutons repr�sentant les lettres
    public static final int LARGEUR_MAX_BOUTONS_LETTRES = 160 ; //Largeur maximale des boutons repr�sentant les lettres
    public static final int HAUTEUR_MIN_BOUTONS_LETTRES = 15 ; //Hauteur minimale des boutons repr�sentant les lettres
    public static final int HAUTEUR_MAX_BOUTONS_LETTRES = 60 ; //Hauteur maximale des boutons repr�sentant les lettres
    public static int largeurEcran ;
    public static int hauteurEcran ;
    public static BitmapFont policeMots ;
    public static Label.LabelStyle styleMots ;

    //R�f�rences aux diff�rents �crans
    public static EcranJeu ecranJeu ;
    public static EcranAccueil ecranAccueil ;
    public static EcranReglages ecranReglages ;
    public static EcranGagne ecranGagne ;
    public static EcranPerdu ecranPerdu ;
    
    public static Table tPartie ;  //Table contenant la partie
    public static Table tFin ;  //Table contenant l'�cran de fin (gagn� ou perdu)
    public static Skin skin ; //Skin utilis�e par l'UI
    public static final int nbLettresParLigne = 5 ;	//Nombre de bouton lettre par ligne
    public static Image affichagePendu;  //El�ment d'UI contenant l'image du pendu
    public static String motADeviner ;
    public static String motDevine ;
    public static Label lMotDevine; //texte du mot � deviner
    public static String lettresProposes ;
    public static int niveau ;		//Niveau de difficult� s�lectionn�
    public static int nbErreurs ;	//Nombre de mauvaises lettres propos�es
    public static int nbErreursMax ;	//Nombre de mauvaises lettres propos�es
    public static final int NB_IMAGES = 12 ;
    public static final String IMAGE_GAGNE = "images/gagne.gif" ;   //Image affich�e en cas de victoire
    public static final String IMAGE_PERDU = "images/perdu.jpg" ;   //Image affich�e en cas de d�faite
    public static final float ATTENTE_FIN_PARTIE = 8f ;    //Nombre de secondes o� on affiche l'image de fin de partie avant de revenir au menu
    public static Texture[] imagePendu = new Texture[NB_IMAGES] ;	//Stocke les images successives de pendu
    public static final String CHEMIN_FICHIERS = "images/" ;  //Chemin vers les fichiers de donn�es
    public static final String PREFIXE_FICHIERS_IMAGES = "pendu" ;	//Pr�fixe des fichiers repr�sentants le pendu (suivis de xx o� xx est le num�ro du fichier)
    public static final String FICHIER_DICTIONNAIRE = "Dictionnaires/Fran�ais.txt" ; //Fichier contenant les mots � deviner
    public static final String[][]	listeDictionnaires =	{ { "Francais", "Dictionnaire francais", "Dictionnaires/Francais.txt"},
    														  { "English", "English dictionary", "Dictionnaires/English.txt"} };
    
    public static Dictionnaire	dictionnaire ;	//Dictionnaire en cours
    public static int score ;   //score = nb de mots devinn�s d'affil�	
	

	
    public void create() {
        score = 0 ;
        niveau = 1 ;
        largeurEcran = Gdx.graphics.getWidth();
        hauteurEcran = Gdx.graphics.getHeight();

        //Charge et d�finit les polices et skin d'affichage
        skin = new Skin(Gdx.files.internal(CHEMIN_SKIN)) ;
        policeMots = new BitmapFont(Gdx.files.internal(POLICE_MOTS));
        styleMots = new Label.LabelStyle();
        styleMots.font = policeMots ;

        //Au d�part aucun �cran n'est cr�e
        ecranJeu = null ;
        ecranPerdu = null ;
//        ecranGagne = null ;
        
        //Chargement des �l�ments en m�moire
        ChargeImagesPendu();		//Les images de la pendaison progressive
        dictionnaire = new Dictionnaire(FICHIER_DICTIONNAIRE);		//Initialisation du dictionnaire
        
        this.setScreen(new EcranAccueil(this));	//Bascule sur l'�cran d'accueil
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
    public EcranReglages getEcranReglages() { return ecranReglages ; }
    public EcranJeu getEcranJeu() { return ecranJeu ; }
    public static EcranGagne getEcranGagne() { return ecranGagne ; }
    public EcranPerdu getEcranPerdu() { return ecranPerdu ; }
    public int getScore() { return score; }
    public int getNiveau() { return niveau ; }
    
    public void setLargeurEcran(int l) { largeurEcran = l ; }
    public void setHauteurEcran(int h) { hauteurEcran = h ; }
    public void setEcranAccueil(EcranAccueil e) { ecranAccueil = e ; }
    public void setEcranReglages(EcranReglages e) { ecranReglages = e ; }
    public void setEcranJeu(EcranJeu e) { ecranJeu = e ; }
    public static void setEcranGagne(EcranGagne e) { ecranGagne = e ; }
    public void setEcranPerdu(EcranPerdu e) { ecranPerdu = e ; }
    public void setScore(int s) { score = s ; }
    public void setNiveau(int n) { niveau = n ; }
    
	public void dispose () {
	}
}
