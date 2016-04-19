/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author Arslan Memon
 */
public class rateBoard {
    //boards to give positional ranking to each piece
    //boards found on http://chessprogramming.wikispaces.com/Simplified+evaluation+function
    static int pawnBoard[][]={
        {   0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}
    };
    static int rookBoard[][]={
        {0,  0,  0,  0,  0,  0,  0,  0},
        {5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {0,  0,  0,  5,  5,  0,  0,  0}
    };
    public static int bishopBoard[][]={
        {20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}
    };
        
    public static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        {-5,  0,  5,  5,  5,  5,  0, -5},
        {0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}
    };
    public static int kingBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        {20, 20,  0,  0,  0,  0, 20, 20},
        {20, 30, 10,  0,  0, 10, 30, 20}
    };
    public static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
    };
    
    public static int rateBoard() {
        int score=0;
        score+= ratePieces();
        score+=ratePositionAndMovability();
        Chess.flipBoard();
        score-=ratePieces();
        score-=ratePositionAndMovability();
        Chess.flipBoard();
        return -1*score;
    }
    public static int ratePieces(){
        //ranks board based on material
    int score=0; 
    int bishops=0;
     for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (Chess.chessboard[i][j]) {
                    case "wP":
                        score += 100;
                        break;
                    case "wR":
                        score += 500;
                        break;
                    case "wB":
                        bishops += 1;
                        break;
                    case "wN":
                        score += 300;
                        break;
                    case "wK":
                        score += 0;
                        break;
                    case "wQ":
                        score += 1000;
                        break;
                }
            }
        }
     if(bishops==2){
     score+=350*bishops;
     }
     else{
     score+=225*bishops;
     }
        return score;
    }
    public static int ratePositionAndMovability(){
    int score=0;//ranks based on position of pieces and how many places each can move
    String movesPossible=Chess.movesPossible();
    if (movesPossible.length()==0){return -200000000;}
    for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (Chess.chessboard[i][j]) {
                    case "wP":
                        score += pawnBoard[i][j]+(Chess.possiblePawnMoves(i, j).length()*10/6);
                        break;
                    case "wR":
                        score += rookBoard[i][j]+(Chess.possibleRookMoves(i, j).length()*30/6);
                        break;
                    case "wB":
                        score += rookBoard[i][j]+(Chess.possibleBishopMoves(i, j).length()*30/5);
                        break;
                    case "wN":
                        score += knightBoard[i][j]+(Chess.possibleKnightMoves(i, j).length()*80/5);
                        break;
                    case "wK":
                        score += kingBoard[i][j]+(Chess.possibleKingMoves(i, j).length()*10/5);
                        break;
                    case "wQ":
                        score += queenBoard[i][j]+(Chess.possibleQueenMoves(i, j).length()*50/5);
                        break;
                }
            }
        }
        return score;
    }
}
