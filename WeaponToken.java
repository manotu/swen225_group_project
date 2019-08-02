public class WeaponToken extends Token {

    public WeaponToken(Description d) {
        assert d != null;
        description = d;
    }

    public String symbol() {
        switch (description) {
            case CANDLESTICK:
                return "?";

            case DAGGER:
                return ">";

            case LEAD_PIPE:
                return "&";

            case REVOLVER:
                return "*";

            case ROPE:
                return "=";

            default:
                return "$"; // Spanner
        }
    }
}
