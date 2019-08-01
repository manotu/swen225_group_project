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
        hand = new ArrayList<>();
        state = State.PLAYING;
        board = b;
    }

    public void setCharacterToken(CharacterToken c) {
        assert c != null;
        characterToken = c;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public int rollDice() {
        return (int)(Math.ceil(Math.random()*6) + Math.ceil(Math.random()*6));
    }

    public void moveToken(int moves) {
        board.display();
        System.out.println( characterToken.description + " rolled " + moves);

        // to check if player moved token into room
        Tile prevArea = board.getTile(characterToken.getRow(), characterToken.getCol());

        int move = 0;
        characterToken.addVisitedPosition(characterToken.row, characterToken.col);

        while (move < moves) {
            Scanner sc =new Scanner(System.in);
            while (true) {

                if (sc.hasNext("w") && characterToken.moveUp(board)) break;
                else if (sc.hasNext("a") && characterToken.moveLeft(board)) break;
                else if (sc.hasNext("s") && characterToken.moveDown(board)) break;
                else if (sc.hasNext("d") && characterToken.moveRight(board)) break;
                else {
                    System.out.println("bad move");
                    sc.nextLine();
                }
            }
            sc.nextLine();
            move++;
            board.display();

            // have option to skip moves if entering room
            Tile currentArea = board.getTile(characterToken.getRow(), characterToken.getCol());
            if (prevArea instanceof Corridor && currentArea instanceof Room && move < moves) {
                System.out.println("you have entered " + ((Room) currentArea).getDescription());
                System.out.println("use remaining dice rolls?");
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
                            System.out.println("bad input");
                        }
                    }
                }
            }
            prevArea = currentArea;

            if (characterToken.cannotMove(board)) {
                System.out.println("oops, can't move");
                return;
            }
        }
    }

    public TurnType startTurn() {
        moveToken(rollDice());
        characterToken.clearVisitedPositions();
        return getTurnType();
    }

    public TurnType getTurnType() {

        System.out.println("1. end turn\n2. make sugg\n3. make accu");
        System.out.println("cards in hand are: ");
        for (Card card : hand) {
            System.out.println(card.getDescription());
        }

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
                        System.out.println("bad input");
                    }
                } else if (turnType == 3) {
                    return TurnType.ACCUSATION;
                } else {
                    System.out.println("bad input");
                }
            } else {
                System.out.println("bad input");
                sc.nextLine();
            }
        }
    }

    public boolean canSuggest() {
        return board.getTile(characterToken.getRow(), characterToken.getCol()) instanceof Room;
    }

    public boolean canRefute(Triple triple) {
        for (Card c : hand) {
            if (triple.containsCard(c)) return true;
        }
        return false;
    }

    public Card refute(Triple triple) {
        System.out.println(characterToken.description.toString() + "is making a refute");
        System.out.println(hand);

        if (!canRefute(triple)) {
            return null;
        }

        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". "+ hand.get(i).getDescription().toString());
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (sc.hasNextInt()) {
                int card = sc.nextInt();
                sc.nextLine();
                if (card <= 0 || card > hand.size()) {
                    System.out.println("bad input");
                } else if (!triple.containsCard(hand.get(card-1))) {
                    System.out.println("bad input");
                }
                else {
                    return hand.get(card-1);
                }

            } else {
                System.out.println("bad input");
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
                System.out.println("bad input");
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
        System.out.println("Choose a " + type.toString());
        for (int i = 0; i < types.size(); i++) {
            System.out.println((i + 1) + ". " + types.get(i));
        }

        while (true) {
            if (sc.hasNextInt()) {
                int index = sc.nextInt();
                sc.nextLine();
                if (index <= 0 || index > types.size()) {
                    System.out.println("bad input");
                } else {
                    return new Card(types.get(index-1));
                }
            } else {
                System.out.println("bad input");
                sc.nextLine();
            }
        }
    }



    public State getState() {
        return this.state;
    }

    public void setState(State s) {
        state = s;
    }
}
