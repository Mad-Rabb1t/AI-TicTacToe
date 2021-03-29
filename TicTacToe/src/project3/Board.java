package project3;

import static project3.Symbol.*;

public class Board {
    private final Symbol[][] board;
    private static int boardSize;
    private final int target;
    private int remainingMoves;

    private Board(Symbol[][] board, int remainingMoves, int target) {
        this.board = board;
        this.target = target;
        this.remainingMoves = remainingMoves;
    }

    public static Board createFromString(String boardString, int target) {
        String[] lines = boardString.split("\\n");
        int boardWidth = lines[0].length();
        boardSize = boardWidth;
        Symbol[][] boardContent = new Symbol[boardWidth][boardWidth];
        int availableMoves = 0;

        for (int row = 0; row < boardWidth; row++) {
            char[] line = lines[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                if (line[col] == 'X') boardContent[row][col] = X;
                else if (line[col] == 'O') boardContent[row][col] = O;
                else {
                    boardContent[row][col] = EMPTY;
                    availableMoves++;
                }
            }
        }
        return new Board(boardContent, availableMoves, target);
    }

    public boolean anyMovesRemain() {
        return remainingMoves > 0;
    }

    public Symbol getSymbolAt(int row, int column) {
        return board[row][column];
    }

    public boolean isCellFilled(int row, int column) {
        return board[row][column].isFull();
    }

    public void setSymbolAt(int row, int column, Symbol newSymbol) {
        board[row][column] = newSymbol;
        if(newSymbol != EMPTY) remainingMoves--;
        else remainingMoves++;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(String.format("%s ", board[i][j].toString()));
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
