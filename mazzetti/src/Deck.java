import java.util.*;

class Deck {
    private List<Integer> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 4; j++) {
                cards.add(i);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return -1;
    }
}
