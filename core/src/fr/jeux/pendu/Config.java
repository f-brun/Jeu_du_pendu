package fr.jeux.pendu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;

public class Config {
	
	private final String NOM_FICHIER_CONFIG = "config" ;
	public static final String DELIMITEUR = "=" ;	
	private final int CLE = 0 ;			//Index de la cl� dans le tableau de String qui constitue un �l�ment de config
	private final int VALEUR = 1 ;		//Index de la valeur dans le tableau de String qui constitue un �l�ment de config
	

	
	private ArrayList<String[]> elementsConfig ;
	private BufferedWriter	writer ;
	private BufferedReader	reader ;
	
	public Config() {
		elementsConfig = new ArrayList<String[]>() ;
		if (Pendu.getDebugState()) Gdx.app.log("INFO", "Lecture du fichier de configuration");
		litFichierConfig(NOM_FICHIER_CONFIG) ;
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
				element[CLE] = ligne.substring(0,ligne.indexOf(DELIMITEUR)) ;	//La cl� est du d�but jusqu'au d�limiteur
				element[VALEUR] = ligne.substring(ligne.indexOf(DELIMITEUR)+1,ligne.length()) ;	//La valeur est juste apr�s le d�limiteur jusqu'� la fin de la ligne
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
			writer = new BufferedWriter(Gdx.files.local(NOM_FICHIER_CONFIG).writer(false)) ;	//Ouvre le fichier en �criture en mode overwrite
		}
		catch  (Exception e) { e.printStackTrace(); writer = null ;}
			
		if (writer == null) {
			Gdx.app.log("ERROR", "Impossible d'ouvrir en ecriture le fichier de config : "+NOM_FICHIER_CONFIG);
			return ;
		}

		String ligne ;						//La ligne � �crire dans le fichier
		String[] element = new String[2] ;	//L'�l�ment contenant les informations � �crire sur la ligne

		Iterator<String[]> i = elementsConfig.iterator();
		while(i.hasNext()){
			  element = (String[]) i.next();
			  ligne  = element[CLE] + DELIMITEUR + element[VALEUR] ;		//Fabrique la ligne a ins�rer dans le fichier de config
			try {
				writer.write(ligne);
				writer.newLine();
			} catch (IOException e) { e.printStackTrace(); }
		}
		try {
			writer.close();
		} catch (IOException e) {
			Gdx.app.log("ERROR", "Impossible de fermer le fichier de config : "+NOM_FICHIER_CONFIG);
			e.printStackTrace();
		}
	}
	
	private String rechercheValeurCle(String cle) {
		String[] element = new String[2] ;
		
		Iterator<String[]> i = elementsConfig.iterator();
		while(i.hasNext()){									//On parcourt la liste � la recherche de la cl�
			  element =  (String[]) i.next();
			  if (element[CLE].equals(cle)) return element[VALEUR] ;	//Si on la trouve, on fixe la valeur et on la renvoie � l'appelant
		}
		return null ;	//Si on n'a pas trouv� la cl�, on renvoie null
	}
	
	/**
	 * Recup�re la valeur d'une cl� dans la config actuelle
	 * @param cle cl� dont on veut connaitre la valeur
	 * @param valeurParDefaut valeur a donner si la cl� est introuvable
	 * @return valeur de la cl� dans la config ou valeurParDefaut si la cl� n'est pas dans la config
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
		return -1 ;		//On a rien trouv�
	}
	
	public boolean setValeurCle(String cle, String valeur) {
		String[] element = new String[2] ;
		element[CLE] = cle ;
		element[VALEUR] = valeur ;
		int index = getIndexCle(cle) ;
		if (index < 0) {
			elementsConfig.add(element) ;	//On ajoute l'�l�ment � la liste des �l�ments de config
			return false ;	//On a pas r�ussit � trouver la cl� - il faut donc la cr�er
		}
		elementsConfig.set(index,element) ;	//Fixe la valeur de l'�l�ment
		return true ;
	}

	public boolean setValeurCle(String cle, int valeur) {
		return setValeurCle(cle, Integer.toString(valeur)) ;
	}
}
