package dirogue.example.code_squelette;

/**
 * La representation d'une aventure du point de vue du joueur. Les classes qui
 * implémentent l’interface Labyrinthe ne représentent que les aspects spatiaux
 * du jeu. Les classes qui héritent de la classe abstraite Aventure contiennent un Labyrinthe
 * et nous permettent d’interroger le labyrinthe du point de vue du joueur. Cela se
 * fait grâce à un ensemble de méthodes qui nous permettent d’analyser le contenu
 * d’un donjon : par exemple, s’il contient des dangers, des trésors, s’il est gagnable, etc.
 * <p/>
 * 
 * NB: NE PAS CHANGER CE FICHIER
 * 
 * @author Michalis Famelis
 *
 */
public abstract class Aventure {

	/**
	 * Toute aventure doit contenir une carte.
	 */
	protected Labyrinthe carte;

	/**
	 * Initialize une Aventure avec un Labyrinthe
	 * 
	 * @param c - la carte de l'Aventure
	 */
	public Aventure(Labyrinthe c) {
		this.carte = c;
	}

	/**
	 * Retourne la carte de l'Aventure.
	 * 
	 * @return la carte
	 */
	public Labyrinthe getLabyrinthe() {
		return this.carte;
	}

	/**
	 * Nous dit si l'aventure contient des méchants.
	 * 
	 * @return true si et seulement si la carte de l'Aventure ne contient pas de
	 *         Pieces avec des RencontreType.MONSTRE ou RencontreType.BOSS.
	 */
	public abstract boolean estPacifique();

	/**
	 * Nous dit si l'aventure est rentable, en d'autre termes si elle contient du
	 * tresor du tout.
	 * 
	 * @return true si et seulement si la carte de l'Aventure contient au minimum une
	 *         Piece avec RencontreType.TRESOR
	 */
	public abstract boolean contientDuTresor();

	/**
	 * Nous dit combien de RencontreType.TRESOR existent dans l'Aventure.
	 * 
	 * @return le nombre de RencontreType.TRESOR dans l'Aventure.
	 */
	public abstract int getTresorTotal();

	/**
	 * Nous dit si l'Aventure contient un boss (ou plusieurs).
	 * 
	 * @return true si et seulement si l'Aventure contient une Rencontre.BOSS ou plus.
	 */
	public abstract boolean contientBoss();

	/**
	 * Une façon de determiner si le donjon est gagnable. Retourne un chemin en partant
	 * de l'Exterieur jusqu'au Boss. Nous faisons l'hypothèse qu'un Labyrinthe sera
	 * toujours construit de façon à ce qu'en commençant depuis l'Extérieur (ID=0) et en
	 * suivant des Pieces avec des ID en ordre croissant, nous devrions trouver le Boss,
	 * tomber dans une boucle, ou arreter à une Piece sans sortie.
	 * 
	 * @return - une chaine de Pieces telle que chaine[0].getID()==0,
	 *         chaine[i+1].getID()=chaine[i].getID()+1 et
	 *         chaine[chaine.length-1].getRencontreType==Rencontre.BOSS; s'il n'est
	 *         pas possible de trouver le Boss, retourne un tableau vide.
	 */
	public abstract Piece[] cheminJusquAuBoss();

}
