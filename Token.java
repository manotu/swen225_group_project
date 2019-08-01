public abstract class Token {

    protected int row, col;
    protected Description description;

    public void reallocate(Room r, Board b) {
        assert r != null && b != null;
        // first find the room current token is in, and remove from that room

        for (int row = 0; row < b.ROWS; row++) {
            for (int col = 0; col < b.COLS; col++) {
                Tile tile = b.getTile(row, col);
                if (tile.hasToken(this)) {
                    tile.removeToken(this);
                }
            }
        }

        // then find the room and add token to the room
        // need to iterate to find free row,col
        while (true) {
            int row = (int)(Math.random() * b.ROWS);
            int col = (int)(Math.random() * b.COLS);

            if (b.getTile(row, col) == r && r.tokenAt(row, col) == null) {
                r.addToken(this);
                this.row = row;
                this.col = col;
                assert this.row >= 0 && this.row < Board.ROWS && this.col >= 0 && this.col < Board.COLS;
                return;
            }
        }
    }

    public void setRow(int r) {
        assert r >= 0 && r < Board.ROWS;
        row = r;
    }

    public void setCol(int c) {
        assert c >= 0 && c < Board.COLS;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public abstract String symbol();
}
