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

    private TextButton[] boutonLangue ;
    private TextButton boutonRetour;
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
        table.pad(3);
        table.add(titre);
        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire

        boutonLangue = new TextButton[jeu.listeDictionnaires.length] ;

        //Prépare le listener des boutons langue
        listenerLangues = new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                if (acteur instanceof TextButton) {
                	int index ;
                	//On récupère l'index à partir du début de la chaîne du nom du bouton
                	index = Integer.parseInt(((TextButton)acteur).getText().toString().substring(1,((TextButton)acteur).getText().toString().indexOf("-")-1)) ;
                	jeu.dictionnaire = new Dictionnaire(jeu.listeDictionnaires[index-1][2]) ; //Et on charge le dictionnaire correspondant
                	langueChoisie.setText("\nLangue en cours : "+jeu.dictionnaire.getLangue());
                }
            }
        } ;

        
        for (int i = 0 ; i < jeu.listeDictionnaires.length ; i++) {
        	boutonLangue[i] = new TextButton(" "+(i+1)+" - "+jeu.listeDictionnaires[i][0]+ " ("+jeu.listeDictionnaires[i][1]+" mots) ",jeu.getSkin());
        	boutonLangue[i].addListener(listenerLangues) ;
            table.add(boutonLangue[i]) ;	//Ajoute le bouton à l'UI
            table.row();					//Et passe à la ligne suivante
        }

        boutonRetour = new TextButton("Retour", jeu.getSkin());
        langueChoisie = new Label("\nLangue en cours : "+jeu.dictionnaire.getLangue(), jeu.getSkin()) ;

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
