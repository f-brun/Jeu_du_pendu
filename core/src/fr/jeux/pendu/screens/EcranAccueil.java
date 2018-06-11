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
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fr.jeux.pendu.Pendu ;

public class EcranAccueil implements Screen {

    public Stage stage ;
    public Table tMenu ;  //Table contenant le menu

    protected TextButton boutton;
    protected Label titre;
    protected Image imageTitre;

    Texture img;
    
    Pendu jeu ;	//référence aux données du jeu
	
    public EcranAccueil(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
    	
    	jeu.setEcranAccueil(this) ; 	//Enregistre la référence de cet écran
    	
        stage = new Stage(new StretchViewport(jeu.getLargeurEcran(),jeu.getHauteurEcran()));

        Gdx.input.setInputProcessor(stage);

        tMenu = new Table();
        tMenu.setFillParent(true);  //La table occupe tout l'écran
        stage.addActor(tMenu);

        if (jeu.getDebugState()) tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        boutton = new TextButton("Jouer", jeu.getSkin());
        titre = new Label("Jeu du pendu\n", jeu.getSkin());
        titre.setFontScale(3);	//Augmente la taille de la police
        img = jeu.getImagesPendu()[jeu.getImagesPendu().length-1] ;	//Dernière image de pendu (complétement pendu)
        imageTitre = new Image(img);

        tMenu.pad(3);
        tMenu.add(titre);
        tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        tMenu.add(boutton);
        tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        tMenu.add(imageTitre);

                try {   //On attend 0,5 s pour que le mot complet ait le temps de s'afficher
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
        
    
        boutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                if (acteur instanceof TextButton) {
                	if (jeu.getEcranJeu() == null) {
                		jeu.setScreen(new EcranJeu(jeu,1));	//Crée l'écran de jeu
                	}
                	else {
                		jeu.setScreen(jeu.getEcranJeu());	//Bascule sur l'écran de jeu déjà existant
                	}
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
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranAccueil","show");
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

    
    @Override
    public void dispose() {
        img.dispose();
        stage.dispose();
    }
}
