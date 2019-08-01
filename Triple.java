import java.util.Objects;

/**
 * Class representing a Triple of 3 cards, i.e. a triple of
 * - a weapon card
 * - a room card
 * - a character card.
 *
 * This class becomes useful during instances of the game where a Triple of
 * 3 cards are required, e.g. setting up murder circumstances, a player
 * making a suggestion, a player making an accusation, e.t.c.
 */
public class Triple {

    /**
     * FIELDS: A triple of 3 different cards.
     */
    private final Card weapon;
    private final Card room;
    private final Card character;

    /**
     * CONSTRUCTOR: Initialise our triple of 3 cards.
     * @param w The weapon card.
     * @param r The room card.
     * @param c The character card.
     */
    public Triple(Card w, Card r, Card c) {
        assert w != null && r != null && c != null : "Arguments must not be null";
        weapon = w;
        room = r;
        character = c;
    }

    /**
     * Check whether this Triple is equal to another Triple.
     * Triples are equal, if they have the same weapon, room, and character card.
     * @param o
     * @return Whether two Triples are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple triple = (Triple) o;

        // Note that it's safe to directly check whether the fields are equal since
        // the constructor ensures the fields are not null.
        return triple.weapon.equals(weapon) &&
                triple.room.equals(room) &&
                triple.character.equals(character);
    }

    /**
     * When we override the equals() method, it’s good practice to override the hashCode() method
     * so that their contract is not violated by our implementation.
     * @return HashCode of this Triple.
     */
    @Override
    public int hashCode() {
        return Objects.hash(weapon, room, character);
    }

    /**
     * Check whether any field (card) of this Triple is the same as the card passed in.
     * @param c The passed in game.Card object.
     * @return Whether this Triple contains the Card c passed in.
     */
    public boolean containsCard(Card c) {
        assert c != null : "game.Card cannot be null";
        return weapon.equals(c) || room.equals(c) || character.equals(c);
    }

    /**
     * Get the weapon Card in this Triple.
     * @return The weapon game.Card.
     */
    public Card getWeapon() {
        return weapon;
    }

    /**
     * Get the room Card in this Triple.
     * @return The room game.Card.
     */
    public Card getRoom() {
        return room;
    }

    /**
     * Get the character Card in this Triple.
     * @return The character Card.
     */
    public Card getCharacter() {
        return character;
    }
}
