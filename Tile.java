import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Tile {
    /**
     * FIELDS
     */
    protected List<Token> tokens = new ArrayList<>();

    // ==========================
    //         INTERFACE
    // ==========================

    public Token tokenAt(int row, int col){
        for (Token t : tokens) {
            if (t.getRow() == row && t.getCol() == col) return t;
        }
        return null;
    }

    public boolean addToken(Token token) {
        return tokens.add(token);
    }

    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public boolean hasToken(Token t) {
        return tokens.contains(t);
    }

    public boolean removeToken(Token token) {
        return tokens.remove(token);
    }


    public abstract String symbol();

    public String print(int row, int col) {
        //
        Token token = tokenAt(row, col);
        if (token != null) return token.symbol();

        //
        return symbol();
    }

}
