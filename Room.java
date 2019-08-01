public class Room extends Tile {

    /**
     * FIELDS
     */
    private Description description;

    /**
     * CONSTRUCTOR
     * @param d
     */
    public Room(Description d) {
        description = d;
    }

    // ==========================
    //         INTERFACE
    // ==========================

    /**
     *
     * @return
     */
    public String symbol() {
        switch (description) {
            case KITCHEN:
                return "k";

            case BALLROOM:
                return "b";

            case CONSERVATORY:
                return "c";

            case DINING_ROOM:
                return "d";

            case BILLIARD_ROOM:
                return "p";

            case LIBRARY:
                return "q";

            case LOUNGE:
                return "l";

            case HALL:
                return "h";

            default:
                return "s"; // Study
        }
    }

    public Description getDescription() {
        return description;
    }
}
