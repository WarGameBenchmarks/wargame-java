package wargame;

public enum Suit {
  Hearts ("Hearts"),
  Spades ("Spades"),
  Clubs ("Clubs"),
  Diamonds ("Diamonds");

  public String name;

  Suit(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

}
