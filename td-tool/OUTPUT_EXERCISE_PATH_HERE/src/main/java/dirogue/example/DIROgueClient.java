package dirogue.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe représentant un client pour l'application DIROgue.
 * Ce client se connecte à un serveur spécifique et peut envoyer des commandes
 * pour charger, sauvegarder des fichiers ou quitter l'application.
 */
public class DIROgueClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 1370;

        Socket socket = null;
        PrintWriter out = null;
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.println("Enter a command (load, save, or exit):");
            input = scanner.nextLine().trim();

            if (input.equals("load")) {
                System.out.println("Enter the path to the file you want to load:");
                var filePath = scanner.nextLine().trim();

                try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (input.equals("save")) {
                System.out.println("Enter the path where you want to save the report:");
                var reportPath = scanner.nextLine().trim();
                out.println(input + " " + reportPath);

            } else if (input.equals("exit")) {
                out.println(input);
                break;
            } else {
                System.out.println("Invalid command. Please enter 'load', 'save', or 'exit'.");
            }
        }

        System.out.println("Exiting the program.");
        scanner.close();
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
