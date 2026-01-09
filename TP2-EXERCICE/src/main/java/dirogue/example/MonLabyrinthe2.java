package dirogue.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dirogue.example.code_squelette.*;

/**
 * La classe MonLabyrinthe2 implémente l'interface Labyrinthe et Serializable.
 * Elle représente un labyrinthe composé de pièces et de corridors qui les
 * relient.
 * Cette classe gère les pièces, les corridors, et fournit des méthodes pour
 * ajouter
 * de nouvelles pièces, relier des pièces par des corridors, obtenir des
 * informations
 * sur les pièces connectées et vérifier l'existence de corridors entre des
 * pièces.
 */
public class MonLabyrinthe2 implements Labyrinthe, Serializable {

    private List<Piece> pieces;

    private Map<Integer, List<Integer>> adjList;

    /**
     * Constructeur de la classe MonLabyrinthe2 initialisant la liste des pièces et l'adjacence.
     */
    public MonLabyrinthe2() {
        pieces = new ArrayList<>();
        adjList = new HashMap<>();
    }

    public Piece[] getPieces() {
        return pieces.toArray(new Piece[0]);
    }

    public int nombreDePieces() {
        return pieces.size();
    }

    public void ajouteEntree(Exterieur out, Piece e) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Méthode pour ajouter un corridor entre deux pièces existantes.
     *
     * @param e1 Première pièce.
     * @param e2 Deuxième pièce.
     */
    public void ajouteCorridor(Piece e1, Piece e2) {
        if (getPieceByID(e1.getID()) == null)
            pieces.add(e1);
        if (getPieceByID(e2.getID()) == null)
            pieces.add(e2);
        addEdge(e1, e2);
    }

    public void ajouteCorridor(int e1ID, int e2ID) throws PieceNotFoundException {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Méthode pour ajouter une nouvelle pièce au labyrinthe.
     *
     * @param e La pièce à ajouter.
     * @return L'index de la pièce ajoutée.
     */
    public int ajoutePiece(Piece e) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean existeCorridorEntre(Piece e1, Piece e2) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Méthode pour obtenir les pièces connectées à une pièce spécifique.
     *
     * @param e La pièce dont les pièces connectées sont recherchées.
     * @return Un tableau de pièces connectées à la pièce donnée.
     */
    public Piece[] getPiecesConnectees(Piece e) {
        throw new UnsupportedOperationException("TODO");
    }

    private void addEdge(Piece e1, Piece e2) {
        adjList.computeIfAbsent(e1.getID(), k -> new ArrayList<>()).add(e2.getID());
        adjList.computeIfAbsent(e2.getID(), k -> new ArrayList<>()).add(e1.getID());
    }

    private Piece getPieceByID(int ID) {
        for (Piece piece : pieces) {
            if (piece.getID() == ID) {
                return piece;
            }
        }
        return null;
    }
}
