import java.util.*;

public class CharacterToken extends Token {

    private Set<Position> visitedPositions = new HashSet();

    public CharacterToken(Description d, int r, int c) {
        assert d != null && r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS;
        description = d;
        row = r;
        col = c;
    }

    public void clearVisitedPositions() {
        visitedPositions.clear();
    }

    public void addVisitedPosition(int row, int col) {
        assert row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS;
        visitedPositions.add(new Position(row, col));
    }

    public String symbol() {
        switch (description) {
            case MRS_WHITE:
                return "1";

            case MR_GREEN:
                return "2";

            case MRS_PEACOCK:
                return "3";

            case PROFESSOR_PLUM:
                return "4";

            case MISS_SCARLETT:
                return "5";

            default:    // Colonel Mustard
                return "6";
        }
    }

    public boolean moveUp(Board board) {
        assert board != null;
        if (isValidMove(row-1, col, board)) {
            board.getTile(row, col).removeToken(this);
            board.getTile(row-1, col).addToken(this);
            row--;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveDown(Board board) {
        assert board != null;
        if (isValidMove(row+1, col, board)) {
            board.getTile(row, col).removeToken(this);
            board.getTile(row+1, col).addToken(this);
            row++;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveLeft(Board board) {
        assert board != null;
        if (isValidMove(row, col-1, board)) {
            board.getTile(row, col).removeToken(this);
            board.getTile(row, col-1).addToken(this);
            col--;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveRight(Board board) {
        assert board != null;
        if (isValidMove(row, col+1, board)) {
            board.getTile(row, col).removeToken(this);
            board.getTile(row, col+1).addToken(this);
            col++;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean isValidMove(int destRow, int destCol, Board board) {
        assert board != null;
        if (destRow < 0 || destRow >= Board.ROWS || destCol < 0 || destCol >= Board.COLS) return false;
        if (visitedPositions.contains(new Position(destRow, destCol))) return false;
        if (board.getTile(destRow, destCol) instanceof Wall) return false;
        if (board.getTile(destRow, destCol).tokenAt(destRow, destCol) != null) return false;
        return true;
    }

    public boolean cannotMove(Board board) {
        assert board != null;
        return !isValidMove(row-1, col, board) &&
                !isValidMove(row+1, col, board) &&
                !isValidMove(row, col-1, board) &&
                !isValidMove(row, col+1, board);
    }

    private class Position {
        private int row, col;

        public Position(int row, int col) {
            assert row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS;
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row &&
                    col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
