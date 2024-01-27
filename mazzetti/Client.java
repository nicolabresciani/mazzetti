import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(reader.readLine()); // Messaggio di benvenuto

            int numPlayers;
            while (true) {
                try {
                    System.out.print("Inserisci il numero di giocatori: ");
                    String numPlayersInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    numPlayers = Integer.parseInt(numPlayersInput);
                    if (numPlayers > 0) {
                        break;
                    } else {
                        System.out.println("Inserisci un numero di giocatori valido (maggiore di 0):");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Inserisci un numero di giocatori valido (maggiore di 0):");
                }
            }

            writer.println(numPlayers);

            for (int i = 1; i <= numPlayers; i++) {
                System.out.println(reader.readLine()); // Prompt per il nome del giocatore
                System.out.print("Inserisci il nome del giocatore " + i + ": ");
                writer.println(new BufferedReader(new InputStreamReader(System.in)).readLine());

                System.out.println(reader.readLine()); // Prompt per la puntata del giocatore
                int bet;
                while (true) {
                    try {
                        System.out.print("Inserisci la puntata per il giocatore " + i + ": ");
                        String betInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        bet = Integer.parseInt(betInput);
                        if (bet >= 0) {
                            break;
                        } else {
                            System.out.println("Inserisci una puntata valida (non negativa):");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Inserisci una puntata valida (non negativa):");
                    }
                }
                writer.println(bet);
            }

            System.out.println("In attesa degli altri giocatori per iniziare...");

            String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
            writer.println(input);

            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
