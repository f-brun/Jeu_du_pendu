package fr.jeux.pendu;


public class Niveau {
	private static int nbNiveauxCrees = 0 ;
	private int numero ;		//Numéro interne du niveau (en partant de zéro)
	private String denomination ;
	private boolean minuteur ;
	private boolean minuteurFaitPerdre ;
	private float dureeMinuteur ;	//Durée du minuteur en secondes
	private float[][] gains ;	//Tableau contenant les gains pour chaque pourcentage du temps restant sur le minuteur	
	private int nbErreursMax ;
	private int[] imagesPendaison ;	//Liste des numéros des images pour la pendaison (quel numéro d'image correspond à l'étape x de la pendaison)
	private int[] niveauxDeJeu ;	//Liste des niveaux successifs de jeu

	
	public Niveau(String denomination, boolean minuteur, boolean minuteurFaitPerdre, float dureeMinuteur, float[][] gains, int nbErreursMax,int[] imagesPendaison, int[] niveauxDeJeu) {
		this.numero = nbNiveauxCrees++ ;	//Numérote le niveau et incrémente le nombre de niveaux crées
		this.denomination = denomination ;
		this.minuteur = minuteur ;
		this.minuteurFaitPerdre = minuteurFaitPerdre ;
		this.dureeMinuteur = dureeMinuteur ;
		this.gains = gains ;
		this.nbErreursMax = nbErreursMax ;
		this.imagesPendaison = imagesPendaison ;
		this.niveauxDeJeu = niveauxDeJeu ;
	}
	
	public Niveau() {
		denomination = "defaut" ;
		minuteur = false ;
		dureeMinuteur = 60 ;
		gains[0][0] = 0f;
		gains[1][0] = 10f ;
		nbErreursMax = Pendu.imagePendu.length ;
		for (int i = 0 ; i < Pendu.imagePendu.length ; i++) imagesPendaison[i] = i ;
		this.numero = nbNiveauxCrees++ ;	//Numérote le niveau et incrémente le nombre de niveaux crées
		this.niveauxDeJeu = new int[nbNiveauxCrees] ;	//Par défaut on va jouer à tous les niveaux 1 fois
		for (int i = 0 ; i< nbNiveauxCrees ; i++) this.niveauxDeJeu[i] = i ;
	}
	
	public Niveau(String nomNiveau) {
		this() ;		//Constructeur par défaut
		denomination = nomNiveau ;	//On définit juste le nom du niveau
	}
	
    public int calculeGain(float pourcentage) {
    	int i ;
    	for (i = 0 ; i < this.getNiveauDeJeu().getGains()[0].length ; i++) {	//On parcours le tableau des pourcentage pour savoir quand on a un pourcentage de temps restant supérieur
    		if (pourcentage > this.getNiveauDeJeu().getGains()[0][i]) break ;
    	}
    	//Dans le pire des cas on a parcouru tout le tableau sans succès et on a i qui est égal à la taille du tableau des pourcentages, ce qui correspond à la dernière case du tableau des gains
    	return (int) this.getNiveauDeJeu().getGains()[1][i] ;
    }
    
    public int getNumero()	{ return this.numero ; }
    public String getDenomination()	{ return this.denomination ; }
    public boolean getMinuteur()	{return this.minuteur ; }
    public boolean getMinuteurFaitPerdre() {return this.minuteurFaitPerdre ; }
    public float getDureeMinuteur()	{ return this.dureeMinuteur ; }
    private float[][] getGains()	{ return this.gains ; }
    public int getNbErreursMax()	{ return this.nbErreursMax ; }
    public int[] getImagesPendaison()	{ return this.imagesPendaison ; }
    
    public Niveau getNiveauDeJeu() {
    	if (Pendu.nbMotsDevines < niveauxDeJeu.length) {
    		return Pendu.niveaux[niveauxDeJeu[Pendu.nbMotsDevines]] ;    //Renvoi le niveau de la liste correspondant au nombre de mots devinnés
    	}
    	else return Pendu.niveaux[niveauxDeJeu[niveauxDeJeu.length-1]] ;	//Ou le dernier de la liste si on a déjà trouvé plus de mots que dans le tableau
    }
    
    public void setNbNiveaux(int nbNiveaux) { nbNiveauxCrees = nbNiveaux ; }
}
