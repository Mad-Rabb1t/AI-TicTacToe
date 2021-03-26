package project3;

public enum Symbol {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final char symbol;

    Symbol(char initSymbol) {
        this.symbol = initSymbol;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public boolean isFull() {
        return this != EMPTY;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
