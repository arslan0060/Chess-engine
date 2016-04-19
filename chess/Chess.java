/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.*;
import javax.swing.*;
import com.sun.corba.se.impl.orbutil.ObjectWriter;

/**
 *
 * @author Arslan Memon
 */
public class Chess {

    /**
     * @param args the command line arguments
     */
    public static String chessboard[][] = {
        {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"},
        {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP"},
        {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
        {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
        {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
        {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
        {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP"},
        {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"}
    };
    public static int bKx = 4, bKy = 0, wKx = 4, wKy = 7; //keeps track of kings position
    public static int humanColor = 0, globalDepth = 4;//-1 is black, 1 is white

    public static String movesPossible() {
        String moves = "";//move in form yxbap where x and y are the indices of the piece,
        //a and b is the location to move the piece to and P is the piece taken 
        for (int i = 7; i >=0; i--) {
            for (int j = 0; j < 8; j++) {
                switch (chessboard[i][j]) {
                    case "wP":
                        moves += possiblePawnMoves(i, j);
                        break;
                    case "wR":
                        moves += possibleRookMoves(i, j);
                        break;
                    case "wB":
                        moves += possibleBishopMoves(i, j);
                        break;
                    case "wN":
                        moves += possibleKnightMoves(i, j);
                        break;
                    case "wK":
                        moves += possibleKingMoves(i, j);
                        break;
                    case "wQ":
                        moves += possibleQueenMoves(i, j);
                        break;
                }
            }
        }
        return moves;
    }

    public static String possiblePawnMoves(int y, int x) {
        String move = "";
        String transform[] = {"wQ", "wN", "wR", "wB"};//pawn can trasform into these
        //loop checks the diagonals of the pawn to see if there is a black piece there
        for (int i = -1; i < 2; i += 2) {
            try {
                if ("b".equals(chessboard[y - 1][x + i].substring(0, 1))) {
                    //if pawns next move gets it to the top of the board, it can transform
                    if (y - 1 == 0) {
                        //loop records pawn transfornations if they do not put king in check
                        for (int j = 0; j < 4; j++) {
                            String pTaken = chessboard[y - 1][x + i];
                            chessboard[y - 1][x + i] = transform[j];
                            chessboard[y][x] = "  ";
                            if (kingIsSafe()) {
                                move = move + y + x + (y - 1) + (x + i) + pTaken + "T" + transform[j];
                            }
                            chessboard[y - 1][x + i] = pTaken;
                            chessboard[y][x] = "wP";
                        }

                    } //other wise just record where the pawn moves and what it kills
                    else {
                        String pTaken = chessboard[y - 1][x + i];
                        chessboard[y - 1][x + i] = "wP";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y - 1) + (x + i) + pTaken;
                        }
                        chessboard[y - 1][x + i] = pTaken;
                        chessboard[y][x] = "wP";
                    }

                }
            } catch (Exception e) {
            }
        }
        //check straight ahead of pawn to see if space is empty

        if (y - 1 == 0 && "  ".equals(chessboard[y - 1][x])) {
            //loop for transform
            for (int j = 0; j < 4; j++) {
                chessboard[y - 1][x] = transform[j];
                chessboard[y][x] = "  ";
                if (kingIsSafe()) {
                    move = move + y + x + (y - 1) + (x) + "  " + "T" + transform[j];
                }
                chessboard[y - 1][x] = "  ";
                chessboard[y][x] = "wP";
            }
        } else if ("  ".equals(chessboard[y - 1][x])) {
            chessboard[y - 1][x] = "wP";
            chessboard[y][x] = "  ";
            if (kingIsSafe()) {
                move = move + y + x + (y - 1) + (x) + "  ";
            }
            //check if pawn cam move forward twice
            if (y == 6 && "  ".equals(chessboard[y - 2][x])) {
                chessboard[y - 2][x] = "wP";
                chessboard[y - 1][x] = "  ";
                if (kingIsSafe()) {
                    move = move + y + x + (y - 2) + (x) + "  ";
                }
                chessboard[y - 2][x] = "  ";
                chessboard[y - 1][x] = "wP";
            }

            chessboard[y - 1][x] = "  ";
            chessboard[y][x] = "wP";

        }
        return move;
    }

    public static String possibleKingMoves(int y, int x) {
        String move = "";
        String pTaken = "";
        //loop to check what moves king can make
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (i != 0 || j != 0) {
                        //check around king, and if not in check, then add to list
                        if (chessboard[y + i][x + j].substring(0, 1).equals("b") || chessboard[y + i][x + j].equals("  ")) {
                            pTaken = chessboard[i + y][j + x];
                            chessboard[i + y][j + x] = "wK";
                            chessboard[y][x] = "  ";
                            wKy = i + y;
                            wKx = j + x;
                            if (kingIsSafe()) {
                                move = move + y + x + (y + i) + (x + j) + pTaken;
                            }
                            chessboard[y][x] = "wK";
                            chessboard[i + y][j + x] = pTaken;
                            wKy = y;
                            wKx = x;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return move;
    }

    public static String possibleKnightMoves(int y, int x) {
        String move = "";
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                try {
                    //check if up/down two, right/left one, is valid
                    if (chessboard[y + 2 * i][x + j].substring(0, 1).equals("b") || chessboard[y + 2 * i][x + j].equals("  ")) {
                        String pTaken = chessboard[2 * i + y][j + x];
                        chessboard[2 * i + y][j + x] = "wN";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + 2 * i) + (x + j) + pTaken;
                        }
                        chessboard[2 * i + y][j + x] = pTaken;
                        chessboard[y][x] = "wN";
                    }
                } catch (Exception e) {
                }
                try {
                    //check if up/down one, right/left two, is valid
                    if (chessboard[y + i][x + 2 * j].substring(0, 1).equals("b") || chessboard[y + i][x + 2 * j].equals("  ")) {
                        String pTaken = chessboard[i + y][2 * j + x];
                        chessboard[i + y][2 * j + x] = "wN";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + i) + (x + 2 * j) + pTaken;
                        }
                        chessboard[i + y][2 * j + x] = pTaken;
                        chessboard[y][x] = "wN";
                    }
                } catch (Exception e) {
                }
            }
        }
        return move;

    }

    public static String possibleQueenMoves(int y, int x) {
        String move = "";
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int mag = 1;
                try {
                    //check diagonals, vertical, horizontal of queen until space is not blank and add move
                    //to string if not in check
                    while ("  ".equals(chessboard[i * mag + y][j * mag + x])) {
                        chessboard[y + mag * i][j * mag + x] = "wQ";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + i * mag) + (x + j * mag) + "  ";
                        }
                        chessboard[y + i * mag][j * mag + x] = "  ";
                        chessboard[y][x] = "wQ";
                        mag++;
                    }
                    //if the piece is black, add move to string
                    if ((chessboard[y + i * mag][x + j * mag].substring(0, 1)).equals("b")) {
                        String pTaken = chessboard[y + i * mag][j * mag + x];
                        chessboard[y + i * mag][j * mag + x] = "wQ";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + i * mag) + (x + j * mag) + pTaken;
                        }
                        chessboard[y + i * mag][j * mag + x] = pTaken;
                        chessboard[y][x] = "wQ";

                    }
                } catch (Exception e) {
                }
            }

        }
        return move;
    }

    public static String possibleBishopMoves(int y, int x) {
        String move = "";
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                //similar to queen, but only checks diagonals
                int mag = 1;
                try {
                    while ("  ".equals(chessboard[i * mag + y][j * mag + x])) {
                        chessboard[y + mag * i][j * mag + x] = "wB";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + i * mag) + (x + j * mag) + "  ";
                        }
                        chessboard[y + i * mag][j * mag + x] = "  ";
                        chessboard[y][x] = "wB";
                        mag++;
                    }
                    if ((chessboard[y + i * mag][x + j * mag].substring(0, 1)).equals("b")) {
                        String pTaken = chessboard[y + i * mag][j * mag + x];
                        chessboard[y + i * mag][j * mag + x] = "wB";
                        chessboard[y][x] = "  ";
                        if (kingIsSafe()) {
                            move = move + y + x + (y + i * mag) + (x + j * mag) + pTaken;
                        }
                        chessboard[y + i * mag][j * mag + x] = pTaken;
                        chessboard[y][x] = "wB";

                    }
                } catch (Exception e) {
                }
            }

        }
        return move;
    }

    public static String possibleRookMoves(int y, int x) {
        String move = "", pTaken;
        for (int i = -1; i < 2; i += 2) {
            int magnitude = 1;
            try {
                while (chessboard[y][x + i * magnitude].equals("  ")) {
                    chessboard[y][i * magnitude + x] = "wR";
                    chessboard[y][x] = "  ";
                    if (kingIsSafe()) {
                        move = move + y + x + (y) + (x + i * magnitude) + "  ";
                    }
                    chessboard[y][i * magnitude + x] = "  ";
                    chessboard[y][x] = "wR";
                    magnitude++;
                }
                if ((chessboard[y][x + i * magnitude].substring(0, 1)).equals("b")) {
                    pTaken = chessboard[y][i * magnitude + x];
                    chessboard[y][i * magnitude + x] = "wR";
                    chessboard[y][x] = "  ";
                    if (kingIsSafe()) {
                        move = move + y + x + (y) + (x + i * magnitude) + pTaken;
                    }
                    chessboard[y][i * magnitude + x] = pTaken;
                    chessboard[y][x] = "wR";

                }
            } catch (Exception e) {
            }
        }

        for (int i = -1; i < 2; i += 2) {
            int magnitude = 1;
            try {
                while (chessboard[y + i * magnitude][x].equals("  ")) {
                    chessboard[i * magnitude + y][x] = "wR";
                    chessboard[y][x] = "  ";
                    if (kingIsSafe()) {
                        move = move + y + x + (i * magnitude + y) + (x) + "  ";
                    }
                    chessboard[i * magnitude + y][x] = "  ";
                    chessboard[y][x] = "wR";
                    magnitude++;
                }
                if ((chessboard[y + i * magnitude][x].substring(0, 1)).equals("b")) {
                    pTaken = chessboard[i * magnitude + y][x];
                    chessboard[i * magnitude + y][x] = "wR";
                    chessboard[y][x] = "  ";
                    if (kingIsSafe()) {
                        move = move + y + x + (i * magnitude + y) + (x) + pTaken;
                    }
                    chessboard[i * magnitude + y][x] = pTaken;
                    chessboard[y][x] = "wR";

                }
            } catch (Exception e) {
            }
        }

        return move;
    }

    public static boolean kingIsSafe() {
        //loop checks if black queen, pawn, bishop can attack  king, move invalid if they can
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                try {
                    int mag = 1;
                    while ("  ".equals(chessboard[wKy + i * mag][wKx + j * mag])) {
                        mag++;
                    }
                    if ("bB".equals(chessboard[wKy + i * mag][wKx + j * mag])
                            || "bQ".equals(chessboard[wKy + i * mag][wKx + j * mag])) {
                        return false;
                    }
                    if (mag == 1 && i == -1 && "bP".equals(chessboard[wKy + i * mag][wKx + j * mag])) {
                        return false;
                    }
                } catch (Exception e) {
                }//check if knight can attack king
                try {
                    if ("bN".equals(chessboard[wKy + 2 * i][wKx + j])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                try {
                    if ("bN".equals(chessboard[wKy + i][wKx + 2 * j])) {
                        return false;
                    }
                } catch (Exception e) {
                }

            }

        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (i != 0 || j != 0) {
                        if ("bK".equals(chessboard[wKy + i][wKx + j])) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        //loop checks if queen or rook can attack white king, and if they can move is invalid
        for (int i = -1; i < 2; i += 2) {
            try {
                int mag = 1;
                while ("  ".equals(chessboard[wKy][wKx + i * mag])) {
                    mag++;
                }
                if ("bR".equals(chessboard[wKy][wKx + i * mag])
                        || "bQ".equals(chessboard[wKy][wKx + i * mag])) {
                    return false;
                }

            } catch (Exception e) {
            }

            try {
                int mag = 1;
                while ("  ".equals(chessboard[wKy + i * mag][wKx])) {
                    mag++;
                }
                if ("bR".equals(chessboard[wKy + i * mag][wKx])
                        || "bQ".equals(chessboard[wKy + i * mag][wKx])) {
                    return false;
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static void undoMove(String move) {
        if (move.length() == 6) {
            chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'] = chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'];
            chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'] = move.substring(4);
            if ("wK".equals(chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'])) {
                wKy = move.charAt(0) - '0';
                wKx = move.charAt(1) - '0';
            }
        } else {
            chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'] = move.substring(4, 6);
            chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'] = "wP";

        }
    }

    public static void move(String move) {
        if (move.length() > 6 && move.charAt(6) == 'T') {
            move = move.substring(0, 9);
        } else {
            move = move.substring(0, 6);
        }
        
        
        if (move.length() == 6) {
            chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'] = chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'];
            chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'] = "  ";
            if ("wK".equals(chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'])) {
                wKy = move.charAt(2) - '0';
                wKx = move.charAt(3) - '0';
            }

        } else {
            chessboard[move.charAt(2) - '0'][move.charAt(3) - '0'] = move.substring(7,9);
            chessboard[move.charAt(0) - '0'][move.charAt(1) - '0'] = "  ";

        }

    }

    public static void flipBoard() {
        //flips board and changes  black to white and white to black, to prevent repetetiveness
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {

                String temp;
                if (chessboard[i][j].equals("  ")) {
                    temp = "  ";
                } else if ((chessboard[i][j]).charAt(0) == 'b') {
                    temp = "w" + (chessboard[i][j]).substring(1);
                } else {
                    temp = "b" + (chessboard[i][j]).substring(1);
                }
                if ("  ".equals(chessboard[7 - i][7 - j])) {
                    chessboard[i][j] = "  ";
                } else {
                    if (chessboard[7 - i][7 - j].charAt(0) == 'b') {
                        chessboard[i][j] = "w" + (chessboard[7 - i][7 - j]).substring(1);
                    }
                    if (chessboard[7 - i][7 - j].charAt(0) == 'w') {
                        chessboard[i][j] = "b" + (chessboard[7 - i][7 - j]).substring(1);
                    }

                    if (chessboard[i][j].equals("bK")) {
                        bKy = i;
                        bKx = j;
                    }
                    if (chessboard[i][j].equals("wK")) {
                        wKy = i;
                        wKx = j;
                    }
                }
                chessboard[7 - i][7 - j] = temp;
                if (temp.equals("wK")) {
                    wKy = 7 - i;
                    wKx = 7 - j;
                }
                if (temp.equals("bK")) {
                    bKy = 7 - i;
                    bKx = 7 - j;
                }
            }
        }
    }

    

    ;
    public static String AlphaBetaTreePruning(int alpha, int beta, String move, int MinOrMax, int currentDepth, int overAllDepth) {
        String moves = movesPossible();
        if (currentDepth == 0 || moves.length() == 0) {
            return move + rateBoard.rateBoard()* MinOrMax;
        }
        MinOrMax = MinOrMax * -1;
//sort moves later
        
        for (int i = 0; i < moves.length(); i += 6) {
            String m;
            if (moves.length() > i + 6 && moves.charAt(i+6) == 'T') {
                m = moves.substring(i, i+9);
                i+=3;
            } else {
                m = moves.substring(i, i+6);
            }
            move(m);
            flipBoard();
            String returnMove = AlphaBetaTreePruning(alpha, beta, m, MinOrMax, currentDepth - 1, overAllDepth);
            int value;
            if (returnMove.length() > 6 && returnMove.charAt(6) == 'T') {
                value = Integer.valueOf(returnMove.substring(9));
            } else {
                value = Integer.valueOf(returnMove.substring(6));
            }
            flipBoard();
            undoMove(m);
            if (MinOrMax == -1) {
                if (value <= beta) {
                    beta = value;
                    if (currentDepth == overAllDepth) {
                        if (returnMove.length() > 6 && returnMove.charAt(6) == 'T') {
                            move = returnMove.substring(0, 9);
                        } else {
                            move = returnMove.substring(0, 6);
                        }
                    }
                }
            } else if (value > alpha) {
                alpha = value;
                if (currentDepth == overAllDepth) {
                    if (returnMove.length() > 6 && returnMove.charAt(6) == 'T') {
                            move = returnMove.substring(0, 9);
                        } else {
                            move = returnMove.substring(0, 6);
                        }
                }
            }
            if (alpha >= beta) {
                if (MinOrMax == -1) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }
        if (MinOrMax == -1) {
            return move + beta;
        } else {
            return move + alpha;
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        JFrame f = new JFrame("Chess AI");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI userInterface = new UI();
        f.add(userInterface);
        f.setSize(800, 800);
        Object option[] = {"black", "white"};
        humanColor = JOptionPane.showOptionDialog(null,
                "Black Or White?", "Input", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                option, option[1]);
        String stringInput = JOptionPane.showInputDialog("1. easy 2. medium 3.hard, >5 for depth >5");
        int number = Integer.parseInt(stringInput);
        if(number<3)
        {
        globalDepth=2*number;
        }
        else if(number==3)
        {
        globalDepth=5;
        }
        else{
        globalDepth=number;
        }
        f.setVisible(true);
        if (humanColor == 0) {
            humanColor = -1;
            String m = AlphaBetaTreePruning(Integer.MIN_VALUE, Integer.MAX_VALUE, "", -1, globalDepth, globalDepth);
            move(m);      
            flipBoard();
            f.repaint();
        }
    }
}
