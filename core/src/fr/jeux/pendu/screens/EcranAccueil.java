package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.Pendu ;
import fr.jeux.pendu.GestionClavier.EcouteClavier;
import fr.jeux.pendu.Score;

public class EcranAccueil implements Screen {
	
    private static final float[][] TAILLES_POLICE_ADAPTEES = {{600, 400,  300,  200,  100,    0},
															 {  3,   2, 1.5f,   1f, 0.5f, 0.3f}};
    private static final float ESPACEMENT_BOUTONS = 15 ;
    private static final float HAUTEUR_UI = 600 ;

    public Stage stage ;
    private Table tMenu ;  //Table contenant le menu

    private TextButton boutonJeu, boutonReglages, boutonHighscores, boutonQuitter ;
    protected Label titre;
    private Image imageTitre;

    private Texture img;
    private Cell[]	cellulesUI ;

    private Pendu jeu ;	//référence aux données du jeu
	
    public EcranAccueil(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
    	
    	Pendu.setEcranAccueil(this) ; 	//Enregistre la référence de cet écran
    	
    	creeUI() ;
    }

    private void creeUI() {
        stage = new Stage(new ScreenViewport());

        tMenu = new Table();
        tMenu.setFillParent(true);  //La table occupe tout l'écran
        stage.addActor(tMenu);

        if (Pendu.getDebugState()) tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Jeu du pendu", Pendu.getSkin());
        boutonJeu = new TextButton("Jouer", Pendu.getSkin());
        boutonReglages = new TextButton("Reglages", Pendu.getSkin());
        boutonHighscores = new TextButton("Highscores", Pendu.getSkin());
        boutonQuitter = new TextButton("Quitter",Pendu.getSkin()) ;
        
        img = Pendu.getImagesPendu()[Pendu.getImagesPendu().length-1] ;	//Dernière image de pendu (complétement pendu)
        imageTitre = new Image(img);

        cellulesUI = new Cell[5] ;	//Déclare 5 cellules pour l'écran
        cellulesUI[0] = tMenu.add(titre) ;
        tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        cellulesUI[1] = tMenu.add(boutonJeu).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
        tMenu.row();
        cellulesUI[2] = tMenu.add(boutonReglages).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
        tMenu.row();
        cellulesUI[3] = tMenu.add(boutonHighscores).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
        tMenu.row();
        cellulesUI[4] = tMenu.add(boutonQuitter).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(imageTitre);

        boutonJeu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getDebugState()) Gdx.app.log("INFO","raz du nb de mots devinnés");
            	Pendu.nbMotsDevines = 0 ;	//On débute une nouvelle partie, donc on ré-initialise le nb de mots devinnés. Le reste sera initialisé dans l'écran jeu
            	Pendu.score = new Score(Pendu.score.getJoueur(),Pendu.getNiveau().getNumero(),Pendu.dictionnaires.getDictionnaireActuel().getLangue()) ;    //On refait un score à 0 avec même joueur, niveau et dico
            	if (Pendu.lNbMotsDevines != null) Pendu.lNbMotsDevines.setText("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevines);

            	Pendu.chrono.depart(); 	//Lance le chrono
            	if (Pendu.getEcranJeu() == null) {
               		jeu.setScreen(new EcranJeu(jeu));	//Crée l'écran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranJeu());	//Bascule sur l'écran de jeu déjà existant
               	}
            }
        } ) ;
        
        boutonReglages.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
               	if (Pendu.getEcranReglages() == null) {
               		jeu.setScreen(new EcranReglages(jeu));	//Crée l'écran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranReglages());	//Bascule sur l'écran de jeu déjà existant
               	}
            }
        } ) ;
 
        boutonHighscores.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
               	if (Pendu.getEcranHighscores() == null) {
               		jeu.setScreen(new EcranHighscores(jeu));	//Crée l'écran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranHighscores());	//Bascule sur l'écran de jeu déjà existant
               	}
            }
        } ) ;
        
       boutonQuitter.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
               	if (Pendu.DEBUG) Gdx.app.log("INFO", "Appui sur le bouton quitter - fin du programme") ;
               	jeu.quitter() ;
            }
        } ) ;

    }
    
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
    	Pendu.setHauteurEcran(height) ;
    	Pendu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","Redimmensionnement vers "+width+" x "+height);
    	titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage

    	//On redimensionne l'espace entre les boutons
		for (int i = 0 ; i < cellulesUI.length ; i++) cellulesUI[i].space(ESPACEMENT_BOUTONS*((float)Pendu.getHauteurEcran()/HAUTEUR_UI)) ;
		
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	InputMultiplexer im ;

    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranAccueil - show");
    	Pendu.position = -1 ;	//Remet à -1 la position dans les highscores avant une nouvelle partie
    	
		im = new InputMultiplexer() ;
		im.addProcessor(stage);
		im.addProcessor(new GestionClavier(new EcouteClavier(){
			public void toucheGAUCHE() { }
			public void toucheDROITE() { }
		    public void toucheESCAPE() { if (Pendu.getDebugState()) Gdx.app.log("INFO","Sortie du programme par touche Esc ou Back"); jeu.quitter() ; }
		}));
		Gdx.input.setInputProcessor(im) ;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    
    /**
     * Elimine les références statiques aux objets car si l'application est relancée juste après être quittée, toutes les références statiques
     * demeurent alors que les objets (écran, widgets... ) sont eux détruits
     */
    @Override
    public void dispose() {
    	Pendu.setEcranAccueil(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
    	img.dispose();
    	stage.dispose();
    }
}
