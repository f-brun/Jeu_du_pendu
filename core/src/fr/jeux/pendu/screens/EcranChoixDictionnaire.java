package fr.jeux.pendu.screens;

	import com.badlogic.gdx.Gdx;
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

	import fr.jeux.pendu.Dictionnaire;
	import fr.jeux.pendu.Pendu;

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
	    
	    Pendu jeu ;	//référence aux données du jeu
		
	    public EcranChoixDictionnaire(Pendu jeuEnCours) {
	    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
	    	
	    	jeu.setEcranChoixDictionnaire(this) ; 	//Enregistre la référence de cet écran
	    	
	        stage = new Stage(new ScreenViewport());

	        Gdx.input.setInputProcessor(stage);

	        table = new Table();
	        table.setFillParent(true);  //La table occupe tout l'écran
	        stage.addActor(table);

	        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.

	        titre = new Label("Reglages\n", Pendu.getSkin());
	        titre.setFontScale(3);	//Augmente la taille de la police
	        table.pad(3);
	        table.add(titre);
	        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire

	        boutonLangue = new TextButton[Pendu.listeDictionnaires.length] ;

	        //Prépare le listener des boutons langue
	        listenerLangues = new ChangeListener() {
	            public void changed(ChangeEvent event, Actor acteur) {
                	int index ;
                	//On récupère l'index à partir du début de la chaîne du nom du bouton
                	index = Integer.parseInt(((TextButton)acteur).getText().toString().substring(1,((TextButton)acteur).getText().toString().indexOf("-")-1)) ;
                	Pendu.dictionnaire = new Dictionnaire(Pendu.listeDictionnaires[index-1][2]) ; //Et on charge le dictionnaire correspondant
                	langueChoisie.setText("\nLangue en cours : "+Pendu.dictionnaire.getLangue()+ " ("+Pendu.dictionnaire.getNbMots()+" mots)");
	            }
	        } ;

	        
	        for (int i = 0 ; i < Pendu.listeDictionnaires.length ; i++) {
	        	boutonLangue[i] = new TextButton(" "+(i+1)+" - "+Pendu.listeDictionnaires[i][0],Pendu.getSkin());
	        	boutonLangue[i].addListener(listenerLangues) ;
	            table.add(boutonLangue[i]).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS).align(Align.center) ;	//Ajoute le bouton à l'UI
	            table.row();					//Et passe à la ligne suivante
	        }

	        boutonRetour = new TextButton("Retour", Pendu.getSkin());
	        langueChoisie = new Label("Langue en cours : "+Pendu.dictionnaire.getLangue()+ " ("+Pendu.dictionnaire.getNbMots()+" mots)", Pendu.getSkin()) ;
	        langueChoisie.setWrap(true); //Autorise le passage à la ligne si le texte est trop grand
	        langueChoisie.setAlignment(Align.center); //Centre le texte dans la boite

	        
	        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
	        table.row();
	        table.row() ;
	        celluleLangueChoisie = table.add(langueChoisie).align(Align.center).width(Pendu.getLargeurEcran()) ;
	    
	        boutonRetour.addListener(new ChangeListener() {
	            public void changed(ChangeEvent event, Actor acteur) {
	                if (acteur instanceof TextButton) {
	               		jeu.setScreen(Pendu.getEcranReglages());	//Retourne sur l'écran des réglages
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
	    	if (Pendu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
	   		titre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
	        celluleLangueChoisie.width(Pendu.getLargeurEcran()) ;	//Redimensionne la cellule contenant le texte précisant la langue choisie

	        stage.getViewport().update(width, height, true);
	    }

	    @Override
	    public void show() {
	        if (Pendu.getDebugState()) Gdx.app.log("EcranChoixDictionnaire","show");
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
