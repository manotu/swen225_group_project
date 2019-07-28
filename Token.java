public class Token {

    protected int row, col;
    protected Description description;

    public Token(Description d) {
        this.description = d;
    }

    public void reallocate(Room room) {


    }

    public void setRow(int row) {this.row = row;}

    public void setCol(int col) {this.col = col;}

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
