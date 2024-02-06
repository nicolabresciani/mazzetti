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
            MazzettiServer server = new MazzettiServer();
            server.start();
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
                }

                playGame();
                serverSocket.close(); // Chiude la socket del server dopo il gioco
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void playGame() {
            System.out.println("Inizia una nuova partita!");
            deck.shuffle();

            // Richiesta di scommesse da parte dei giocatori
            for (Player player : players) {
                player.send("Benvenuto a Mazzetti! Inserisci il tuo nome:");
            }
            for (Player player : players) {
                String playerName = player.receive();
                player.setName(playerName);

                player.send("Inserisci la tua scommessa:");
                int bet = Integer.parseInt(player.receive());
                player.setBet(bet);
            }

            // Pesca una carta per ogni giocatore
            
            for (Player player : players) {
                int card = deck.drawCard();
                player.setLastCardValue(card);
                player.send("Hai pescato la carta: " + card);
            }

            
            // Determinazione del vincitore e distribuzione dei premi
            int highestCardValue = -1;
            Player winner = null;
            for (Player player : players) {
                int cardValue = player.getLastCardValue();
                if (cardValue > highestCardValue) {
                    highestCardValue = cardValue;
                    winner = player;
                }
            }

            if (winner != null) {
                // Calcola la somma totale delle puntate dei giocatori
                int totalBetAmount = getTotalBetAmount();

                // Invia il messaggio "Hai vinto" al vincitore
                winner.send("Hai vinto con la carta: " + highestCardValue + "  con un montepremi di: " + totalBetAmount);

                // Invia il messaggio "Hai perso" agli altri giocatori
                for (Player player : players) {
                    if (player != winner) {
                        player.send("Hai perso con la carta: " + player.getLastCardValue() + ". Il vincitore è stato: " + winner.getName() + " con la carta: " + winner.getLastCardValue());
                    }
                }

                // Aggiorna i soldi del vincitore
                winner.setMoney(winner.getMoney() + totalBetAmount);
            } else {
                // Invia un messaggio di pareggio se non c'è un vincitore
                for (Player player : players) {
                    player.send("Pareggio. La tua puntata: " + player.getBet());
                }
            }


            // Determinazione del vincitore globale e somma dei soldi vinti
            Player globalWinner = getWinner();
            if (globalWinner != null) {
                System.out.println("Il vincitore è: " + globalWinner.getName() + " con la carta: " + globalWinner.getLastCardValue() + " e la somma dei soldi vinti: " + globalWinner.getMoney());
            } else {
                System.out.println("Nessun vincitore.");
            }

            // Chiudere le connessioni dei giocatori
            for (Player player : players) {
                player.send("Fine del gioco.");
                player.close();
            }
        }



        public int getTotalBetAmount() {
            int totalBet = 0;
            for (Player player : players) {
                totalBet += player.getBet();
            }
            return totalBet;
        }

        public Player getWinner() {
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
