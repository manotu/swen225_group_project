import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

    private CharacterToken characterToken;
    private List<Card> hand;
    private State state;
    private Board board;

    public enum State {
        PLAYING, LOST, WON
    }

    public enum TurnType{
        END_TURN, SUGGESTION, ACCUSATION
    }

    public Player(Board b) {
        assert b != null;
        hand = new ArrayList<>();
        state = State.PLAYING;
        board = b;
    }

    public void setCharacterToken(CharacterToken c) {
        assert c != null;
        characterToken = c;
    }

    public void addToHand(Card card) {
        assert card != null;
        hand.add(card);
    }

    public int rollDice() {
        return (int)(Math.ceil(Math.random()*6)) + (int)(Math.ceil(Math.random()*6));
    }

    public void moveToken(int moves) {
        assert moves >= 2 && moves <= 12;
        board.display();
        System.out.println( characterToken.description + " (" + characterToken.symbol() + ")" +" rolled a " + moves + "! (w, a, s, d) to move:");

        // to check if player moved token into room
        Tile prevArea = board.getTile(characterToken.getRow(), characterToken.getCol());
        characterToken.addVisitedPosition(characterToken.row, characterToken.col);

        while (moves > 0) {
            Scanner sc =new Scanner(System.in);
            while (true) {

                if (sc.hasNext("w") && characterToken.moveUp(board)) break;
                else if (sc.hasNext("a") && characterToken.moveLeft(board)) break;
                else if (sc.hasNext("s") && characterToken.moveDown(board)) break;
                else if (sc.hasNext("d") && characterToken.moveRight(board)) break;
                else {
                    System.out.println("Oops! you can't move there.");
                    sc.nextLine();
                }
            }
            sc.nextLine();
            moves--;
            board.display();
            if (moves > 0) System.out.println("Moves left: " + moves);

            // have option to skip moves if entering room
            Tile currentArea = board.getTile(characterToken.getRow(), characterToken.getCol());
            if (prevArea instanceof Corridor && currentArea instanceof Room && moves > 0) {
                System.out.println(characterToken.getDescription() + " has entered the " + ((Room) currentArea).getDescription() +"!");
                System.out.println("Use remaining dice rolls?");
                while (true) {
                    if (sc.hasNext()) {
                        String answer = sc.next();
                        sc.nextLine();
                        if (answer.equalsIgnoreCase("no")) {
                            return;
                        }
                        if (answer.equalsIgnoreCase("yes")) {
                            break;
                        } else {
                            System.out.println("I need either a \"yes\" or \"no\"!");
                        }
                    }
                }
            }
            prevArea = currentArea;

            if (characterToken.cannotMove(board)) {
                System.out.println(
                        "╔═════════════════════════════════╗\n" +
                        "oops! It looks like you can't move!\n" +
                        "╚═════════════════════════════════╝\n");
                return;
            }
        }
    }

    public TurnType startTurn() {
        moveToken(rollDice());
        characterToken.clearVisitedPositions();
        return getTurnType();
    }

    public void printHand() {
        System.out.println("Your cards in hand are:");
        System.out.println("+++++++++++++++++++++++");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i).getDescription());
        }
        System.out.println("+++++++++++++++++++++++");
    }

    public TurnType getTurnType() {
        printHand();

        System.out.println("1. End your turn?\n2. Make a suggestion?\n3. Make an accusation?");

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextInt()) {
                int turnType = sc.nextInt();
                sc.nextLine();
                if (turnType == 1) {
                    return TurnType.END_TURN;
                } else if (turnType == 2) {
                    if (canSuggest()) {
                        return TurnType.SUGGESTION;
                    } else {
                        System.out.println("You must be in a room to make a suggestion!");
                    }
                } else if (turnType == 3) {
                    return TurnType.ACCUSATION;
                } else {
                    System.out.println("I need a number between 1 and 3!");
                }
            } else {
                System.out.println("I need a number!");
                sc.nextLine();
            }
        }
    }

    public boolean canSuggest() {
        return board.getTile(characterToken.getRow(), characterToken.getCol()) instanceof Room;
    }

    public boolean canRefute(Triple triple) {
        assert triple != null;
        for (Card c : hand) {
            if (triple.containsCard(c)) return true;
        }
        return false;
    }

    public Card refute(Triple triple) {
        assert triple != null;
        if (!canRefute(triple)) {
            System.out.println(
                    "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                    "Oops! " + characterToken.getDescription() + " has no matching cards to make a refutation\n" +
                    "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            return null;
        }

        System.out.println(
                "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                characterToken.description.toString() + " is making a refutation!\n" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        System.out.println("The refutation cards are: ");
        triple.print();

        printHand();


        Scanner sc = new Scanner(System.in);

        while (true) {
            if (sc.hasNextInt()) {
                int card = sc.nextInt();
                sc.nextLine();
                if (card <= 0 || card > hand.size()) {
                    System.out.println("I need a number between 0 and " + hand.size() +"!");
                } else if (!triple.containsCard(hand.get(card-1))) {
                    System.out.println("I need a card that matches the suggestion!");
                }
                else {
                    return hand.get(card-1);
                }

            } else {
                System.out.println("I need a number!");
                sc.nextLine();
            }
        }
    }

    public Triple makeSuggestion() {
        while (true) {
            Triple t = getTriple();

            Description room = ((Room)board.getTile(characterToken.getRow(), characterToken.getCol())).getDescription();
            if (room == t.getRoom().getDescription()) {
                return t;
            } else {
                System.out.println(
                        "++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                        "Oops! Your suggestion must include the room you're in!\n" +
                        "++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
        }
    }

    public Triple makeAccusation() {
        return getTriple();
    }

    public Triple getTriple() {
        // Split
        List<Description> weapons = new ArrayList<>();
        List<Description> characters = new ArrayList<>();
        List<Description> rooms = new ArrayList<>();
        for (Description d : Description.values()) {
            if (d.isAWeapon()) weapons.add(d);
            if (d.isACharacter()) characters.add(d);
            if (d.isARoom()) rooms.add(d);
        }

        Card weapon = getCard(weapons, Description.Type.WEAPON);
        Card character = getCard(characters, Description.Type.CHARACTER);
        Card room = getCard(rooms, Description.Type.ROOM);

        return new Triple(weapon, room, character);
    }

    public Card getCard(List<Description> types, Description.Type type) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose a " + type.toString() + ":");
        System.out.println("+++++++++++++++++++++++");
        for (int i = 0; i < types.size(); i++)
            System.out.println((i + 1) + ". " + types.get(i));
        System.out.println("+++++++++++++++++++++++");

        while (true) {
            if (sc.hasNextInt()) {
                int index = sc.nextInt();
                sc.nextLine();
                if (index <= 0 || index > types.size()) {
                    System.out.println("I need a number between 0 and " + types.size() + "!");
                } else {
                    return new Card(types.get(index-1));
                }
            } else {
                System.out.println("I need a number!");
                sc.nextLine();
            }
        }
    }

    public CharacterToken getCharacterToken() {
        return characterToken;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State s) {
        assert s != null;
        state = s;
    }
}
