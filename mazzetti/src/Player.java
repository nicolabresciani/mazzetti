import java.io.*;
import java.net.*;

public class Player {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private int bet;
    private int lastCardValue;
    private int money; // Aggiunto campo money
    private boolean wantsToBetAgain;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void close() throws IOException {
        socket.close();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void setLastCardValue(int lastCardValue) {
        this.lastCardValue = lastCardValue;
    }

    public String getName() {
        return name;
    }

    public int getBet() {
        return bet;
    }

    public int getLastCardValue() {
        return lastCardValue;
    }

    public void setWantsToBetAgain(boolean wantsToBetAgain) {
        this.wantsToBetAgain = wantsToBetAgain;
    }

    public boolean wantsToBetAgain() {
        return wantsToBetAgain;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void send(String message) {
        out.println(message);
    }
}
