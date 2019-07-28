import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

    CharacterToken characterToken;
    List<Card> hand;
    State state;
    Board board;

    public enum State {
        PLAYING, LOST, WON
    }

    public Player(Board board) {
        this.hand = new ArrayList<>();
        this.state = State.PLAYING;
        this.board = board;
    }

    public void setCharacterToken(CharacterToken characterToken) {
        this.characterToken = characterToken;
    }

    public void addToHand(Card card) {
        this.hand.add(card);
    }

    public int rollDice() {
        return (int)(Math.ceil(Math.random()*6) + Math.ceil(Math.random()*6));
    }

    public void moveToken(int moves) {
        board.display();
        System.out.println( characterToken.description + " rolled " + moves);

        // to check if player token into room
        Area prevArea = board.getArea(characterToken.getRow(), characterToken.getCol());

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
            Area currentArea = board.getArea(characterToken.row, characterToken.col);
            if (prevArea != currentArea && currentArea instanceof Room && move < moves) {
                System.out.println("you have entered " + ((Room) currentArea).getDescription());
                System.out.println("use remaining dice rolls?");
                while (true) {
                    if (sc.hasNextBoolean()) {
                        boolean useRemaining = sc.nextBoolean();
                        if (!useRemaining) {
                            sc.nextLine();
                            return;
                        }
                        else {
                            sc.nextLine();
                            break;
                        }
                    } else {
                        sc.nextLine();
                        System.out.println("bad input");
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

    public Triple takeTurn() {
        moveToken(rollDice());
        characterToken.clearVisitedPositions();


        return null;
    }

    public Triple makeSuggestion() {
        return null;
    }

    public Card makeRefutation(Triple triple) {
        return null;
    }

    public State getState() {
        return this.state;
    }
}
