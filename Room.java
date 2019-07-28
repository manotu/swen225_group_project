public class Room extends Area {

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
                return "K";

            case BALLROOM:
                return "B";

            case CONSERVATORY:
                return "C";

            case DINING_ROOM:
                return "D";

            case BILLIARD_ROOM:
                return "P";

            case LIBRARY:
                return "Q";

            case LOUNGE:
                return "L";

            case HALL:
                return "H";

            default:
                return "S"; // Study
        }
    }

    public Description getDescription() {
        return description;
    }
}
