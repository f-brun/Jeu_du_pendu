package fr.jeux.pendu.screens;

	import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
	import com.badlogic.gdx.graphics.GL20;
	import com.badlogic.gdx.scenes.scene2d.Actor;
	import com.badlogic.gdx.scenes.scene2d.Stage;
	import com.badlogic.gdx.scenes.scene2d.ui.Cell;
	import com.badlogic.gdx.scenes.scene2d.ui.Label;
	import com.badlogic.gdx.scenes.scene2d.ui.Table;
	import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
	import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
	import com.badlogic.gdx.utils.Align;
	import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.GestionClavier.EcouteClavier;
/**
 * Ecran de choix de la liste de mot ("dictionnaire") a utiliser.
 * @author Florent Brun
 *
 */
	public class EcranChoixDictionnaire implements Screen{

	    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
																{   3,   2f, 1.5f,   1f, 0.9f, 0.5f}};

	    public Stage stage ;
	    public Table table ;  //Table contenant les choix

	    private TextButton[] boutonLangue ;
	    private TextButton boutonRetour;
	    private Label titre, langueChoisie;
	    private Cell<Label>	celluleLangueChoisie ;
	    public ChangeListener	listenerLangues ;
	    
	    Pendu jeu ;	//r�f�rence aux donn�es du jeu
		
	    public EcranChoixDictionnaire(Pendu jeuEnCours) {
	    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
	    	
	    	Pendu.setEcranChoixDictionnaire(this) ; 	//Enregistre la r�f�rence de cet �cran
	    	
	        stage = new Stage(new ScreenViewport());

	        Gdx.input.setInputProcessor(stage);

	        table = new Table();
	        table.setFillParent(true);  //La table occupe tout l'�cran
	        stage.addActor(table);

	        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.

	        titre = new Label("Reglages\n", Pendu.getSkin());
	        titre.setFontScale(3);	//Augmente la taille de la police
	        table.pad(3);
	        table.add(titre);
	        table.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire

	        boutonLangue = new TextButton[Pendu.dictionnaires.getListeDictionnaires().length] ;

	        //Pr�pare le listener des boutons langue
	        listenerLangues = new ChangeListener() {
	            public void changed(ChangeEvent event, Actor acteur) {
                	int index ;
                	//On r�cup�re l'index � partir du d�but de la cha�ne du nom du bouton
                	index = Integer.parseInt(((TextButton)acteur).getText().toString().substring(1,((TextButton)acteur).getText().toString().indexOf("-")-1)) ;
                	Pendu.dictionnaires.setDictionnaire(index-1) ; //Et on charge le dictionnaire correspondant
                	langueChoisie.setText("\nLangue en cours : "+Pendu.dictionnaires.getDictionnaireActuel().getLangue()+ " ("+Pendu.dictionnaires.getDictionnaireActuel().getNbMots()+" mots)");
                	Pendu.config.setValeurCle(Pendu.CLE_DICTIONNAIRE, index-1) ;
	            }
	        } ;

	        
	        for (int i = 0 ; i < Pendu.dictionnaires.getListeDictionnaires().length ; i++) {
	        	boutonLangue[i] = new TextButton(" "+(i+1)+" - "+Pendu.dictionnaires.getListeDictionnaires()[i][0],Pendu.getSkin());
	        	boutonLangue[i].addListener(listenerLangues) ;
	            table.add(boutonLangue[i]).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS).align(Align.center) ;	//Ajoute le bouton � l'UI
	            table.row();					//Et passe � la ligne suivante
	        }

	        boutonRetour = new TextButton("Retour", Pendu.getSkin());
	        langueChoisie = new Label("Langue en cours : "+Pendu.dictionnaires.getDictionnaireActuel().getLangue()+ " ("+Pendu.dictionnaires.getDictionnaireActuel().getNbMots()+" mots)", Pendu.getSkin()) ;
	        langueChoisie.setWrap(true); //Autorise le passage � la ligne si le texte est trop grand
	        langueChoisie.setAlignment(Align.center); //Centre le texte dans la boite

	        
	        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
	        table.row();
	        table.row() ;
	        celluleLangueChoisie = table.add(langueChoisie).align(Align.center).width(Pendu.getLargeurEcran()) ;
	    
	        boutonRetour.addListener(new ChangeListener() {
	            public void changed(ChangeEvent event, Actor acteur) { Pendu.getEcranChoixDictionnaire().retourReglages() ; }
	        } ) ;
	        
	    }
	    
	    public void retourReglages() {
            if (Pendu.getEcranReglages() == null) {
            	jeu.setScreen(new EcranReglages(jeu));
            }
            else jeu.setScreen(Pendu.getEcranReglages());	//Retourne sur l'�cran des r�glages
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
	        celluleLangueChoisie.width(Pendu.getLargeurEcran()) ;	//Redimensionne la cellule contenant le texte pr�cisant la langue choisie

	        stage.getViewport().update(width, height, true);
	    }

	    @Override
	    public void show() {
	    	InputMultiplexer im ;
	    	
	        if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranChoixDictionnaire - show");

			im = new InputMultiplexer() ;
			im.addProcessor(stage);
			im.addProcessor(new GestionClavier(new EcouteClavier(){
				public void toucheGAUCHE() { } ;
				public void toucheDROITE() { } ;
			    public void toucheESCAPE() { Pendu.getEcranChoixDictionnaire().retourReglages() ; } ; 
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
	    	Pendu.setEcranChoixDictionnaire(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
	    	stage.dispose();
	    }
	}
