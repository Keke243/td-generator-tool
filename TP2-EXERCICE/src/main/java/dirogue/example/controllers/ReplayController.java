package dirogue.example.controllers;

import dirogue.example.view.ReplayView;
import dirogue.example.view.ViewBase;
import dirogue.example.rencontre.Rencontre;
import dirogue.example.App;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contrôleur pour la fonctionnalité de relecture des rencontres (Replay) dans
 * l'application.
 * Ce contrôleur gère l'affichage des messages et des images des rencontres dans
 * la vue de relecture.
 */
public class ReplayController extends ControllerBase {

    /**
     * Vue spécifique utilisée pour la fonctionnalité de relecture.
     */
    private ReplayView replayView;

    /**
     * Tableau des rencontres à rejouer.
     */
    private Rencontre[] encounters;

    /**
     * Index pour suivre la ligne actuelle des rencontres.
     */
    private int currentIndex = 0;

    /**
     * Constructeur pour le contrôleur de relecture.
     *
     * @param view La vue associée à ce contrôleur.
     * @param data Les données (report) associées au contrôleur.
     */
    public ReplayController(ViewBase view, String data) {
        super(view, data);
    }

    /**
     * Méthode d'initialisation du contrôleur de relecture.
     * Cette méthode configure les actions des boutons de navigation et initialise
     * les rencontres à partir des données.
     */
    @Override
    protected void initialize() {
        this.replayView = (ReplayView) super.view;
        initializeEncounters(super.data);
        showCurrentMessageAndImage();
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Initialise les rencontres à partir du rapport (report) fourni.
     *
     * @param report Le rapport contenant les informations sur les rencontres.
     */
    public void initializeEncounters(String report) {
        Pattern pattern = Pattern.compile("<\\d+-(\\w+)> :");
        Matcher matcher = pattern.matcher(report);
        var list = new ArrayList<Rencontre>();
        while (matcher.find()) {
            String className = matcher.group(1);
            try {
                Class<?> clazz = Class.forName("dirogue.example.rencontre." + className);
                Rencontre instance = (Rencontre) clazz.getDeclaredConstructor().newInstance();
                list.add(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Affiche le message et l'image de la rencontre en cours.
     */
    private void showCurrentMessageAndImage() {
        if (encounters != null && currentIndex >= 0 && currentIndex < encounters.length) {
            var encounter = encounters[currentIndex];
            Label messageLabel = replayView.getMessageLabel();
            messageLabel.setText(encounter.rencontre());
            File file = new File(encounter.getSprite());
            Image image = new Image(file.toURI().toString());
            ImageView imageView = replayView.getImageView();
            imageView.setImage(image);
        }
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Méthode pour naviguer vers la rencontre précédente dans le replay.
     */
    private void goBackward() {
        currentIndex = Math.max(0, currentIndex - 1);
        showCurrentMessageAndImage();
    }

    /**
     * Méthode pour naviguer vers la rencontre suivante dans le replay.
     */
    private void goForward() {
        currentIndex = Math.min(encounters.length - 1, currentIndex + 1);
        showCurrentMessageAndImage();
    }

    /**
     * Méthode pour quitter la fonctionnalité de relecture et revenir à la vue
     * principale.
     */
    private void exit() {
        App.showView("main");
    }
}
