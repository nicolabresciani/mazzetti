import java.io.*;
import java.net.*;
import java.util.*;

public class MazzettiServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 4;
    private static final int MAX_CARDS = 10;

    private List<Player> players;
    private Deck deck;

    public static void main(String[] args) {
        new MazzettiServer().start();
    }

    public MazzettiServer() {
        players = new ArrayList<>();
        deck = new Deck();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server avviato. In attesa di connessioni...");

            while (players.size() < MAX_PLAYERS) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione: " + clientSocket);

                Player player = new Player(clientSocket);
                players.add(player);

                if (players.size() == MAX_PLAYERS) {
                    playGame();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame() {
        System.out.println("Inizia una nuova partita!");
        deck.shuffle();

        // Richiesta di scommesse da parte dei giocatori
        for (Player player : players) {
            player.send("Benvenuto a Mazzetti! Inserisci il tuo nome:");
            String playerName = player.receive();
            player.setName(playerName);

            player.send("Inserisci la tua scommessa:");
            int bet = Integer.parseInt(player.receive());
            player.setBet(bet);
        }

        // Distribuzione delle carte
        for (int i = 0; i < MAX_CARDS; i++) {
            for (Player player : players) {
                player.send("Nuova mano: " + deck.drawCard());
            }

            int highestCardValue = -1;
            Player winner = null;

            for (Player player : players) {
                int cardValue = player.receiveCardValue();
                if (cardValue > highestCardValue) {
                    highestCardValue = cardValue;
                    winner = player;
                }
            }

            if (winner != null) {
                winner.send("Hai vinto con la carta: " + highestCardValue + " e la tua puntata: " + winner.getBet());
                for (Player player : players) {
                    if (player != winner) {
                        player.send("Hai perso con la carta: " + highestCardValue + " e la tua puntata: " + player.getBet());
                    }
                }
                winner.setMoney(winner.getMoney() + getTotalBetAmount());
            }
        }

        // Determinazione del vincitore e somma dei soldi vinti
        Player winner = getWinner();
        if (winner != null) {
            System.out.println("Il vincitore è: " + winner.getName() + " con la carta: " + winner.getLastCardValue() + " e la somma dei soldi vinti: " + winner.getMoney());
        } else {
            System.out.println("Nessun vincitore.");
        }

        // Chiudere le connessioni dei giocatori
        for (Player player : players) {
            player.send("Fine del gioco.");
            player.close();
        }
    }

    private int getTotalBetAmount() {
        int totalBet = 0;
        for (Player player : players) {
            totalBet += player.getBet();
        }
        return totalBet;
    }

    private Player getWinner() {
        Player winner = null;
        int highestCardValue = -1;
        for (Player player : players) {
            if (player.getLastCardValue() > highestCardValue) {
                highestCardValue = player.getLastCardValue();
                winner = player;
            }
        }
        return winner;
    }
}
