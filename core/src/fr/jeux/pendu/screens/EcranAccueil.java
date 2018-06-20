package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.Pendu ;

public class EcranAccueil implements Screen {
	
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600, 400,  300,  200,  100,    0},
															 {  3,   2, 1.5f,   1f, 0.5f, 0.3f}};

    public Stage stage ;
    public Table tMenu ;  //Table contenant le menu

    protected TextButton boutonJeu, boutonReglages, boutonHighscores ;
    protected Label titre;
    protected Image imageTitre;

    Texture img;
    
    Pendu jeu ;	//référence aux données du jeu
	
    public EcranAccueil(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
    	
    	jeu.setEcranAccueil(this) ; 	//Enregistre la référence de cet écran
    	
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        tMenu = new Table();
        tMenu.setFillParent(true);  //La table occupe tout l'écran
        stage.addActor(tMenu);

        if (Pendu.getDebugState()) tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Jeu du pendu\n", Pendu.getSkin());
        boutonJeu = new TextButton("Jouer", Pendu.getSkin());
        boutonReglages = new TextButton("Reglages", Pendu.getSkin());
        boutonHighscores = new TextButton("Highscores", Pendu.getSkin());
        
        img = Pendu.getImagesPendu()[Pendu.getImagesPendu().length-1] ;	//Dernière image de pendu (complétement pendu)
        imageTitre = new Image(img);

        tMenu.pad(3);
        tMenu.add(titre);
        tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        tMenu.add(boutonJeu).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(boutonReglages).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(boutonHighscores).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(imageTitre);

        boutonJeu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getDebugState()) Gdx.app.log("INFO","raz du nb de mots devinnés");
            	Pendu.nbMotsDevines = 0 ;	//On débute une nouvelle partie, donc on ré-initialise le nb de mots devinnés. Le reste sera initialisé dans l'écran jeu
            	if (Pendu.lNbMotsDevines != null) Pendu.lNbMotsDevines.setText("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevines);

            	jeu.chrono.depart(); 	//Lance le chrono
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

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
    	jeu.setHauteurEcran(height) ;
    	jeu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","Redimmensionnement vers "+width+" x "+height);
    	titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranAccueil - show");
        Gdx.input.setInputProcessor(stage);

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
    	jeu.setEcranAccueil(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
    	img.dispose();
    	stage.dispose();
    }
}
