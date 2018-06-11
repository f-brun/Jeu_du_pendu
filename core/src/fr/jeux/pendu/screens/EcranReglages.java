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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.Dictionnaire;
import fr.jeux.pendu.Pendu;

public class EcranReglages implements Screen {
    public Stage stage ;
    public Table table ;  //Table contenant les choix

    private TextButton boutonFra, boutonEng, boutonRetour;
    private Label titre, langueChoisie;
    public ChangeListener	listenerLangues ;
    
    Pendu jeu ;	//référence aux données du jeu
	
    public EcranReglages(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
    	
    	jeu.setEcranReglages(this) ; 	//Enregistre la référence de cet écran
    	
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);  //La table occupe tout l'écran
        stage.addActor(table);

        if (jeu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Reglages\n", jeu.getSkin());
        titre.setFontScale(3);	//Augmente la taille de la police

        boutonFra = new TextButton(jeu.listeDictionnaires[0][1], jeu.getSkin());
        boutonEng = new TextButton(jeu.listeDictionnaires[1][1], jeu.getSkin());
        boutonRetour = new TextButton("Retour", jeu.getSkin());
        
        langueChoisie = new Label("\nLangue en cours : "+jeu.listeDictionnaires[0][1], jeu.getSkin()) ;
        
        table.pad(3);
        table.add(titre);
        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        table.add(boutonFra) ;
        table.row();
        table.add(boutonEng) ;
        table.row();
        table.add(boutonRetour) ;
        table.row();
        table.add(langueChoisie) ;

    
        boutonRetour.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                if (acteur instanceof TextButton) {
               		jeu.setScreen(jeu.getEcranAccueil());	//Retourne sur l'écran d'accueil
                }
            }
        } ) ;
        
        listenerLangues = new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                if (acteur instanceof TextButton) {
                	int i ;
                	String a,b ;
                	for ( i = 0 ; i < jeu.listeDictionnaires.length ; i++) {
                		a = ((TextButton)acteur).getText().toString() ;
                		b = jeu.listeDictionnaires[i][1] ;
                		if (((TextButton)acteur).getText().toString().equals(jeu.listeDictionnaires[i][1])) {
                			break ;
                		}
                	}
                	if (i>1) {
                		Gdx.app.log("BUG"," i = "+i);
                		i = 1 ;
                	}
                	langueChoisie.setText("\nLangue en cours : "+jeu.listeDictionnaires[i][0]) ;
                	jeu.dictionnaire = new Dictionnaire(jeu.listeDictionnaires[i][2]) ; 
//                	langueChoisie.setText("\nLangue en cours : "+jeu.dictionnaire.getLangue()) ;
                }
            }
        } ;

        boutonFra.addListener(listenerLangues) ;
        boutonEng.addListener(listenerLangues) ;

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
    	if (jeu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranRéglages","show");
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
        stage.dispose();
    }
}
