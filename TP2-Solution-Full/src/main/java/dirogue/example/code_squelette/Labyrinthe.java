package dirogue.example.code_squelette;

/**
 * Représentation d'un labyrinthe (graphe non-orienté). La méthode
 * ajouteEntree(Exterieur out, Piece e) doit être appellée au minimum une
 * fois.
 * <p/>
 * 
 * NB: NE PAS CHANGER CE FICHIER
 * 
 * @author Michalis Famelis
 *
 */
public interface Labyrinthe {

	/**
	 * Retourne un tableau avec toutes les Pieces du Labyrinthe.
	 * 
	 * @return - l'ensemble des Pieces.
	 */
	public Piece[] getPieces();

	/**
	 * Retourne le nombre total de Pieces dans le Labyrinthe.
	 * 
	 * @return - le nombre de Pieces.
	 */
	public int nombreDePieces();

	/**
	 * Crée et ajoute une piece.
	 * 
	 * @param e - une piece
	 */
	public int ajoutePiece(Piece e);

	/**
	 * Crée un corridor (lien) entre l'Exterieur et une Piece. Si l'un ou les
	 * deux paramètres ne font pas partie du Labyrinthe, il les ajoute. Cette
	 * méthode doit être invoquée au minimum une fois.
	 * 
	 * @param out - l'Exterieur (vous pouvez prendre une instance comme
	 *           Exterieur.getExterieur())
	 * @param e - une autre Piece
	 */
	public void ajouteEntree(Exterieur out, Piece e);

	/**
	 * Crée un corridor (lien) entre deux Pieces. Si l'une ou les deux
	 * Pieces ne font pas partie du Labyrinthe, il les ajoute.
	 * 
	 * @param e1 - une Piece
	 * @param e2 - une autre Piece
	 */
	public void ajouteCorridor(Piece e1, Piece e2);

	/**
	 * Crée un corridor (lien) entre deux Pieces. Si l'une ou les deux
	 * Pieces ne font pas partie du Labyrinthe, il leve une exception.
	 * 
	 * @param e1ID - ID d'une Piece
	 * @param e2ID - ID d'une autre Piece
	 * @throws PieceNotFoundException si la piece est introuvable
	 */
	public void ajouteCorridor(int e1ID, int e2ID) throws PieceNotFoundException;

	/**
	 * Retourne vrai si et seulement si deux Pieces font partie du Labyrinthe et
	 * sont connectées via un corridor. Les corridors ne sont pas orientés.
	 * 
	 * @param e1 - une Piece
	 * @param e2 - une autre Piece
	 * @return - true si e1 et e2 sont dans le Labyrinthe et il y a un corridor
	 *         entre eux; false sinon.
	 */
	public boolean existeCorridorEntre(Piece e1, Piece e2);

	/**
	 * Retourne l'ensemble des Pieces reliées (par des corridors) à la Piece donnée
	 * 
	 * Pour un Labyrinthe lab, l'appel
	 * lab.PiecesConnectes(Exterieur.getExterieur()) doit nous donner
	 * l'ensemble des entrées.
	 * 
	 * @param e - une Piece
	 * @return - un tableau de Pieces, reliées au e; null si e ne fait pas
	 *         partie du Labyrinthe
	 */
	public Piece[] getPiecesConnectees(Piece e);



}
