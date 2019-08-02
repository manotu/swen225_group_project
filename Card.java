import java.util.Objects;

public class Card {

    private Description description;

    public Card (Description d) {
        assert d != null;
        description = d;
    }

    public Description getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return description == card.description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    public String toString() {
        return description.toString();
    }
}
