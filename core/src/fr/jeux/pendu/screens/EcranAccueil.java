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
    
    Pendu jeu ;	//r�f�rence aux donn�es du jeu
	
    public EcranAccueil(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
    	
    	jeu.setEcranAccueil(this) ; 	//Enregistre la r�f�rence de cet �cran
    	
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        tMenu = new Table();
        tMenu.setFillParent(true);  //La table occupe tout l'�cran
        stage.addActor(tMenu);

        if (Pendu.getDebugState()) tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Jeu du pendu\n", Pendu.getSkin());
        boutonJeu = new TextButton("Jouer", Pendu.getSkin());
        boutonReglages = new TextButton("Reglages", Pendu.getSkin());
        boutonHighscores = new TextButton("Highscores", Pendu.getSkin());
        
        img = Pendu.getImagesPendu()[Pendu.getImagesPendu().length-1] ;	//Derni�re image de pendu (compl�tement pendu)
        imageTitre = new Image(img);

        tMenu.pad(3);
        tMenu.add(titre);
        tMenu.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire
        tMenu.add(boutonJeu).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(boutonReglages).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(boutonHighscores).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        tMenu.row();
        tMenu.add(imageTitre);

        boutonJeu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getDebugState()) Gdx.app.log("INFO","raz du nb de mots devinn�s");
            	Pendu.nbMotsDevines = 0 ;	//On d�bute une nouvelle partie, donc on r�-initialise le nb de mots devinn�s. Le reste sera initialis� dans l'�cran jeu
            	if (Pendu.lNbMotsDevines != null) Pendu.lNbMotsDevines.setText("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevines);

            	jeu.chrono.depart(); 	//Lance le chrono
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
    	titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
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
     * Elimine les r�f�rences statiques aux objets car si l'application est relanc�e juste apr�s �tre quitt�e, toutes les r�f�rences statiques
     * demeurent alors que les objets (�cran, widgets... ) sont eux d�truits
     */
    @Override
    public void dispose() {
    	jeu.setEcranAccueil(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
    	img.dispose();
    	stage.dispose();
    }
}
