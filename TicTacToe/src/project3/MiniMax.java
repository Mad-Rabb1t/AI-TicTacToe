package project3;

import static project3.Symbol.*;

public class MiniMax {

    private static final int MAX_DEPTH = 4;
    private static boolean won = false;
    public static Symbol mySymbol = EMPTY;
    public static Symbol opSymbol = EMPTY;

    public static int miniMax(Board board, int depth, int alpha, int beta, boolean isMax) {
        int nodeValue = heuristicFunction(board, depth);

        if (depth == MAX_DEPTH || !board.anyMovesRemain() || won) {
            won = false;
            return nodeValue;
        }
        // Maximize
        if (isMax) {
            int maximumValue = Integer.MIN_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (!board.isCellFilled(i, j)) {
                        board.setSymbolAt(i, j, mySymbol);
                        maximumValue = Math.max(maximumValue, miniMax(board,
                                depth + 1, alpha, beta, false));
                        board.setSymbolAt(i, j, EMPTY);
                        alpha = Math.max(alpha, maximumValue);
                        if (alpha >= beta) {
                            return maximumValue;
                        }
                    }
                }
            }
            return maximumValue;
            // Minimize
        } else {
            int minimumValue = Integer.MAX_VALUE;
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (!board.isCellFilled(i, j)) {
                        board.setSymbolAt(i, j, opSymbol);
                        minimumValue = Math.min(minimumValue, miniMax(board,
                                depth + 1, alpha, beta, true));
                        board.setSymbolAt(i, j, EMPTY);
                        beta = Math.min(beta, minimumValue);
                        if (alpha >= beta) {
                            return minimumValue;
                        }
                    }
                }
            }
            return minimumValue;
        }
    }

    public static Move getNextMove(Board board) {
//        int[] nextMove = new int[]{-1, -1};
        Move nextMove = new Move(-1, -1);
        int bestValue = Integer.MIN_VALUE;

        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (!board.isCellFilled(i, j)) {
                    board.setSymbolAt(i, j, mySymbol);
                    int moveValue = miniMax(board, 0, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, false);
                    board.setSymbolAt(i, j, EMPTY);
                    if (moveValue > bestValue) {
                        nextMove.x = i;
                        nextMove.y = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        System.out.println("BEST VALUE: " + bestValue);
        System.out.println("BEST MOVE ROW: " + nextMove.x);
        System.out.println("BEST MOVE COL: " + nextMove.y);
        return nextMove;
    }


    private static int heuristicFunction(Board board, int depth) {
        int value = 0;
        int bWidth = board.getBoardSize();
        int target = board.getTarget();

        int xcount = 0, ocount = 0;
        int xpcount = 0, opcount = 0;
        int maxXcount = 0, maxOcount = 0;

        // Find if any row is winning
        for (int row = 0; row < bWidth; row++) {
            for (int col = 0; col < bWidth; col++) {
                char mark = board.getSymbolAt(row, col).getSymbol();

                if (mark == 'X') {
                    xcount++;
                    xpcount++;
                    ocount = 0;
                    opcount = 0;
                } else if (mark == 'O') {
                    ocount++;
                    opcount++;
                    xcount = 0;
                    xpcount = 0;
                } else {
                    xpcount++;
                    opcount++;
                }
                if(xpcount == target + 1){
                    xpcount = target;
                    if(board.getSymbolAt(row, col - target) == X) xcount--;
                }
                if(opcount == target + 1){
                    opcount = target;
                    if(board.getSymbolAt(row, col - target) == O) ocount--;
                }
                if (xpcount == target && maxXcount < xcount) {
                    maxXcount = xcount;
                }
                if (opcount == target && maxOcount < ocount) {
                    maxOcount = ocount;
                }
            }
            if (maxXcount == target) {
                won = true;
//                System.out.println("MaxXCount: " + maxXcount);
                if (mySymbol == X) return (Integer.MAX_VALUE / 2) - depth;
                else return (Integer.MIN_VALUE / 2) + depth;
            }
            if (maxOcount == target) {
                won = true;
//                System.out.println("MaxOCount: " + maxOcount);
                if (mySymbol == O) return (Integer.MAX_VALUE / 2) - depth;
                else return (Integer.MIN_VALUE / 2) + depth;
            }
//            if (mySymbol == X) value += (maxXcount * maxXcount) - (maxOcount * maxOcount);
//            else value += (maxOcount * maxOcount) - (maxXcount * maxXcount);
            if (mySymbol == X) value += maxXcount - maxOcount;
            else value += maxOcount - maxXcount;
            xcount = 0;
            ocount = 0;
            maxXcount = 0;
            maxOcount = 0;
            xpcount = 0;
            opcount = 0;
        }

        // Find if any column is winning
        for (int col = 0; col < bWidth; col++) {
            for (int row = 0; row < bWidth; row++) {
                char mark = board.getSymbolAt(row, col).getSymbol();
                if (mark == 'X') {
                    xcount++;
                    xpcount++;
                    ocount = 0;
                    opcount = 0;
                } else if (mark == 'O') {
                    ocount++;
                    opcount++;
                    xcount = 0;
                    xpcount = 0;
                } else {
                    xpcount++;
                    opcount++;
                }
                if(xpcount == target + 1){
                    xpcount = target;
                    if(board.getSymbolAt(col, row - target) == X) xcount--;
                }
                if(opcount == target + 1){
                    opcount = target;
                    if(board.getSymbolAt(col, row - target) == O) ocount--;
                }
                if (xpcount == target && maxXcount < xcount) {
                    maxXcount = xcount;
                }
                if (opcount == target && maxOcount < ocount) {
                    maxOcount = ocount;
                }
            }
            if (maxXcount == target) {
                won = true;
//                System.out.println("MaxXCount: " + maxXcount);
                if (mySymbol == X) return (Integer.MAX_VALUE / 2) - depth;
                else return (Integer.MIN_VALUE / 2) + depth;
            }
            if (maxOcount == target) {
                won = true;
//                System.out.println("MaxOCount: " + maxOcount);
                if (mySymbol == O) return (Integer.MAX_VALUE / 2) - depth;
                else return (Integer.MIN_VALUE / 2) + depth;
            }
//            if (mySymbol == X) value += (maxXcount * maxXcount) - (maxOcount * maxOcount);
//            else value += (maxOcount * maxOcount) - (maxXcount * maxXcount);
            if (mySymbol == X) value += maxXcount - maxOcount;
            else value += maxOcount - maxXcount;

            xcount = 0;
            ocount = 0;
            maxXcount = 0;
            maxOcount = 0;
            xpcount = 0;
            opcount = 0;
        }

        // Find if any diagonal is winning
        for (int i = 0; i < bWidth; i++) {
            char mark = board.getSymbolAt(i, i).getSymbol();
            if (mark == 'X') {
                xcount++;
                xpcount++;
                ocount = 0;
                opcount = 0;
            } else if (mark == 'O') {
                ocount++;
                opcount++;
                xcount = 0;
                xpcount = 0;
            } else {
                xpcount++;
                opcount++;
            }
            if(xpcount == target + 1){
                xpcount = target;
                if(board.getSymbolAt(i - target, i - target) == X) xcount--;
            }
            if(opcount == target + 1){
                opcount = target;
                if(board.getSymbolAt(i - target, i - target) == O) ocount--;
            }

            if (xpcount == target && maxXcount < xcount) {
                maxXcount = xcount;
            }
            if (opcount == target && maxOcount < ocount) {
                maxOcount = ocount;
            }
        }
        if (maxXcount == target) {
            won = true;
//                System.out.println("MaxXCount: " + maxXcount);
            if (mySymbol == X) return (Integer.MAX_VALUE / 2) - depth;
            else return (Integer.MIN_VALUE / 2) + depth;
        }
        if (maxOcount == target) {
            won = true;
//                System.out.println("MaxOCount: " + maxOcount);
            if (mySymbol == O) return (Integer.MAX_VALUE / 2) - depth;
            else return (Integer.MIN_VALUE / 2) + depth;
        }
//        if (mySymbol == X) value += (maxXcount * maxXcount) - (maxOcount * maxOcount);
//        else value += (maxOcount * maxOcount) - (maxXcount * maxXcount);
        if (mySymbol == X) value += maxXcount - maxOcount;
        else value += maxOcount - maxXcount;

        xcount = 0;
        ocount = 0;
        maxXcount = 0;
        maxOcount = 0;
        xpcount = 0;
        opcount = 0;


        int indexMax = bWidth - 1;
        for (int i = 0; i <= indexMax; i++) {
            char mark = board.getSymbolAt(i, indexMax - i).getSymbol();
            if (mark == 'X') {
                xcount++;
                xpcount++;
                ocount = 0;
                opcount = 0;
            } else if (mark == 'O') {
                ocount++;
                opcount++;
                xcount = 0;
                xpcount = 0;
            } else {
                xpcount++;
                opcount++;
            }
            if(xpcount == target + 1){
                xpcount = target;
                if(board.getSymbolAt(i - target, (indexMax - i)  + target) == X) xcount--;
            }
            if(opcount == target + 1){
                opcount = target;
                if(board.getSymbolAt(i - target, (indexMax - i)  + target) == O) ocount--;
            }
            if (xpcount == target && maxXcount < xcount) {
                maxXcount = xcount;
            }
            if (opcount == target && maxOcount < ocount) {
                maxOcount = ocount;
            }
        }
        if (maxXcount == target) {
            won = true;
//                System.out.println("MaxXCount: " + maxXcount);
            if (mySymbol == X) return (Integer.MAX_VALUE / 2) - depth;
            else return (Integer.MIN_VALUE / 2) + depth;
        }
        if (maxOcount == target) {
            won = true;
//                System.out.println("MaxOCount: " + maxOcount);
            if (mySymbol == O) return (Integer.MAX_VALUE / 2) - depth;
            else return (Integer.MIN_VALUE / 2) + depth;
        }
//        if (mySymbol == X) value += (maxXcount * maxXcount) - (maxOcount * maxOcount);
//        else value += (maxOcount * maxOcount) - (maxXcount * maxXcount);
        if (mySymbol == X) value += maxXcount - maxOcount;
        else value += maxOcount - maxXcount;

        if(value > 0) value -= depth;
        else value += depth;

        return value;
    }

}
