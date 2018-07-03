package fr.jeux.pendu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
/**
 * Classe de gestion d'un fichier de log qui �crit simplement les information qu'on lui transmet � la fin du fichier de log.
 * @author Florent Brun
 *
 */
public class Logger {

	private static final String NOM_FICHIER_LOG = "log.txt" ;	//Nom par d�faut du fichier de log
	
	private BufferedWriter	writer ;
	
	public Logger() {
		initLogger(NOM_FICHIER_LOG) ;
	}

	public Logger(String nomFichierLog) {
		initLogger(nomFichierLog) ;
	}

	private void initLogger(String nomFichierLog) {
		writer = new BufferedWriter(Gdx.files.local(nomFichierLog).writer(true)) ;	//Ouvre le fichier en �criture en mode append
	}
	
	/**
	 * Ecrit  la cha�ne d'information en argument dans le fichier de log en la faisant pr�c�der d'un horodatage
	 * @param information Chaine � �crire dans le log
	 */
	public void ecritLog(String information) {
		try {
			writer.newLine();
			writer.write(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(new Date()));	//Fabrique l'hotodatage
			writer.write(information) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fermeture() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
