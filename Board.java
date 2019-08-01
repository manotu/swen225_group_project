import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board {
    /**
     * CONSTANTS
     */
    public static final int ROWS = 25;
    public static final int COLS = 24;

    /**
     * FIELDS
     */
    private Tile[][] board;

    /**
     * CONSTRUCTOR
     */
    public Board() {
        board = new Tile[ROWS][COLS];
    }

    public void setup(List<WeaponToken> weapons, List<CharacterToken> characters) {
        assert weapons != null && characters != null;
        loadFile();
        placeWeaponTokens(weapons);
        placePlayerTokens(characters);
    }

    // ==========================
    //         INTERFACE
    // ==========================

    /**
     *  Fill the 2D array 'board' field by initialising each entry with an Tile.
     *  To do this, we read character by character through the file "board_template.txt"
     *  which contains ASCII symbols. Each symbol represents a particular type of
     *  Tile, or a subtype of Tile, e.g. a Room or Corridor.
     *
     *  Note that multiple entries in the 'board' point to the same Tile object in memory.
     *  This helps us to check whether a particular Room has multiple tokens and their types.
     *
     *  Once we've initialised the 'board', we then set particular Tile entries
     *  inside the board whether they have an entrance. We do this via a similar method,
     *  i.e. scanning through the file and finding which entries correspond to an '@'
     *  symbol, denoting "this is an entrance".
     */
    public void loadFile() {
        try {
            // BufferedReader is more efficient than Scanner
            BufferedReader br = new BufferedReader(new FileReader("board_template.txt"));

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
            Wall wall = new Wall();

            // For each row and column entry in the 'board' 2D array...
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {

                    // Obtain the character representing our Tile or Room object
                    switch((char)br.read()) {
                        case 'k':
                            board[row][col] = kitchen;
                            break;

                        case 'b':
                            board[row][col] = ballroom;
                            break;

                        case 'c':
                            board[row][col] = conservatory;
                            break;

                        case 'd':
                            board[row][col] = diningRoom;
                            break;

                        case 'p':
                            board[row][col] = billiardRoom;
                            break;

                        case 'q':
                            board[row][col] = library;
                            break;

                        case 'l':
                            board[row][col] = lounge;
                            break;

                        case 'h':
                            board[row][col] = hall;
                            break;

                        case 's':
                            board[row][col] = study;
                            break;

                        case '_':
                            board[row][col] = corridor;
                            break;

                        case '#':
                            board[row][col] = wall;
                            break;
                    }
                }
                br.read();  // skip the '\n' character at the end
            }
            br.readLine();
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
    public void placeWeaponTokens(List<WeaponToken> weaponTokens) {
        // 'i' is the index which iterates through each weapon in weaponTokens.
        int i = 0;

        // For each weapon, find a random (row, col) entry in the 'board', and if this
        // area is a Room and has no tokens, update the weapon token's (row, col),
        // so that in future, it will be drawn here.
        while (i < weaponTokens.size()) {
            int row = (int) (Math.random() * ROWS);
            int col = (int) (Math.random() * COLS);

            Tile area = getTile(row, col);
            if (area instanceof Room && area.getTokens().size() == 0) {
                Token weapon = weaponTokens.get(i);
                weapon.setCol(col);
                weapon.setRow(row);
                area.addToken(weapon);
                i++;
            }
        }
    }

    /**
     * Given a List of characterTokens, find it's corresponding Tile on the board
     * and add the Token that Tile.
     * @param characterTokens
     */
    public void placePlayerTokens(List<CharacterToken> characterTokens) {
        for (Token t : characterTokens) {
            getTile(t.getRow(), t.getCol()).addToken(t);
        }
    }

    /**
     *  Give a textual representation of the board:
     *      - _'s are corridors
     *      - k is the kitchen
     *      - b is the ballroom
     *      - c is the conservatory
     *      - d is the dining room
     *      - l is the lounge
     *      - h is the hall
     *      - s is the study
     *      - q is the library
     *      - p is the billiard room
     *      - #'s are inaccessible areas, e.g. the cellar
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

                // Print the current Tile
                Tile area = board[row][col];
                System.out.print(area.print(row, col));

                // Give a border between areas
                System.out.print("|");
            }
            System.out.println();
        }
    }

    /**
     * Retrieve the Tile object at some (row, col) of the 'board'
     * @param row
     * @param col
     * @return  Tile
     */
    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    /**
     *
     * @param t
     */
    public void reallocateTokens(Triple t) {
        assert t != null;
        Token character = findToken(t.getCharacter().getDescription());
        Token weapon = findToken(t.getWeapon().getDescription());

        // should never be null
        Room room = findRoom(t.getRoom().getDescription());
        System.out.println(room.getDescription());

        if (!room.hasToken(character) && character != null) {
            character.reallocate(room, this);
        }
        if (!room.hasToken(weapon) && weapon != null) {
            weapon.reallocate(room, this);
        }

    }

    public Token findToken(Description d) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                List<Token> tokens = getTile(row, col).getTokens();
                for (Token t : tokens) {
                    if (t.description == d) return t;
                }
            }
        }
        return null;
    }

    public Room findRoom(Description d) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile tile = getTile(row, col);
                if (tile instanceof Room && ((Room) tile).getDescription() == d) {
                    return (Room) tile;
                }
            }
        }
        return null;
    }
}
