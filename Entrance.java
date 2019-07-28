public class Entrance {
    private int row, col;

    public Entrance(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String symbol() {
        return "@";
    }

}
