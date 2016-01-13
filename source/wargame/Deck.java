package wargame;

import java.util.*;

class Deck {

  private ArrayList<Card> cards;

  Deck(ArrayList<Card> cards) {
    this.cards = cards;
  }

  Deck() {
    cards = new ArrayList<Card>();
  }

  public void shuffle() {
    Collections.shuffle(this.cards);
  }

  public int getSize() {
    return this.cards.size();
  }

  public boolean hasCards() {
    return getSize() > 0;
  }

  public Card getCard() {
    return this.cards.get(0);
  }

  public void addCard(Card card) {
    this.cards.add(card);
  }

  public void giveCard(Deck other) {
    Card c = this.getCard();
    this.cards.remove(0);
    other.addCard(c);
  }

  public void giveCards(Deck other) {
    int size = getSize();
    for (int i = 0; i < size; i++) {
      giveCard(other);
    }
  }

  public Deck[] split() {
    int size = getSize();
    int half = size / 2;
    Deck[] decks = new Deck[2];
    decks[0] = new Deck();
    decks[1] = new Deck();
    for (int i = 0; i < size; i++) {
      if (i < half) {
        decks[0].addCard(this.cards.get(i));
      } else {
        decks[1].addCard(this.cards.get(i));
      }
    }
    return decks;
  }

  public static Deck fresh() {
    Deck deck = new Deck();
    Suit[] suits = new Suit[] {Suit.Hearts, Suit.Spades, Suit.Diamonds, Suit.Clubs};
    Value[] values = new Value[]
      {Value.Two, Value.Three, Value.Four,
      Value.Five, Value.Six, Value.Seven,
      Value.Eight, Value.Nine, Value.Ten,
      Value.Jack, Value.Queen, Value.King, Value.Ace};
    for (Suit s: suits) {
      for (Value v: values) {
        deck.addCard(new Card(v, s));
      }
    }
    return deck;
  }

}
