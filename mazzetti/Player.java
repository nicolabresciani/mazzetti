public class Player {
    private String name;
    private int bet;
    private int card;

    public Player(String name, int bet) {
        this.name = name;
        this.bet = bet;
        this.card = 0;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public int getBet() {
        return bet;
    }
}