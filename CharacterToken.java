import java.util.*;

public class CharacterToken extends Token {

    private Set<Position> visitedPositions = new HashSet();

    public CharacterToken(Description d, int row, int col) {
        super(d);
        this.row = row;
        this.col = col;
    }

    public void clearVisitedPositions() {
        visitedPositions.clear();
    }

    public void addVisitedPosition(int row, int col) {
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
        if (isValidMove(row-1, col, board)) {
            board.getArea(row, col).removeToken(this);
            board.getArea(row-1, col).addToken(this);
            row--;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveDown(Board board) {
        if (isValidMove(row+1, col, board)) {
            board.getArea(row, col).removeToken(this);
            board.getArea(row+1, col).addToken(this);
            row++;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveLeft(Board board) {
        if (isValidMove(row, col-1, board)) {
            board.getArea(row, col).removeToken(this);
            board.getArea(row, col-1).addToken(this);
            col--;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean moveRight(Board board) {
        if (isValidMove(row, col+1, board)) {
            board.getArea(row, col).removeToken(this);
            board.getArea(row, col+1).addToken(this);
            col++;
            visitedPositions.add(new Position(row, col));
            return true;
        }
        return false;
    }

    public boolean isValidMove(int destRow, int destCol, Board board) {
        if (destRow < 0 || destRow >= Board.ROWS || destCol < 0 || destCol >= Board.COLS) return false;
        if (visitedPositions.contains(new Position(destRow, destCol))) return false;
        if (board.getArea(destRow, destCol) instanceof Inaccessible) return false;
        if (board.getArea(destRow, destCol).tokenAt(destRow, destCol) != null) return false;
        if (board.getArea(destRow, destCol) == board.getArea(row, col)) return true;
        else {
            if (board.getArea(row, col).entranceAt(row, col) != null ||
                board.getArea(destRow, destCol).entranceAt(destRow, destCol) != null) return true;
            else {
                return false;
            }
        }
    }

    public boolean cannotMove(Board board) {
        return !isValidMove(row-1, col, board) &&
                !isValidMove(row+1, col, board) &&
                !isValidMove(row, col-1, board) &&
                !isValidMove(row, col+1, board);
    }

    class Position {
        int row, col;

        public Position(int row, int col) {
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
