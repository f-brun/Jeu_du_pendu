package fr.jeux.pendu;


public class Niveau {
	private static int nbNiveauxCrees = 0 ;
	public int numero ;		//Num�ro interne du niveau (en partant de z�ro)
	public String denomination ;
	public boolean minuteur ;
	public boolean minuteurFaitPerdre ;
	public float dureeMinuteur ;	//Dur�e du minuteur en secondes
	public float[][] gains ;	//Tableau contenant les gains pour chaque pourcentage du temps restant sur le minuteur	
	public int nbErreursMax ;
	public int[] imagesPendaison ;	//Liste des num�ros des images pour la pendaison (quel num�ro d'image correspond � l'�tape x de la pendaison)

	
	public Niveau(String denomination, boolean minuteur, boolean minuteurFaitPerdre, float dureeMinuteur, float[][] gains, int nbErreursMax,int[] imagesPendaison) {
		this.denomination = denomination ;
		this.minuteur = minuteur ;
		this.minuteurFaitPerdre = minuteurFaitPerdre ;
		this.dureeMinuteur = dureeMinuteur ;
		this.gains = gains ;
		this.nbErreursMax = nbErreursMax ;
		this.imagesPendaison = imagesPendaison ;
		this.numero = nbNiveauxCrees++ ;	//Num�rote le niveau et incr�mente le nombre de niveaux cr�es
	}
	
	public Niveau() {
		numero = 0 ;
		denomination = "defaut" ;
		minuteur = false ;
		dureeMinuteur = 60 ;
		gains[0][0] = 0f;
		gains[1][0] = 10f ;
		nbErreursMax = Pendu.imagePendu.length ;
		for (int i = 0 ; i < Pendu.imagePendu.length ; i++) imagesPendaison[i] = i ;
		this.numero = nbNiveauxCrees++ ;	//Num�rote le niveau et incr�mente le nombre de niveaux cr�es
	}
	
	public Niveau(String nomNiveau) {
		this() ;		//Constructeur par d�faut
		denomination = nomNiveau ;	//On d�finit juste le nom du niveau
	}
	
    public int calculeGain(float pourcentage) {
    	int i ;
    	for (i = 0 ; i < gains[0].length ; i++) {	//On parcours le tableau des pourcentage pour savoir quand on a un pourcentage de temps restant sup�rieur
    		if (pourcentage > gains[0][i]) break ;
    	}
    	//Dans le pire des cas on a parcouru tout le tableau sans succ�s et on a i qui est �gal � la taille du tableau des pourcentages, ce qui correspond � la derni�re case du tableau des gains
    	return (int) gains[1][i] ;
    }
}
