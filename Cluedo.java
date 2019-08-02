import java.util.*;

public class Cluedo {

    private Board board;
    private Triple envelope;
    private List<Player> players;

    public Cluedo () {
        String upperBorder = "╔════════════════════════════════════════════════════════════════════════════════╗";
        String header = "  ▒█░░▒█ █▀▀ █░░ █▀▀ █▀▀█ █▀▄▀█ █▀▀ 　 ▀▀█▀▀ █▀▀█ 　 ▒█▀▀█ █░░ █░░█ █▀▀ █▀▀▄ █▀▀█ \n" +
                        "  ▒█▒█▒█ █▀▀ █░░ █░░ █░░█ █░▀░█ █▀▀ 　 ░▒█░░ █░░█ 　 ▒█░░░ █░░ █░░█ █▀▀ █░░█ █░░█ \n" +
                        "  ▒█▄▀▄█ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀▀ ▀░░░▀ ▀▀▀ 　 ░▒█░░ ▀▀▀▀ 　 ▒█▄▄█ ▀▀▀ ░▀▀▀ ▀▀▀ ▀▀▀░ ▀▀▀▀ ";
        String lowerBorder = "╚════════════════════════════════════════════════════════════════════════════════╝";

        String instructions =
                "╔════════════╗\n" +
                "INTRODUCTIONS:\n" +
                "╚════════════╝\n" +
                    "Welcome to the game of Cluedo!\n" +
                    "This is a murder mystery game played by 3-6 players who move around a board comprising of\n" +
                    "nine rooms.\n" +
                    "A serious and terrible murder has occurred by one of six potential characters, and it is\n" +
                    "up to the players to find out WHO committed the murder and their circumstances.\n" +
                    "A murder circumstance involves who committed the murderer, their weapon of choice\n" +
                    "and which room they committed the murder.\n" +
                    "The WINNER of the game is the first player who successfully guesses all 3 murder circumstances!\n";

        String characterTokens =
                "╔═══════════════╗\n" +
                "CHARACTER TOKENS:\n" +
                "╚═══════════════╝\n" +
                        "There are 6 character tokens placed on the board which each player assumes\n" +
                        "The characters and their in game ascii representation are numbers:\n" +
                        "" +
                        "- Miss Scarlett (5)\n" +
                        "- Colonel Mustard (6)\n" +
                        "- Mrs. White (1)\n" +
                        "- Mr. Green (2)\n" +
                        "- Mrs. Peacock (3)\n" +
                        "- Professor Plum (4)\n";

        String weaponTokens =
                "╔════════════╗\n" +
                "WEAPON TOKENS:\n" +
                "╚════════════╝\n" +
                        "There are 6 weapon tokens on the board, one of which is the murder weapon:\n" +
                        "The weapon and their in game ascii representation are symbols:\n" +
                        "- Candlestick (?)\n" +
                        "- Dagger (>)\n" +
                        "- Lead Pipe (&)\n" +
                        "- Revolver (*)\n" +
                        "- Rope (=)\n" +
                        "- Spanner ($)\n" +
                        "At the start of the game, each weapon is randomly placed in a room such that no two\n" +
                        "weapons start in the same room.\n";

        String rules =
                "╔════╗\n" +
                "RULES:\n" +
                "╚════╝\n" +
                        "Each player has cards which represent a room, weapon, or character. At the start of the game, a\n" +
                        "character, weapon, and room card are randomly selected to represent the murder circumstances\n" +
                        "Players then need to figure out the murder circumstances during game play. These cards are \n" +
                        "secret, so are placed in an \"envelope\" hidden from the players\n" +
                        "\n" +
                        "The remaining weapon, room and character cards are combined, shuffled and \n" +
                        "dealt secretly to players. Note that Some players may end up with more cards than others.\n" +
                        "\n" +
                        "Players take turns to roll two dice and move their character token the sum of the dice values\n" +
                        "on the board. Diagonal movement is prohibited and no square may be visited\n" +
                        "again during the same turn. When a player enters a room, they have the option of using their remaining moves.\n" +
                        "After moving, players can then make suggestion which comprises the room they are in, a character and a weapon.\n" +
                        "Players can also make an accusation which they attempt to guess the murder circumstances, or end their turn. \n" +
                        "If the character and weapon named in the suggestion are not in that room, they are now moved into the room.\n" +
                        "\n" +
                        "After a suggestion, each player starting from the current player going clockwise,\n" +
                        "attempts to refute the suggestion by secretly showing a card in their hand matching a card in the suggestion.\n" +
                        "If a player has multiple refutation cards, it is their choice which one they pick.\n" +
                        "\n" +
                        "An accusation represents a character, a weapon, and a room card. If the accusation exactly matches\n" +
                        "the murder circumstances, the player wins.\n" +
                        "Otherwise, the player can no longer make anymore suggestions or accusations, but can still refute.\n";

        System.out.println(upperBorder);
        System.out.println(header);
        System.out.println(lowerBorder);
        System.out.println(instructions);
        System.out.println(characterTokens);
        System.out.println(weaponTokens);
        System.out.println(rules);
    }

    public void restart() {
        List <WeaponToken> weaponTokens = initialiseWeaponTokens();
        List <CharacterToken> characterTokens = initialisePlayerTokens();
        List <Card> cards = initialiseCards();
        board = new Board();
        initialisePlayers();
        characterTokens = distributeTokensToPlayers(characterTokens);
        board.setup(weaponTokens, characterTokens);
        setMurderCircumstances(cards);
        dealCardsToPlayers(cards);


        System.out.println(
                "╔═════════════════════════════════════════════════════════════════════════════════╗\n" +
                "║ Note that the order of player turns are randomised each game to ensure fairness.║\n" +
                "║ In this game, the player order is:                                              ║\n" +
                "╚═════════════════════════════════════════════════════════════════════════════════╝");
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ". " + players.get(i).getCharacterToken().getDescription());
        }
        System.out.println("\nLet's begin!\n");

    }


    public boolean gameOver() {
        // a player has won
        for (Player p : players) {
            if (p.getState() == Player.State.WON) {
                System.out.println(
                        "╔═════╗\n" +
                        "WINNER!\n" +
                        "╚═════╝\n" +
                                p.getCharacterToken().getDescription() + " has found the murder circumstances! Well done!\n");

                return true;
            }
        }

        // at least one player is still playing
        for (Player p : players) {
            if (p.getState() == Player.State.PLAYING) return false;
        }

        // everyone has lost
        System.out.println(
                "Oh no!\n" +
                "╔═══════════════════════════════╗\n" +
                "EVERYONE HAS GUESSED INCORRECTLY!\n" +
                "╚═══════════════════════════════╝\n");
        return true;
    }


    public void dealCardsToPlayers(List<Card> cards) {
        Collections.shuffle(cards);

        for (int i = 0; i < cards.size(); i++) {
            Player p = players.get(i%players.size());
            p.addToHand(cards.get(i));
        }
    }

    public List<Card> initialiseCards() {
        return new ArrayList<>(Arrays.asList(
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

    public void setMurderCircumstances(List<Card> cards) {
        Card weapon, room, character;

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.getDescription().isAWeapon()) {
                cards.remove(c);
                weapon = c;
                break;
            }
        }

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.getDescription().isARoom()) {
                cards.remove(c);
                room = c;
                break;
            }
        }

        while (true) {
            Card c = cards.get((int)(Math.random()*cards.size()));
            if (c.getDescription().isACharacter()) {
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
        System.out.println(
                "╔══════════════════════╗\n" +
                "Number of players (3-6)?\n" +
                "╚══════════════════════╝\n");
        while (true) {
            if (!sc.hasNextInt()) {
                System.out.println("That's not a number!");
                sc.nextLine();
            } else {
                numPlayers = sc.nextInt();
                sc.nextLine();
                if (numPlayers >= 3 && numPlayers <= 6) {
                    break;
                } else {
                    System.out.println("I need a number between 3 and 6!");
                }
            }
        }

        players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(board));
        }
    }

    public List<CharacterToken> distributeTokensToPlayers(List<CharacterToken> characterTokens) {
        Scanner sc = new Scanner(System.in);
        List<CharacterToken> tokens = new ArrayList<>();

        System.out.println(
                "╔═══════════════════════╗\n" +
                "Players pick your tokens!\n" +
                "╚═══════════════════════╝\n");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + (char)('A' + i) + " choose your token (1-" +characterTokens.size() + "):");
            for (int j = 0; j < characterTokens.size(); j++) {
                System.out.println((j + 1) + ". " + characterTokens.get(j).description);
            }

            while (true) {
                if (!sc.hasNextInt()) {
                    System.out.println("That's not a number!");
                    sc.nextLine();
                }
                else {
                    int j = sc.nextInt();
                    sc.nextLine();
                    if (j > 0 && j <= characterTokens.size()) {
                        CharacterToken token = characterTokens.get(j - 1);
                        players.get(i).setCharacterToken(token);
                        tokens.add(token);
                        characterTokens.remove(token);
                        break;
                    } else {
                        System.out.println("I need a number between 1 and " + characterTokens.size() + "!");
                    }
                }
            }
        }
        return tokens;
    }


    /**
     *
     */
    public List<CharacterToken> initialisePlayerTokens() {
        // Note that each CharacterToken has an unique starting area on the Board.
        return new ArrayList<>(Arrays.asList(
                new CharacterToken(Description.MISS_SCARLETT, 24, 7),
                new CharacterToken(Description.COLONEL_MUSTARD, 17, 0),
                new CharacterToken(Description.MRS_WHITE, 0, 9),
                new CharacterToken(Description.MR_GREEN, 0, 14),
                new CharacterToken(Description.MRS_PEACOCK, 6, 23),
                new CharacterToken(Description.PROFESSOR_PLUM, 19,23)
        ));
    }

    public List<WeaponToken> initialiseWeaponTokens() {
        return new ArrayList<>(Arrays.asList(
                new WeaponToken(Description.CANDLESTICK),
                new WeaponToken(Description.DAGGER),
                new WeaponToken(Description.LEAD_PIPE),
                new WeaponToken(Description.REVOLVER),
                new WeaponToken(Description.ROPE),
                new WeaponToken(Description.SPANNER)
        ));
    }

    public void runGame() {
        int index = 0;
        while (!gameOver()) {
            Player p = players.get(index%players.size());
            Description character = p.getCharacterToken().getDescription();

            if (p.getState() == Player.State.PLAYING) {

                switch (p.startTurn()){
                    case END_TURN:
                        break;
                    case ACCUSATION:

                        System.out.println(
                                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                                character + " has decided to make an accusation!\n" +
                                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

                        Triple accusation = p.makeAccusation();
                        if (accusation.equals(envelope)) {
                            p.setState(Player.State.WON);
                        } else {
                            p.setState(Player.State.LOST);
                            System.out.println(
                                    "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                                    "Oops! Looks like " + character + "'s accusation was wrong!\n" +
                                    character + " can no longer win the game. RIP.\n" +
                                    "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                        }
                        break;
                    case SUGGESTION:
                        Triple suggestion = p.makeSuggestion();
                        board.reallocateTokens(suggestion);

                        int nextIndex = (index+1)%players.size();
                        for (int i = 0; i < players.size()-1; i++) {
                            Player refutingPlayer = players.get(nextIndex);
                            Card refutation = refutingPlayer.refute(suggestion);

                            if (refutation != null) {
                                System.out.println(refutation);
                                break;
                            }
                            else nextIndex = (nextIndex+1)%players.size();
                        }
                        break;
                }
            } else {
                System.out.println(
                        "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                        character + " has already lost! Skipping turn...\n" +
                        "+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
            index++;
        }
    }

    public static boolean queryRestart() {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "╔═══════════════╗\n" +
                "Start a new game?\n" +
                "╚═══════════════╝\n");
        while (true) {
            if (sc.hasNext()) {
                String ans = sc.next();
                sc.nextLine();
                if (ans.equalsIgnoreCase("yes")) {
                    return true;
                }

                else if (ans.equalsIgnoreCase("no")) {
                    return false;
                }
                else {
                    System.out.println("I need a \"yes\" or \"no\"!");
                }
            }
        }
    }

    public static void main(String[] args) {

        Cluedo cluedo = new Cluedo();
        while (queryRestart()) {
            cluedo.restart();
            cluedo.runGame();
        }
    }
}