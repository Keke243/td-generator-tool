package dirogue.example.controllers;

import dirogue.example.App;
import dirogue.example.view.MainView;
import dirogue.example.view.ViewBase;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Contrôleur principal pour la vue principale de l'application.
 * Ce contrôleur gère les interactions et les actions liées à la vue principale
 * de l'application.
 */
public class MainController extends ControllerBase {

    /**
     * Vue spécifique utilisée pour la vue principale.
     */
    private MainView mainView;

    /**
     * Constructeur pour le contrôleur principal.
     *
     * @param view La vue associée à ce contrôleur.
     */
    public MainController(ViewBase view) {
        super(view, null);
    }

    /**
     * Méthode d'initialisation du contrôleur principal.
     * Cette méthode configure les actions des boutons de la vue principale.
     */
    @Override
    protected void initialize() {
        this.mainView = (MainView) super.view;
        Button loadButton = mainView.getLoadButton();
        Button replayButton = mainView.getReplayButton();
        loadButton.setOnAction(event -> loadTextFile());
        replayButton.setOnAction(e -> {
            App.showView("Replay", mainView.getTextArea().getText());
        });
    }

    /**
     * Méthode privée pour charger un fichier texte dans la vue principale.
     * Cette méthode est déclenchée lorsqu'un utilisateur appuie sur le bouton de
     * chargement.
     */
    private void loadTextFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load report");
        throw new UnsupportedOperationException("TODO");
    }
}
