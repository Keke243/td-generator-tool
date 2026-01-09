package dirogue.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;
import dirogue.example.code_squelette.*;

/**
 * Classe représentant une aventure spécifique implémentant les fonctionnalités
 * définies dans l'interface Aventure.
 * Cette classe contient des méthodes pour évaluer l'état de l'aventure, y
 * compris
 * la présence de monstres, de trésors, la pacificité, ainsi que des méthodes
 * pour
 * obtenir le chemin jusqu'au boss et sauvegarder un rapport d'aventure.
 */
public class MonAventure extends Aventure {

    /**
     * Constructeur pour initialiser une nouvelle aventure basée sur un labyrinthe donné.
     *
     * @param c Le labyrinthe sur lequel l'aventure est basée.
     */
    public MonAventure(Labyrinthe c) {
        super(c);
    }

    public boolean estPacifique() {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean contientDuTresor() {
        throw new UnsupportedOperationException("TODO");
    }

    public int getTresorTotal() {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean contientBoss() {
        throw new UnsupportedOperationException("TODO");
    }

    public Piece[] cheminJusquAuBoss() {
        if (!contientBoss())
            return new Piece[0];
        throw new UnsupportedOperationException("TODO");
    }

    private Piece[] findBoss(Piece current, Piece[] pathUntilCurrent) {
        Piece nextToCheck = Stream.of(carte.getPiecesConnectees(current)).filter((p) -> p.getID() > current.getID()).sorted(Comparator.comparing(Piece::getID)).findFirst().get();
        Piece[] newPath = Arrays.copyOf(pathUntilCurrent, pathUntilCurrent.length + 1);
        newPath[pathUntilCurrent.length] = nextToCheck;
        if (nextToCheck.getRencontre().essence() == RencontreType.BOSS) {
            return newPath;
        } else {
            return findBoss(nextToCheck, newPath);
        }
    }

    /**
     * Méthode permettant de sauvegarder un rapport détaillé de l'aventure dans un fichier spécifié.
     *
     * @param filePath Le chemin du fichier où le rapport sera sauvegardé.
     * @throws IOException En cas d'erreur lors de l'écriture du fichier.
     */
    public void sauvegarderRapport(String filePath) throws IOException {
        throw new UnsupportedOperationException("TODO");
    }
}
