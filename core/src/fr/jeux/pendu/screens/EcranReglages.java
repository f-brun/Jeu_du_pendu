package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.Pendu;

public class EcranReglages implements Screen {

    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
															{   3,   2f, 1.5f,   1f, 0.9f, 0.5f}};

    public Stage stage ;
    public Table table ;  //Table contenant les choix

    private List<String> listeNiveaux ;
    private String[] intitulesNiveaux ;
    private TextButton boutonDictionnaire, boutonRetour;
    private Label titre, langueChoisie;
    private Cell<Label>	celluleLangueChoisie ;
    
    Pendu jeu ;	//référence aux données du jeu
	
    public EcranReglages(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
    	
    	jeu.setEcranReglages(this) ; 	//Enregistre la référence de cet écran
    	
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);  //La table occupe tout l'ï¿½cran
        stage.addActor(table);

        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.

        titre = new Label("Reglages\n", Pendu.getSkin());
        titre.setFontScale(3);	//Augmente la taille de la police
        table.pad(3);
        table.add(titre);
        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire

    	//Crée la liste de sélection du niveau de difficulté
        listeNiveaux = new List<String>(Pendu.getSkin()) ;
        intitulesNiveaux = new String[Pendu.niveaux.length] ;
        for (int i = 0 ; i < Pendu.niveaux.length ; i++) {
        	intitulesNiveaux[i] = Pendu.niveaux[i].denomination ;
        }
        listeNiveaux.setItems(intitulesNiveaux) ;
        
     //Prï¿½pare le listener des boutons langue
        listeNiveaux.addListener( new ChangeListener() {
        	public void changed(ChangeEvent event, Actor acteur) {
                List<String> liste = (List<String>) acteur ;
               	if (liste.getSelectedIndex() == -1) liste.setSelectedIndex(0);  	//S'il n'y a pas de niveau selectionne, on prend le premier de la liste
               	Pendu.niveau = Pendu.niveaux[liste.getSelectedIndex()];
            }
        } );

        table.row() ;
        table.add(listeNiveaux) ;
        
        boutonDictionnaire = new TextButton("Choix dictionnaire",Pendu.getSkin()) ;
        boutonDictionnaire.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                if (Pendu.getEcranChoixDictionnaire() == null) {
                	jeu.setScreen(new EcranChoixDictionnaire(jeu));	//cree l'ecran de choix du dictionnaire s'il n'existe pas
                }
                else jeu.setScreen(Pendu.getEcranChoixDictionnaire());	//Retourne sur l'ï¿½cran d'accueil
            }
        } ) ;
       table.row() ;
        table.add(boutonDictionnaire).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;

        
        boutonRetour = new TextButton("Retour", Pendu.getSkin());
        boutonRetour.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getEcranAccueil() == null ) {
            		jeu.setEcranAccueil(new EcranAccueil(jeu));
            	}
            	else jeu.setScreen(Pendu.getEcranAccueil());	//Retourne sur l'ecran d'accueil
            }
        } ) ;
        table.row() ;
        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
        
        langueChoisie = new Label("\nLangue en cours : "+Pendu.dictionnaire.getLangue(), Pendu.getSkin()) ;
        langueChoisie.setWrap(true); //Autorise le passage a la ligne si le texte est trop grand
        langueChoisie.setAlignment(Align.center); //Centre le texte dans la boite

        table.row() ;
        celluleLangueChoisie = table.add(langueChoisie).align(Align.center).width(Pendu.getLargeurEcran()) ;
    
        
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
   		titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police ï¿½ la hauteur de l'affichage
        celluleLangueChoisie.width(Pendu.getLargeurEcran()) ;	//Redimensionne la cellule contenant le texte precisant la langue choisie

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranReglages - show");
        Gdx.input.setInputProcessor(stage);
        listeNiveaux.setSelectedIndex(Pendu.getNiveau().numero); 	//On se positionne sur le niveau actuel
        langueChoisie.setText("\nLangue en cours : "+Pendu.dictionnaire.getLangue()) ;
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
     * Elimine les references statiques aux objets car si l'application est relancee juste apres etre quittee, toutes les references statiques
     * demeurent alors que les objets (ï¿½cran, widgets... ) sont eux detruits
     */
    @Override
    public void dispose() {
        if (Pendu.DEBUG) Gdx.app.log("INFO","Suppression des references de l'ecran des reglages") ;
    	jeu.setEcranReglages(null) ;	//Supprime la reference a l'ecran pour l'obliger a etre re-cree la prochaine fois
        if (Pendu.DEBUG) Gdx.app.log("INFO","Destruction du stage") ;
    	stage.dispose();
    }
}
