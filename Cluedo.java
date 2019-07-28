import java.util.*;

public class Cluedo {

    private Board board;
    private Triple envelope;

    private List<Token> weaponTokens;
    private List<CharacterToken> characterTokens;
    private List<Card> cards;
    private List<Player> players;

    public Cluedo() {
        restart();
    }


    public void restart() {
        board = new Board();
        board.loadFile();
        initialiseTokens();
        board.placeWeaponTokens(weaponTokens);

        initialisePlayers();
        distributeTokensToPlayers();
        board.placePlayerTokens(characterTokens);

        initialiseCards();
        setMurderCircumstances();
        dealCardsToPlayers();
    }

    public boolean gameOver() {
        for (Player p : players) {
            if (p.getState() == Player.State.PLAYING) return false;
        }

        // a player has won
        for (Player p : players) {
            if (p.getState() == Player.State.WON) return true;
        }

        // all players have lost
        return true;
    }


    public void dealCardsToPlayers() {
       Collections.shuffle(cards);

       for (int i = 0; i < cards.size(); i++) {
           Player p = players.get(i%players.size());
           p.addToHand(cards.get(i));
       }
    }

    public void initialiseCards() {
        cards = new ArrayList<>(Arrays.asList(
                // Weapons
                new Card(Description.CANDLESTICK),
                new Card(Description.DAGGER),
                new Card(Description.LEAD_PIPE),
                new Card(Description.REVOLVER),
                new Card(Description.ROPE),
                new Card(Description.SPANNER),
                // Characters
                new Card(Description.MISS_SCARLETT),
                new Card(Description.MRS_WHITE),
                new Card(Description.MR_GREEN),
                new Card(Description.MRS_PEACOCK),
                new Card(Description.PROFESSOR_PLUM),
                new Card(Description.COLONEL_MUSTARD),
                // Rooms
                new Card(Description.KITCHEN),
                new Card(Description.BALLROOM),
                new Card(Description.CONSERVATORY),
                new Card(Description.DINING_ROOM),
                new Card(Description.BILLIARD_ROOM),
                new Card(Description.LIBRARY),
                new Card(Description.LOUNGE),
                new Card(Description.HALL),
                new Card(Description.STUDY)
        ));
    }

    public void setMurderCircumstances() {
        Random random = new Random();
        Card weapon, room, character;

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.description.isAWeapon()) {
                cards.remove(c);
                weapon = c;
                break;
            }
        }

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.description.isARoom()) {
                cards.remove(c);
                room = c;
                break;
            }
        }

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.description.isACharacter()) {
                cards.remove(c);
                character = c;
                break;
            }
        }
        envelope = new Triple(weapon, room, character);
    }

    /**
     * Initialise the 'players' list with 3-6 potential players.
     */
    public void initialisePlayers() {
        Scanner sc = new Scanner(System.in);
        int numPlayers;
        System.out.println("num players 3-6? ");
        while (true) {
            if (!sc.hasNextInt()) {
                System.out.println("bad input ");
                sc.nextLine();
            } else {
                numPlayers = sc.nextInt();
                sc.nextLine();
                if (numPlayers >= 3 && numPlayers <= 6) {
                    break;
                } else {
                    System.out.println("bad input ");
                }
            }
        }

        players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(board));
        }
    }


    public void distributeTokensToPlayers() {
        Scanner sc = new Scanner(System.in);
        List<CharacterToken> tokens = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + (char)('A' + i) + " choose a token:\n");
            for (int j = 0; j < characterTokens.size(); j++) {
                System.out.println((j + 1) + ". " + characterTokens.get(j).description);
            }

            while (true) {
                if (!sc.hasNextInt()) {
                    System.out.println("bad input ");
                    sc.nextLine();
                }
                else {
                    int k = sc.nextInt();
                    sc.nextLine();
                    if (k > 0 && k <= characterTokens.size()) {
                        CharacterToken token = characterTokens.get(k - 1);
                        players.get(i).setCharacterToken(token);
                        System.out.println("player " + (char)('A' + i) + " is now playing as " + token.description);
                        tokens.add(token);
                        characterTokens.remove(token);
                        break;
                    } else {
                        System.out.println("bad input ");
                    }
                }
            }
        }
        characterTokens = tokens;
    }


    /**
     * Initialise the Tokens' Lists with corresponding tokens.
     *  - Tokens which have type weapon are placed in the weaponTokens list.
     *  - Tokens which have type character are placed in the characterTokens list.
     * Note that weaponTokens are not initialised with a (row, col) entry, since
     * they are arbitrarily put into a starting room each new game.
     */
    public void initialiseTokens() {
        weaponTokens = new ArrayList<>(Arrays.asList(
                new Token(Description.CANDLESTICK),
                new Token(Description.DAGGER),
                new Token(Description.LEAD_PIPE),
                new Token(Description.REVOLVER),
                new Token(Description.ROPE),
                new Token(Description.SPANNER)
        ));
        // Note that each CharacterToken has an unique starting area on the Board.
        characterTokens = new ArrayList<>(Arrays.asList(
                new CharacterToken(Description.MISS_SCARLETT, 24, 7),
                new CharacterToken(Description.COLONEL_MUSTARD, 17, 0),
                new CharacterToken(Description.MRS_WHITE, 0, 9),
                new CharacterToken(Description.MR_GREEN, 0, 14),
                new CharacterToken(Description.MRS_PEACOCK, 6, 23),
                new CharacterToken(Description.PROFESSOR_PLUM, 19,23)
        ));
    }

    public void runGame() {
        int currentPlayer = 0;
        while (!gameOver()) {
            Player p = players.get(currentPlayer%players.size());
            p.takeTurn();
            currentPlayer++;
        }
    }

    public static void main(String[] args) {
        Cluedo cluedo = new Cluedo();
        cluedo.runGame();
    }
}
