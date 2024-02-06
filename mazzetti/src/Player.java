import java.io.*;
import java.net.*;

class Player {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;
    private int bet;
    private int money;
    private int lastCardValue;

    public Player(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setLastCardValue(int lastCardValue) {
        this.lastCardValue = lastCardValue;
    }

    public int getLastCardValue() {
        return lastCardValue;
    }

    public int receiveCardValue() {
        try {
            String cardValue = in.readLine();
            int value = Integer.parseInt(cardValue);
            setLastCardValue(value);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
