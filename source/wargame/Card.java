package wargame;


public class Card implements Comparable<Card> {

  private Value value;
  private Suit suit;

  Card(Value value, Suit suit) {
    this.value = value;
    this.suit = suit;
  }

  public Value getValue() {
    return value;
  }

  public Suit getSuit() {
    return suit;
  }

  public int compareTo(Card other) {
    int a = value.value;
    int b = other.getValue().value;
    if (a < b) {
      return -1;
    } else if (a > b) {
      return 1;
    }
    return 0;
  }

  public String toString() {
    return value + " of " + suit;
  }

}
