import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler implements Runnable {
    private Socket clientSocket;
    private List<Player> players;
    private int totalBet; // Aggiunto per tenere traccia della somma totale delle puntate

    public GameHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.players = new ArrayList<>();
        this.totalBet = 0;
    }

    @Override
    public void run() {
        try {
            initializePlayers();
            distributeCards();
            sendPlayerInfo();
        
            sendPlayerCards();
            determineWinner();
            sendGameResults();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePlayers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        writer.println("Inserisci il numero di giocatori:");

        int numPlayers;
        while (true) {
            try {
                String numPlayersInput = reader.readLine();
                numPlayers = Integer.parseInt(numPlayersInput);
                if (numPlayers > 0) {
                    break;
                } else {
                    writer.println("Inserisci un numero di giocatori valido (maggiore di 0):");
                }
            } catch (NumberFormatException e) {
                writer.println("Inserisci un numero di giocatori valido (maggiore di 0):");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            writer.println("Inserisci il nome del giocatore " + i + ":");
            String name = reader.readLine();

            writer.println("Inserisci la puntata per il giocatore " + i + ":");
            int bet;
            while (true) {
                try {
                    String betInput = reader.readLine();
                    bet = Integer.parseInt(betInput);
                    if (bet >= 0) {
                        break;
                    } else {
                        writer.println("Inserisci una puntata valida (non negativa):");
                    }
                } catch (NumberFormatException e) {
                    writer.println("Inserisci una puntata valida (non negativa):");
                }
            }

            players.add(new Player(name, bet));
        }
    }

    private void distributeCards() {
        List<Integer> cards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);

        for (Player player : players) {
            player.setCard(cards.remove(0));
        }
    }

    private void sendPlayerInfo() throws IOException {
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println("Informazioni Giocatori:");

        for (Player player : players) {
            writer.println(player.getName() + ": Puntata - " + player.getBet() + ", Carta - ");
        }
    }


    private void sendPlayerCards() throws IOException {
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println("Le carte dei giocatori sono:");

        for (Player player : players) {
            writer.println(player.getName() + ": Carta - " + player.getCard());
        }
    }

    private void determineWinner() {
        int maxCard = 0;
        Player winner = null;

        for (Player player : players) {
            if (player.getCard() > maxCard) {
                maxCard = player.getCard();
                winner = player;
            }
        }
    }

    private void sendGameResults() throws IOException {
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println("Partita Conclusa!");

        // Calcola la somma totale delle puntate
        for (Player player : players) {
            totalBet += player.getBet();
        }

        // Trova il vincitore
        int maxCard = 0;
        Player winner = null;

        for (Player player : players) {
            if (player.getCard() > maxCard) {
                maxCard = player.getCard();
                winner = player;
            }
        }

        // Invia il messaggio personalizzato del vincitore
        writer.println("Il vincitore è " + winner.getName() + " con un montepremi di " + totalBet + " € con la carta: " + winner.getCard());
    }

    private void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
