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

    private Pendu jeu ;	//r�f�rence aux donn�es du jeu
	
    public EcranAccueil(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
    	
    	Pendu.setEcranAccueil(this) ; 	//Enregistre la r�f�rence de cet �cran
    	
    	creeUI() ;
    }

    private void creeUI() {
        stage = new Stage(new ScreenViewport());

        tMenu = new Table();
        tMenu.setFillParent(true);  //La table occupe tout l'�cran
        stage.addActor(tMenu);

        if (Pendu.getDebugState()) tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Jeu du pendu", Pendu.getSkin());
        boutonJeu = new TextButton("Jouer", Pendu.getSkin());
        boutonReglages = new TextButton("Reglages", Pendu.getSkin());
        boutonHighscores = new TextButton("Highscores", Pendu.getSkin());
        boutonQuitter = new TextButton("Quitter",Pendu.getSkin()) ;
        
        img = Pendu.getImagesPendu()[Pendu.getImagesPendu().length-1] ;	//Derni�re image de pendu (compl�tement pendu)
        imageTitre = new Image(img);

        cellulesUI = new Cell[5] ;	//D�clare 5 cellules pour l'�cran
        cellulesUI[0] = tMenu.add(titre) ;
        tMenu.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire
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
            	if (Pendu.getDebugState()) Gdx.app.log("INFO","raz du nb de mots devinn�s");
            	Pendu.nbMotsDevines = 0 ;	//On d�bute une nouvelle partie, donc on r�-initialise le nb de mots devinn�s. Le reste sera initialis� dans l'�cran jeu
            	Pendu.score = new Score(Pendu.score.getJoueur(),Pendu.getNiveau().getNumero(),Pendu.dictionnaires.getDictionnaireActuel().getLangue()) ;    //On refait un score � 0 avec m�me joueur, niveau et dico
            	if (Pendu.lNbMotsDevines != null) Pendu.lNbMotsDevines.setText("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevines);

            	Pendu.chrono.depart(); 	//Lance le chrono
            	if (Pendu.getEcranJeu() == null) {
               		jeu.setScreen(new EcranJeu(jeu));	//Cr�e l'�cran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranJeu());	//Bascule sur l'�cran de jeu d�j� existant
               	}
            }
        } ) ;
        
        boutonReglages.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
               	if (Pendu.getEcranReglages() == null) {
               		jeu.setScreen(new EcranReglages(jeu));	//Cr�e l'�cran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranReglages());	//Bascule sur l'�cran de jeu d�j� existant
               	}
            }
        } ) ;
 
        boutonHighscores.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
               	if (Pendu.getEcranHighscores() == null) {
               		jeu.setScreen(new EcranHighscores(jeu));	//Cr�e l'�cran de jeu
               	}
               	else {
               		jeu.setScreen(Pendu.getEcranHighscores());	//Bascule sur l'�cran de jeu d�j� existant
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
    	titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage

    	//On redimensionne l'espace entre les boutons
		for (int i = 0 ; i < cellulesUI.length ; i++) cellulesUI[i].space(ESPACEMENT_BOUTONS*((float)Pendu.getHauteurEcran()/HAUTEUR_UI)) ;
		
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	InputMultiplexer im ;

    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranAccueil - show");
    	Pendu.position = -1 ;	//Remet � -1 la position dans les highscores avant une nouvelle partie
    	
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
     * Elimine les r�f�rences statiques aux objets car si l'application est relanc�e juste apr�s �tre quitt�e, toutes les r�f�rences statiques
     * demeurent alors que les objets (�cran, widgets... ) sont eux d�truits
     */
    @Override
    public void dispose() {
    	Pendu.setEcranAccueil(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
    	img.dispose();
    	stage.dispose();
    }
}
