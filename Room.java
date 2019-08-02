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
        return "_";
    }

    public Description getDescription() {
        return description;
    }
}
