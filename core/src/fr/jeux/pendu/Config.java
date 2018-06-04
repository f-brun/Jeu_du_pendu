package fr.jeux.pendu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.ArrayList;
/**
 *
 * @author Florent
 */
public class Config {
    private static Config instance;
    public static final int LARGEUR_CIBLE = 800 ;   //Largeur de l'écran virtuel cible
    public static final int HAUTEUR_CIBLE = 600 ;   //Hauteur de l'écran virtuel cible
    public static boolean DEBUG = false ;
    public static final int TAILLE_BUFFER = 256*1024 ;   //Taille du buffer de lecture du dictionnaire
    public static final String CHEMIN_SKIN = "skin/freezing-ui.json" ;
    public static final String POLICE_MOTS = "Consolas.fnt" ;
    public static int largeurEcran ;
    public static int hauteurEcran ;
    public static BitmapFont policeMots ;
    public static Label.LabelStyle styleMots ;
    public static Stage stage ;
    public static Table tMenu ;  //Table contenant le menu
    public static Table tPartie ;  //Table contenant la partie
    public static Table tFin ;  //Table contenant l'écran de fin (gagné ou perdu)
    public static Skin skin ; //Skin utilisée par l'UI
    public static Image affichagePendu;  //Elément d'UI contenant l'image du pendu
    public static Partie partie ; //Instance de la partie en cours
    public static String motADeviner ;
    public static String motDevine ;
    public static Label lMotDevine; //texte du mot à deviner
    public static String lettresProposes ;
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
    
    Config() {
    largeurEcran = Gdx.graphics.getWidth();
    hauteurEcran = Gdx.graphics.getHeight();
    skin = new Skin(Gdx.files.internal(CHEMIN_SKIN)) ;
    stage = new Stage(new StretchViewport(LARGEUR_CIBLE,HAUTEUR_CIBLE));
    partie = null ; //Au début il n'y a pas de partie en cours
    policeMots = new BitmapFont(Gdx.files.internal(POLICE_MOTS));
    styleMots = new Label.LabelStyle();
    styleMots.font = policeMots ;
    styleMots.fontColor = Color.WHITE;
    
    Gdx.input.setInputProcessor(stage);
    score = 0 ;
    }
    public void dispose() {
        stage.dispose();
    }
}
