import java.io.*;
import java.net.*;

public class MazzettiClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connessione al server riuscita.");

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server: " + serverMessage);

                if (serverMessage.equals("Fine del gioco.") || serverMessage.startsWith("Hai vinto") || serverMessage.startsWith("Hai perso")) {
                    break;
                }

                if (serverMessage.startsWith("Benvenuto") || serverMessage.startsWith("Inserisci")) {
                    String userInput = consoleInput.readLine();
                    out.println(userInput);
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
