import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Area {
    /**
     * FIELDS
     */
    protected List<Entrance> entrances = new ArrayList<>();
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

    public boolean removeToken(Token token) {
        return tokens.remove(token);
    }

    public List<Entrance> getEntrances() {
        return Collections.unmodifiableList(entrances);
    }

    public boolean addEntrance(Entrance entrance) {
        return entrances.add(entrance);
    }

    public Entrance entranceAt(int row, int col) {
        for (Entrance e : entrances) {
            if (e.getRow() == row && e.getCol() == col) return e;
        }
        return null;
    }

    public abstract String symbol();

    public String print(int row, int col) {
        //
        Token token = tokenAt(row, col);
        if (token != null) return token.symbol();

        //
        Entrance entrance = entranceAt(row, col);
        if (entrance != null) return entrance.symbol();

        //
        return symbol();
    }

}
