package wargame;

class WarGame {

  public void play() {

    Deck deck = Deck.fresh();
    deck.shuffle();

    Deck[] player = deck.split();

    Deck winner = new Deck();

    base:
    while (player[0].hasCards() && player[1].hasCards()) {


      Card c1 = player[0].getCard();
      Card c2 = player[1].getCard();

      player[0].giveCard(winner);
      player[1].giveCard(winner);

      if (c1.compareTo(c2) == 0) {

        war: do {
          if (player[0].getSize() < 4 || player[1].getSize() < 4) {
            break base;
          }

          for (int i = 0; i < 3; i++) {
            player[0].giveCard(winner);
            player[1].giveCard(winner);
          }

          c1 = player[0].getCard();
          c2 = player[1].getCard();

          player[0].giveCard(winner);
          player[1].giveCard(winner);

          if (c1.compareTo(c2) > 0) {
            winner.shuffle();
            winner.giveCards(player[0]);
          } else if (c1.compareTo(c2) < 0) {
            winner.shuffle();
            winner.giveCards(player[1]);
          } else {
            // another war
          }

        } while (c1.compareTo(c2) == 0);

      } else if (c1.compareTo(c2) > 0) {
        winner.shuffle();
        winner.giveCards(player[0]);
      } else if (c1.compareTo(c2) < 0) {
        winner.shuffle();
        winner.giveCards(player[1]);
      }

    }

  }

}
