package dirogue.example.code_squelette;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket server;
    private Socket client;
    private BufferedReader reader;

    private List<EventHandler> handlers = new ArrayList<EventHandler>();

    public Server(int port) throws IOException {
        System.out.println("Demarrage du serveur sur port " + port);
        this.server = new ServerSocket(port, 1);
        System.out.println("En attente de connection du client.");
        this.client = this.server.accept();
        System.out.println("Connecté au client: " + this.client);
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    public void listen() throws IOException {
        String line;
        while ((line = this.reader.readLine()) != null) {
            String cmd = this.getCommandFromLine(line);
            String[] args = this.getArgumentsFromLine(line);
            if (cmd.equals("exit"))
                break;
            this.alertHandlers(cmd, args);
        }
        finalize();
    }

    private void alertHandlers(String cmd, String... args) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, args);
        }
    }

    private String getCommandFromLine(String line) {
        return line.split(" ")[0];
    }

    private String[] getArgumentsFromLine(String line) {
        String[] parts = line.split(" ");
        List<String> args = new ArrayList<String>();
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                args.add(parts[i]);
            }
        }

        return args.toArray(new String[0]);
    }

    @Override
    public void finalize() throws IOException {
        System.out.println("Finalisation du serveur.");
        this.reader.close();
        this.client.close();
        this.server.close();
    }

}
