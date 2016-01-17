package wargame;

public enum Value {
  Two (2, "Two"),
  Three (3, "Three"),
  Four (4, "Four"),
  Five (5, "Five"),
  Six (6, "Six"),
  Seven (7, "Seven"),
  Eight (8, "Eight"),
  Nine (9, "Nine"),
  Ten (10, "Ten"),
  Jack (11, "Jack"),
  Queen (12, "Queen"),
  King (13, "King"),
  Ace (14, "Ace");

  public final int value;
  public final String name;

  Value(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public String toString() {
    return name;
  }

}
