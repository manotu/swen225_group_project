public enum Description {
    // Weapons
    CANDLESTICK(Type.WEAPON), DAGGER(Type.WEAPON), LEAD_PIPE(Type.WEAPON), REVOLVER(Type.WEAPON), ROPE(Type.WEAPON), SPANNER(Type.WEAPON),

    // Characters
    MISS_SCARLETT(Type.CHARACTER), MRS_WHITE(Type.CHARACTER), MR_GREEN(Type.CHARACTER), MRS_PEACOCK(Type.CHARACTER), PROFESSOR_PLUM(Type.CHARACTER), COLONEL_MUSTARD(Type.CHARACTER),

    // Rooms
    KITCHEN(Type.ROOM), BALLROOM(Type.ROOM), CONSERVATORY(Type.ROOM), DINING_ROOM(Type.ROOM), BILLIARD_ROOM(Type.ROOM), LIBRARY(Type.ROOM), LOUNGE(Type.ROOM), HALL(Type.ROOM), STUDY(Type.ROOM);

    private Type type;

    Description(Type type) {
        this.type = type;
    }

    public boolean isAWeapon() {
        return type == Type.WEAPON;
    }

    public boolean isACharacter() {
        return type == Type.CHARACTER;
    }

    public boolean isARoom() {
        return type == Type.ROOM;
    }

    public enum Type {
        WEAPON, CHARACTER, ROOM
    }
}