
import java.util.*;


class WarGame {

    public WarGame() {

        NormalDeck n = new NormalDeck();
        n.shuffle();

        Deck[] player = new Deck[2];
        player = n.split();

        player[0].toString();

        player[1].toString();

        int turns = 0;

        basegame:
        while ((player[0].hasCards() && player[1].hasCards())) {
            turns++;
            //each loop here is a turn

            e("Turn #" + turns);


            //report each deck's size
            e("Player 1 has " + player[0].getSize() + " cards.");
            e("Player 2 has " + player[1].getSize() + " cards.");

            e("");
            e("");

            // Place all cards that can be won this round in this deck.
            Deck winner = new Deck();

            // gets the top card on both decks
            Card card1 = player[0].getCard();
            Card card2 = player[1].getCard();

            // sends those top cards to the winner's pile
            player[0].give(winner);
            player[1].give(winner);



            if (card1.getValue() == card2.getValue()) {

                e("--- A war has started! ---");
                int wars = 0;

                war:
                do {
                    e(card1.toString() + " is equal to " + card2.toString());

                    // this handles the case where the players don't have enough cards
                    if (player[0].getSize() < 4 || player[1].getSize() < 4) {
                        e("One of the players didn't have enough cards to play WAR, so the game ended.");
                        break basegame;
                    }
                    wars++;


                    // since there was no break, continue

                    // this gets the cards off the top, the first 3
                    player[0].giveMany(winner, 3);
                    player[1].giveMany(winner, 3);

                    card1 = player[0].getCard();
                    card2 = player[1].getCard();

                    // sends those top cards to the winner's pile
                    player[0].give(winner);
                    player[1].give(winner);

                    // Get the next set of cards for war-comparison
                    if (card1.getValue() > card2.getValue()) {
                        e("Player 1's card was higher, " + card1.toString() + ", than player 2's card, " + card2.toString());
                        e("Player 1 gets " + winner.getSize() + " cards from this war!");

                        // you're merging the winner deck with their own deck
                        winner.shuffle();
                        player[0].merge(winner);



                    } else if (card1.getValue() < card2.getValue()) {
                        e("Player 2's card was higher, " + card2.toString() + ", than player 1's card, " + card1.toString());
                        e("Player 2 gets " + winner.getSize() + " cards from this war!");

                        // merge the winner deck with the player deck
                        winner.shuffle();
                        player[1].merge(winner);

                    } else {
                        e("--- Another war! ---");
                    }


                } while (card1.getValue() == card2.getValue());


            } else if (card1.getValue() > card2.getValue()) {
                e("Player 1's card, " + card1.toString() + ", was higher than Player 2's, " + card2.toString());
                e("Player 1 gets the cards.");
                // the VALUE of PLAYER 1'S card is HIGHER
                // PLAYER 1 gets the card

                winner.shuffle();
                player[0].merge(winner);

            } else {
                e("Player 1's card, " + card1.toString() + ", was lower than Player 2's card, " + card2.toString());
                e("Player 2 gets the cards.");
                // the VALUE of PLAYER 2'S card is HIGHER
                // PLAYER 2 gets the card

                winner.shuffle();
                player[1].merge(winner);

            }

            e("");
            e("Turn ended.");
            e("");

        }

        e("The game is over.");
        e("The game took " + turns + " turns.");
        e("Player 1 has " + player[0].getSize() + " cards.");
        e("Player 2 has " + player[1].getSize() + " cards.");

    }

    public static void e(String e) {
        //System.out.println(e);
    }
}

class Deck {

    public static int decks = 0;
    private int id;
    public ArrayList<Card> cards;
    private int pointer;

    /**
     * Constructor.
     * 
     * Create a new ArrayList for the cards in this deck and generate an unique id for the deck.
     */
    public Deck() {
        this.setId();
        this.cards = new ArrayList<Card>();
    }

    /**
     * Generate an unique identifier for this deck object.
     *
     */
    private void setId() {
        Deck.decks++;
        this.id = Deck.decks;
    }

    /**
     * Add a Card object to this deck object at the end of the ArrayList or to the top of the deck.
     *
     * @param aCard A Card object that will be added to this deck.
     */
    public void addCard(Card aCard) {
        this.cards.add(aCard);
    }

    /**
     * Add multiple cards to the top of the deck using a loop.
     * 
     * @param aCards A Card object array that will added to the deck.
     * 
     */
    public void addCards(Card[] aCards) {
        for (Card card : aCards) {
            this.cards.add(card);
        }
    }

    /**
     * Shuffle this deck out of this decks's current order.
     * 
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Gets the size of this deck.
     * 
     * @return Returns a whole number based on the number of cards in this deck.
     */
    public int getSize() {
        return this.cards.size();
    }

    /**
     * Gets weather this deck has cards in it.
     * 
     * @return true if not zero and false if zero
     */
    public boolean hasCards() {
        if (this.cards.size() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Uses the internal pointer to get the card at the specified index.
     * 
     * @return Returns a card based on the internal pointer's value and the card index. 
     */
    public Card next() {
        if (this.pointer > this.getSize()) {
            this.pointer = 0;
        }
        return this.cards.get(this.pointer++);
    }

    /**
     * Gets a card from the top of the deck.
     * 
     * @return Returns the top card of the deck.
     */
    public Card getCard() {
        return this.cards.get(0);
    }

    public Card[] getCards(int to) {
        Card[] cs = new Card[to];
        for (int i = 0; i < to; i++) {
            cs[i] = this.cards.get(i);
        }
        return cs;
    }

    /**
     * Gives the supplied deck object the top card from this deck.
     * 
     * @param aDeck A deck objec that will recieve this deck's top card.
     */
    public void give(Deck aDeck) {
        Card c = this.getCard();
        this.cards.remove(0);
        aDeck.addCard(c);
    }

    /**
     * Gives the supplied deck the top card and subsequent cards from this deck.
     * 
     * @param aDeck A deck object what will recieve this deck's cards.
     * @param to The index of the ArrayList of this deck to get cards from.
     */
    public void giveMany(Deck aDeck, int to) {
        for (int i = 0; i < to; i++) {
            Card c = this.getCard();
            this.remove();
            aDeck.addCard(c);
        }
    }

    /**
     * Removes the top card of this deck.
     */
    public Card remove() {
        return this.cards.remove(0);
    }

    /**
     * Sends the top card of this deck to the bottom of the deck.
     * 
     */
    public void toEnd() {
        Card c = this.getCard();
        this.cards.remove(0);
        this.cards.add(c);
    }

    /**
     * Merges this and a supplied deck.
     * 
     * @param aDeck the deck that will be merged with this deck.
     */
    public void merge(Deck aDeck) {
        for (int i = 0; i < aDeck.getSize(); i++) {
            this.addCard(aDeck.cards.get(i));
        }
    }

    /**
     * Gets a string repsentation of the this deck.
     * 
     * @return Return a string repsentation of the current deck.
     */
    public String toString() {
        return Arrays.toString(this.cards.toArray());
    }
}

class NormalDeck extends Deck {

    public NormalDeck() {
        super();
        Suit[] suits = {new Suit("Hearts"), new Suit("Spades"), new Suit("Clubs"), new Suit("Diamonds")};
        for (int s = 0; s < suits.length; s++) {
            for (int c = 2; c <= 14; c++) {
                this.addCard(new Card(c, suits[s]));
            }
        }
    }

    public Deck[] split() {
        int size = this.cards.size();
        int half = size / 2;
        Deck[] newDecks = new Deck[2];
        newDecks[0] = new Deck();
        newDecks[1] = new Deck();
        for (int i = 0; i < size; i++) {
            if (i < half) {
                // i is smaller than half
                newDecks[0].addCard(this.cards.get(i));
            } else {
                // i is larger than half
                newDecks[1].addCard(this.cards.get(i));
            }
        }
        return newDecks;
    }
}

class Suit {

    public static int suits = 0;
    public static int colors = 0;
    private int id;
    private int colorValue;
    private String suitColor;
    private int value;
    private String name;

    /**
     * Regular Constructor.
     * 
     * @param name A name for this suit.
     */
    public Suit(String name) {
        this.setId();
        this.name = name;
        this.value = Suit.suits;
    }

    /**
     * Constructor. Make a Suit using a name and a value.
     * 
     * @param name A name for this suit.
     * @param value A value that can be used for equality.
     */
    public Suit(String name, int value) {
        this.setId();
        this.name = name;
        this.value = value;
    }

    /* The following two methods are subject to change.*/    // Deprecated
    public Suit(String name, int value, String colorName) {
        this.name = name;
        this.value = value;
        this.setId();
        this.suitColor = colorName;
    }
    // Deprecated
    public Suit(String name, int value, String colorName, int colorValue) {
        this.name = name;
        this.value = value;
        this.setId();
        this.colorValue = colorValue;
        this.suitColor = colorName;
    }

    /**
     * Sets the id of this suit.
     */
    private void setId() {
        Suit.suits++;
        this.id = Suit.suits;
    }

    /**
     * Gets the name of this suit.
     * 
     * @return Returns the name of this suit.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of this suit.
     * 
     * @return Returns the value of this suit.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Tests weather this suit
     * 
     * @param aSuit The suit that this suit will be compared to.
     */
    public boolean equals(Suit aSuit) {
        if (this.value == aSuit.value) {
            return true;
        } else {
            return false;
        }
    }
}

class Card {

    public static int cards = 0;
    private int id;
    private int value;
    private String name;
    private Suit suit;

    public Card(int value, Suit aSuit) {
        this.value = value;
        this.name = value + "";
        this.suit = aSuit;
        this.setId();
    }

    public Card(String name, int value, Suit aSuit) {
        this.value = value;
        this.name = name;
        this.suit = aSuit;
        this.setId();
    }

    private void setId() {
        Card.cards++;
        this.id = Suit.suits;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public String toString() {
        String form = (this.name.length() != 0 ? this.getName() : (this.getValue() + ""));
        String string = "The " + form + " of " + this.getSuit().getName();
        return string;
    }
}
