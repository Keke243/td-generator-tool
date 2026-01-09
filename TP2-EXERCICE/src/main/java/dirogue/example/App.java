package dirogue.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dirogue.example.controllers.ControllerBase;
import dirogue.example.view.ViewBase;

/**
 * Classe principale de l'application.
 * Étend la classe Application de JavaFX et initialise l'application graphique.
 */
public class App extends Application {
    private static Stage primaryStage;

    /**
     * Méthode de démarrage de l'application.
     * Initialise la fenêtre principale en affichant la vue "Main".
     * 
     * @param stage - La fenêtre principale de l'application.
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showView("Main");
    }

    /**
     * Affiche une vue spécifique avec des données.
     * 
     * @param viewName - Le nom de la vue à afficher.
     * @param data     - Les données à passer à la vue (peuvent être nulles).
     */
    public static void showView(String viewName, String data) {
        var view = ViewBase.getView(viewName);
        Scene scene = new Scene(view.getRoot(), 640, 480);

        ControllerBase.getController(view, data);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Surcharge de la méthode {@code showView(String viewName, String data)}
     * avec des données nulles par défaut.
     * 
     * @param viewName - Le nom de la vue à afficher.
     */
    public static void showView(String viewName) {
        showView(viewName, null);
    }

    public static void main(String[] args) {
        launch();
    }
}