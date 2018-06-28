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

    public static boolean DEBUG = true ;
    public static final String CHEMIN_SKIN = "skin/freezing-ui.json" ;
    public static final String POLICE_MOTS = "Consolas.fnt" ;
    public static final float DUREE_AFFICHAGE_GAGNE = 1.5f  ;  //Délai avant d'afficher l'écran de victoire (pour qu'on ai le temps de voir le mot complété)
    public static final int LARGEUR_MIN_BOUTONS_LETTRES = 30 ; //Largeur minimale des boutons représentant les lettres
    public static final int LARGEUR_MAX_BOUTONS_LETTRES = 160 ; //Largeur maximale des boutons représentant les lettres
    public static final int HAUTEUR_MIN_BOUTONS = 15 ; //Hauteur minimale des boutons
    public static final int HAUTEUR_MAX_BOUTONS = 60 ; //Hauteur maximale des boutons

    public static int largeurEcran ;
    public static int hauteurEcran ;
    public static BitmapFont policeMots ;
    public static Label.LabelStyle styleMots ;

    //Références aux différents écrans
    public static EcranJeu ecranJeu ;
    public static EcranAccueil ecranAccueil ;
    public static EcranReglages ecranReglages ;
    public static EcranGagne ecranGagne ;
    public static EcranPerdu ecranPerdu ;
    public static EcranChoixDictionnaire ecranChoixDictionnaire ;
    public static EcranHighscores ecranHighscores ;
    
    public static Table tPartie ;  //Table contenant la partie
    public static Table tFin ;  //Table contenant l'écran de fin (gagné ou perdu)
    public static Skin skin ; //Skin utilisée par l'UI
    public static final int nbLettresParLigne = 5 ;	//Nombre de bouton lettre par ligne
    public static Image affichagePendu;  //Elément d'UI contenant l'image du pendu
    public static String motADeviner ;
    public static String motDevine ;
    public static Label lMotDevine; //texte du mot à deviner
    public static Label lNbEssaisRestants ;	//Texte indiquant le nombre d'essais restant au joueur avant de perdre
    public static Label	lNbMotsDevines ;	//Texte indiquant le nombre de mots devinés d'affilés jusqu'ici
    public static BarreMinuteur	barreMinuteur ;	//Barre de progression pour le minuteur
    public static String lettresProposes ;
    public static Niveau niveau ;		//Niveau de difficulté sélectionné
    public static int nbErreurs ;	//Nombre de mauvaises lettres proposées
    public static int nbMotsDevines ;	//Nombre de mots devinés à la suite dans la partie en cours
    public static Chrono chrono ;	//Chronometre de la partie
    public static final int NB_IMAGES = 12 ;
    public static final String IMAGE_GAGNE = "images/gagne.gif" ;   //Image affichée en cas de victoire
    public static final String IMAGE_PERDU = "images/perdu.jpg" ;   //Image affichée en cas de défaite
    public static final float ATTENTE_FIN_PARTIE = 8f ;    //Nombre de secondes où on affiche l'image de fin de partie avant de revenir au menu
    public static Texture[] imagePendu = new Texture[NB_IMAGES] ;	//Stocke les images successives de pendu
    public static final String CHEMIN_FICHIERS = "images/" ;  //Chemin vers les fichiers de données
    public static final String PREFIXE_FICHIERS_IMAGES = "pendu" ;	//Préfixe des fichiers représentants le pendu (suivis de xx où xx est le numéro du fichier)
    public static final String CHEMIN_FICHIERS_DICTIONNAIRES = "Dictionnaires/" ;	//Chemin vers les fichiers dictionnaires
    public static Dictionnaires	dictionnaires ;			//Dictionnaires du jeu
    public static Score score ;   //score
    public static Logger logger ;	//Objet permettant de logger les parties
    public static Highscores highscores ;	//Classe de gestion des highscores
    public static int position ; 	//Position dans la liste des highscores à l'issu du jeu
    public static final Niveau[] niveaux = {
   new Niveau("Progressif", false, false,  0f, new float[][] {{0},{10,10}}                            	 , 11, new int[] {0,1,2,3,4,5,6,7,8,9,10,11}, new int[] {1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6 }) ,
   new Niveau("Niveau 1", false, false,  0f, new float[][] {{0},{10,10}}                            	 , 11, new int[] {0,1,2,3,4,5,6,7,8,9,10,11}, new int[] {1,1}) ,
   new Niveau("Niveau 2",  true, false, 90f, new float[][] {{0.01f,0f},{10,5,5}}                  		 , 10, new int[] {0,1,2,3,4,5,6,7,8,9,11}, new int[] {2,2}) ,
   new Niveau("Niveau 3",  true, false, 60f, new float[][] {{0.6f, 0.4f, 0.2f, 0f},{10, 6, 4, 1, 0}}    , 10, new int[] {0,1,2,3,4,5,6,7,8,9,11}, new int[] {3,3}) ,
   new Niveau("Niveau 4",  true, false, 40f, new float[][] {{0.6f, 0.4f, 0.2f, 0f},{10, 6, 4, 1, 0}}	 ,  9, new int[] {0,1,2,3,4,5,6,7,9,11}, new int[] {4,4}) ,
   new Niveau("Niveau 5",  true,  true, 30f, new float[][] {{0.6f, 0.4f, 0.2f, 0f},{10, 6, 4, 1, -1}}	 ,  7, new int[] {0,2,3,5,6,7,9,11}, new int[] {5,5}) ,
   new Niveau("Niveau 6",  true,  true, 25f, new float[][] {{0.6f, 0.4f, 0.2f, 0f},{10, 6, 4, 1, -1}}	 ,  5, new int[] {0,2,5,7,9,11}, new int[] {6,6})    } ;

    public void create() {
        dictionnaires = new Dictionnaires(CHEMIN_FICHIERS_DICTIONNAIRES) ;
        dictionnaires.setDictionnaire(0);		//Initialisation du premier dictionnaire
        
        niveau = niveaux[0] ;	//Par défaut on commence au premier niveau
        niveau.setNbNiveaux(niveaux.length) ;
        
        score = new Score(niveau.getNumero(),dictionnaires.getDictionnaireActuel().langue) ;	//Par défaut on a un score nul
        score.joueur = "Florent" ;

        largeurEcran = Gdx.graphics.getWidth();
        hauteurEcran = Gdx.graphics.getHeight();

        //Charge et définit les polices et skin d'affichage
        skin = new Skin(Gdx.files.internal(CHEMIN_SKIN)) ;
        policeMots = new BitmapFont(Gdx.files.internal(POLICE_MOTS));
        styleMots = new Label.LabelStyle();
        styleMots.font = policeMots ;
        
        //Chargement des éléments en mémoire
        ChargeImagesPendu();		//Les images de la pendaison progressive
        
        
        chrono = new Chrono() ;	//Crée une instance de la classe Chrono pour chronometrer la partie
        logger = new Logger() ; //Pour enregistrer les bilans des parties
        highscores = new Highscores(niveaux,dictionnaires.getNomsDictionnaires()) ;
        position = -1 ;		//Avant de jouer on n'est pas place dans la liste des highscores

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

    public void render () {
		super.render();
	}

    public static int getLargeurEcran() { 	return largeurEcran ; }
    public static int getHauteurEcran() { 	return hauteurEcran ; }
    public static boolean getDebugState() {  return DEBUG ; }
    public static Skin getSkin() { return skin ; }
    public static Texture[] getImagesPendu() { return imagePendu ; }
    public static int getNbLettresParLigne() {return nbLettresParLigne ; }
    public static EcranAccueil getEcranAccueil() { return ecranAccueil ; }
    public static EcranReglages getEcranReglages() { return ecranReglages ; }
    public static EcranChoixDictionnaire getEcranChoixDictionnaire() { return ecranChoixDictionnaire ; }
    public static EcranJeu getEcranJeu() { return ecranJeu ; }
    public static EcranGagne getEcranGagne() { return ecranGagne ; }
    public static EcranPerdu getEcranPerdu() { return ecranPerdu ; }
    public static EcranHighscores getEcranHighscores() { return ecranHighscores ; }
    public static int getScore() { return score.score; }
    public static int getNbErreurs() { return nbErreurs ; } ;
    public static Niveau getNiveau() { return niveau ; }

    public float getTaillePoliceTitreAdaptee(float contrainte,float tabTaillesAdaptees[][]) {
    	int i ;
    	for (i = 0 ; i < tabTaillesAdaptees[0].length ; i++) {
    		if (contrainte >= tabTaillesAdaptees[0][i]) break ;
    	}
    	return tabTaillesAdaptees[1][i] ;
    }
    
    public static void setLargeurEcran(int l) { largeurEcran = l ; }
    public static void setHauteurEcran(int h) { hauteurEcran = h ; }
    public static void setEcranAccueil(EcranAccueil e) { ecranAccueil = e ; }
    public static void setEcranReglages(EcranReglages e) { ecranReglages = e ; }
    public static void setEcranChoixDictionnaire(EcranChoixDictionnaire e) { ecranChoixDictionnaire = e ; }
    public static void setEcranJeu(EcranJeu e) { ecranJeu = e ; }
    public static void setEcranGagne(EcranGagne e) { ecranGagne = e ; }
    public static void setEcranPerdu(EcranPerdu e) { ecranPerdu = e ; }
    public static void setEcranHighscores(EcranHighscores e) { ecranHighscores = e ; }
    public void setScore(int s) { score.score = s ; }
    public void setNiveau(Niveau n) { niveau = n ; }

    public void pause() {
        if (DEBUG) Gdx.app.log("INFO","L'appli se met en pause...") ;
        if (barreMinuteur != null) barreMinuteur.pause() ;
        if (chrono != null) chrono.pause();
        if (ecranJeu != null && ecranJeu.chronoMot != null) ecranJeu.chronoMot.pause();
    }

    public void resume() {
        if (DEBUG) Gdx.app.log("INFO","L'appli sort de pause...") ;
        if (barreMinuteur != null) barreMinuteur.resume() ;
        if (chrono != null) chrono.reprise();
        if (ecranJeu != null && ecranJeu.chronoMot != null) ecranJeu.chronoMot.reprise();
    }

	public void dispose() {
        if (DEBUG) Gdx.app.log("INFO","Fermeture - Suppression des references aux objets statiques") ;
        if (ecranAccueil != null) ecranAccueil.dispose() ;
        if (ecranChoixDictionnaire != null) ecranChoixDictionnaire.dispose() ;
        if (ecranGagne != null) ecranGagne.dispose() ;
        if (ecranJeu != null) ecranJeu.dispose() ;
        if (ecranPerdu != null) ecranPerdu.dispose() ;
        if (ecranReglages != null) ecranReglages.dispose() ;
        if (ecranHighscores != null) ecranHighscores.dispose() ;
        if (DEBUG) Gdx.app.log("INFO","Fermeture - fermeture du fichier de log") ;
        if (logger != null) logger.fermeture();
        Gdx.app.exit();
	}
}
