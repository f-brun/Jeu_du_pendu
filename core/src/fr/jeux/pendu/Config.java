package fr.jeux.pendu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
/**
 * Classe de gestion d'informations de configuration persistantes. Les informations sont lues à partir du fichier de configuration lors de la création d'une instance de la classe.
 * Puis on accède en lecture ou écriture à ces informations et elles sont écrites dans le fichier de config à la fin par l'appel à la méthode sauvegarde().
 * @author Florent Brun
 *
 */
public class Config {
	
	private final String NOM_FICHIER_CONFIG = "config" ;	//Nom par défaut du fichier de config
	public static final String DELIMITEUR = "=" ;	
	private final int CLE = 0 ;			//Index de la clé dans le tableau de String qui constitue un élément de config
	private final int VALEUR = 1 ;		//Index de la valeur dans le tableau de String qui constitue un élément de config
	
	private ArrayList<String[]> elementsConfig ;
	private String nomFichierConfig ;
	private BufferedWriter	writer ;
	private BufferedReader	reader ;

	/**
	 * Constructeur sans argument utilisant le nom de fichier de config par défaut
	 */
	public Config() {
		initConfig(NOM_FICHIER_CONFIG) ;
	}

	/**
	 * Constructeur demandant le nom du fichier de config. Permet de gérer éventuellement plusieurs fichiers de config simultanément.
	 * @param nomFichierConfig Nom (et chemin) du fichier de config dans le stockage interne. S'il n'existe pas ou s'il est vide, aucun information ne sera disponible, mais la structure mise en place pour enregistrer de nouvelles informations.
	 */
	public Config(String nomFichierConfig) {
		initConfig(nomFichierConfig) ;
	}
	
	private void initConfig(String nomFichierConfig) {
		this.nomFichierConfig = nomFichierConfig ;
		elementsConfig = new ArrayList<String[]>() ;
		if (Pendu.getDebugState()) Gdx.app.log("INFO", "Lecture du fichier de configuration");
		litFichierConfig(nomFichierConfig) ;
	}
	
	private void litFichierConfig(String fichier) {
		try {
			reader = new BufferedReader(Gdx.files.local(fichier).reader()) ;	//Ouvre le fichier en lecture
		}
		catch (Exception e) { reader = null ;}
		
		if (reader == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en lecture le fichier de config : "+fichier);
			return ;
		}

		String ligne = null ;
		do {
			try {
				ligne = reader.readLine() ;
			} catch (IOException e) { e.printStackTrace(); }

			if (ligne != null) {
				String[] element = new String[2] ;
				element[CLE] = ligne.substring(0,ligne.indexOf(DELIMITEUR)) ;	//La clé est du début jusqu'au délimiteur
				element[VALEUR] = ligne.substring(ligne.indexOf(DELIMITEUR)+1,ligne.length()) ;	//La valeur est juste après le délimiteur jusqu'à la fin de la ligne
				elementsConfig.add(element) ;
			}
		} while (ligne != null) ;
		
		try { reader.close();} catch (IOException e) {e.printStackTrace();	}
	}
	
	/**
	 * Sauvegarde la config actuelle
	 */
	public void sauvegarde() {

		try {
			writer = new BufferedWriter(Gdx.files.local(this.nomFichierConfig).writer(false)) ;	//Ouvre le fichier en écriture en mode overwrite
		}
		catch  (Exception e) { e.printStackTrace(); writer = null ;}
			
		if (writer == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en ecriture le fichier de config : " + this.nomFichierConfig);
			return ;
		}

		String ligne ;						//La ligne à écrire dans le fichier
		String[] element = new String[2] ;	//L'élément contenant les informations à écrire sur la ligne

		Iterator<String[]> i = elementsConfig.iterator();
		while(i.hasNext()){
			  element = (String[]) i.next();
			  ligne  = element[CLE] + DELIMITEUR + element[VALEUR] ;		//Fabrique la ligne a insérer dans le fichier de config
			try {
				writer.write(ligne);
				writer.newLine();
			} catch (IOException e) { e.printStackTrace(); }
		}
		try {
			writer.close();
		} catch (IOException e) {
			Gdx.app.log("ERROR", "Impossible de fermer le fichier de config : "+ this.nomFichierConfig);
			e.printStackTrace();
		}
	}
	
	private String rechercheValeurCle(String cle) {
		String[] element = new String[2] ;
		
		Iterator<String[]> i = elementsConfig.iterator();
		while(i.hasNext()){									//On parcourt la liste à la recherche de la clé
			  element =  (String[]) i.next();
			  if (element[CLE].equals(cle)) return element[VALEUR] ;	//Si on la trouve, on fixe la valeur et on la renvoie à l'appelant
		}
		return null ;	//Si on n'a pas trouvé la clé, on renvoie null
	}
	
	/**
	 * Recupére la valeur d'une clé dans la config actuelle
	 * @param cle clé dont on veut connaitre la valeur
	 * @param valeurParDefaut valeur a donner si la clé est introuvable
	 * @return valeur de la clé dans la config ou valeurParDefaut si la clé n'est pas dans la config
	 */
	public int getValeurCle(String cle, int valeurParDefaut) {
		String valeur = rechercheValeurCle(cle) ;
		if (valeur == null) return valeurParDefaut ;
		return Integer.parseInt(valeur) ;
	}
	public float getValeurCle(String cle, float valeurParDefaut) {
		String valeur = rechercheValeurCle(cle) ;
		if (valeur == null) return valeurParDefaut ;
		return Float.parseFloat(valeur) ;
	}
	public String getValeurCle(String cle, String valeurParDefaut) {
		String valeur = rechercheValeurCle(cle) ;
		if (valeur == null) return valeurParDefaut ;
		return valeur ;
	}
	
	private int getIndexCle(String cle) {
		for (int i = 0 ; i < elementsConfig.size() ; i++) {
			if (elementsConfig.get(i)[CLE].equals(cle) ) return i ;
		}
		return -1 ;		//On a rien trouvé
	}
	
	public boolean setValeurCle(String cle, String valeur) {
		String[] element = new String[2] ;
		element[CLE] = cle ;
		element[VALEUR] = valeur ;
		int index = getIndexCle(cle) ;
		if (index < 0) {
			elementsConfig.add(element) ;	//On ajoute l'élément à la liste des éléments de config
			return false ;	//On a pas réussit à trouver la clé - il faut donc la créer
		}
		elementsConfig.set(index,element) ;	//Fixe la valeur de l'élément
		return true ;
	}

	public boolean setValeurCle(String cle, int valeur) {
		return setValeurCle(cle, Integer.toString(valeur)) ;
	}
}
