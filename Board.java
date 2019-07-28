import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Board {
    /**
     * CONSTANTS
     */
    public static final int ROWS = 25;
    public static final int COLS = 24;

    /**
     * FIELDS
     */
    private Area[][] board;

    /**
     * CONSTRUCTOR
     */
    public Board() {
        board = new Area[ROWS][COLS];
    }

    // ==========================
    //         INTERFACE
    // ==========================

    /**
     *  Fill the 2D array 'board' field by initialising each entry with an Area.
     *  To do this, we read character by character through the file "board_template.txt"
     *  which contains ASCII symbols. Each symbol represents a particular type of
     *  Area, or a subtype of Area, e.g. a Room or Corridor.
     *
     *  Note that multiple entries in the 'board' point to the same Area object in memory.
     *  This helps us to check whether a particular Room has multiple tokens and their types.
     *
     *  Once we've initialised the 'board', we then set particular Area entries
     *  inside the board whether they have an entrance. We do this via a similar method,
     *  i.e. scanning through the file and finding which entries correspond to an '@'
     *  symbol, denoting "this is an entrance".
     */
    public void loadFile() {
        try {
            // BufferedReader is more efficient than Scanner
            BufferedReader br = new BufferedReader(new FileReader("board_template.txt"));

            // The types of areas on a 'board'
            Room kitchen = new Room(Description.KITCHEN);
            Room ballroom = new Room(Description.BALLROOM);
            Room conservatory = new Room(Description.CONSERVATORY);
            Room diningRoom = new Room(Description.DINING_ROOM);
            Room billiardRoom = new Room(Description.BILLIARD_ROOM);
            Room library = new Room(Description.LIBRARY);
            Room lounge = new Room(Description.LOUNGE);
            Room hall = new Room(Description.HALL);
            Room study = new Room(Description.STUDY);
            Corridor corridor = new Corridor();
            Inaccessible inaccessible = new Inaccessible();

            // For each row and column entry in the 'board' 2D array...
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {

                    // Obtain the character representing our Area or Room object
                    switch((char)br.read()) {
                        case 'K':
                            board[row][col] = kitchen;
                            break;

                        case 'B':
                            board[row][col] = ballroom;
                            break;

                        case 'C':
                            board[row][col] = conservatory;
                            break;

                        case 'D':
                            board[row][col] = diningRoom;
                            break;

                        case 'P':
                            board[row][col] = billiardRoom;
                            break;

                        case 'Q':
                            board[row][col] = library;
                            break;

                        case 'L':
                            board[row][col] = lounge;
                            break;

                        case 'H':
                            board[row][col] = hall;
                            break;

                        case 'S':
                            board[row][col] = study;
                            break;

                        case '_':
                            board[row][col] = corridor;
                            break;

                        default:    // Inaccessible
                            board[row][col] = inaccessible;
                            break;
                    }
                }
                br.read();  // skip the '\n' character at the end
            }
            br.readLine();

            // Now we read through the remaining part of the file, finding '@' symbols
            // which correspond to entrances.
            // For each row and column entry in the 'board' 2D array...
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {

                    // Obtain the character representing our entrance '@'
                    if ((char)br.read() == '@') {
                        board[row][col].addEntrance(new Entrance(row, col));
                    }
                }
                br.read();  // skip the '\n' character at the end
            }
        } catch (IOException e) {
            throw new RuntimeException("File failure.", e);
        }
    }

    /**
     * Distributes weapon tokens across the board, such that initially no room
     * has two or more weapons.
     * This method is called upon startup of a new Cluedo game.
     * @param weaponTokens  The List containing all weapon tokens in the game of Cluedo.
     */
    public void placeWeaponTokens(List<Token> weaponTokens) {
        // 'i' is the index which iterates through each weapon in weaponTokens.
        int i = 0;

        // For each weapon, find a random (row, col) entry in the 'board', and if this
        // area is a Room, has no tokens, and is not an entrance, update the weapon
        // token's (row, col), so that in future, it will be drawn here.
        while (i < weaponTokens.size()) {
            int row = (int) (Math.random() * ROWS);
            int col = (int) (Math.random() * COLS);

            Area area = getArea(row, col);
            if (area instanceof Room && area.getTokens().size() == 0 && area.entranceAt(row, col) == null) {
                Token weapon = weaponTokens.get(i);
                weapon.setCol(col);
                weapon.setRow(row);
                area.addToken(weapon);
                i++;
            }
        }
    }

    /**
     * Given a List of characterTokens, find it's corresponding Area on the board
     * and add the Token that Area.
     * @param characterTokens
     */
    public void placePlayerTokens(List<CharacterToken> characterTokens) {
        for (Token t : characterTokens) {
            getArea(t.getRow(), t.getCol()).addToken(t);
        }
    }

    /**
     *  Give a textual representation of the board:
     *      - _'s are corridors
     *      - K is the kitchen
     *      - B is the ballroom
     *      - C is the conservatory
     *      - D is the dining room
     *      - L is the lounge
     *      - H is the hall
     *      - S is the study
     *      - Q is the library
     *      - P is the billiard room
     *      - X's are inaccessible areas, e.g. the cellar
     *      - @'s are entrances
     *
     *  This method iterates through each entry in the Board
     *  and outputs its String value.
     */
    public void display() {
        // For each row and column entry in the 'board' 2D array...
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                // Give a border to the left of the board
                if (col == 0) System.out.print("|");

                // Print the current Area
                Area area = board[row][col];
                System.out.print(area.print(row, col));

                // Give a border between areas
                System.out.print("|");
            }
            System.out.println();
        }
    }

    /**
     * Retrieve the Area object at some (row, col) of the 'board'
     * @param row
     * @param col
     * @return  Area
     */
    public Area getArea(int row, int col) {
        return board[row][col];
    }

}
