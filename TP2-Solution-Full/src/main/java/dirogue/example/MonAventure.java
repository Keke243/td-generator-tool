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
        for (Piece piece : carte.getPieces()) {
            if (piece.getRencontre().essence() == RencontreType.MONSTRE
                    || piece.getRencontre().essence() == RencontreType.BOSS)
                return false;
        }
        return true;
    }

    public boolean contientDuTresor() {
        for (Piece piece : carte.getPieces()) {
            if (piece.getRencontre().essence() == RencontreType.TRESOR)
                return true;
        }
        return false;
    }

    public int getTresorTotal() {
        int count = 0;
        for (Piece piece : carte.getPieces()) {
            if (piece.getRencontre().essence() == RencontreType.TRESOR)
                count++;
        }
        return count;
    }

    public boolean contientBoss() {
        for (Piece piece : carte.getPieces()) {
            if (piece.getRencontre().essence() == RencontreType.BOSS)
                return true;
        }
        return false;
    }

    public Piece[] cheminJusquAuBoss() {
        if (!contientBoss())
            return new Piece[0];

        Piece ext = (Piece) Stream.of(carte.getPieces()).filter(p -> p.getID() == 0).findFirst().get();
        return findBoss(ext, new Piece[] { ext });
    }

    private Piece[] findBoss(Piece current, Piece[] pathUntilCurrent) {
        Piece nextToCheck = Stream.of(carte.getPiecesConnectees(current))
                .filter((p) -> p.getID() > current.getID())
                .sorted(Comparator.comparing(Piece::getID))
                .findFirst().get();

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
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println("Rapport:");
            writer.println("Donjon avec " + carte.nombreDePieces() + " pieces :");

            for (Piece piece : carte.getPieces()) {
                StringBuilder connectedPieces = new StringBuilder();

                Piece[] connected = carte.getPiecesConnectees(piece);
                for (Piece connectedPiece : connected) {
                    connectedPieces.append("<").append(connectedPiece.getID()).append("-")
                            .append(connectedPiece.getRencontre().getClass().getSimpleName()).append(">, ");
                }

                if (connectedPieces.length() > 0) {
                    connectedPieces.setLength(connectedPieces.length() - 2);
                }

                writer.println("<" + piece.getID() + "-" + piece.getRencontre().getClass().getSimpleName() + "> : ["
                        + connectedPieces.toString() + "]");
            }

            if (estPacifique()) {
                writer.println("Pacifique.");
            } else {
                writer.println("Non pacifique.");
            }

            if (contientBoss()) {
                writer.println("Contient un boss.");
            }

            int tresorTotal = getTresorTotal();
            if (tresorTotal > 0) {
                writer.println("Contient " + tresorTotal + " tresor" + (tresorTotal > 1 ? "s" : "") + ".");
            }

            Piece[] chemin = cheminJusquAuBoss();
            if (chemin.length > 0) {
                writer.println("Chemin jusqu'au boss :");
                for (Piece piece : chemin) {
                    writer.println("<" + piece.getID() + "-" + piece.getRencontre().getClass().getSimpleName() + ">");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
