package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;

        //Adding a brand-new board with pieces
        board.resetBoard();
    }

    //DEEP COPY
    public ChessBoard boardCopy (){
        ChessBoard boarCopy = new ChessBoard();
        for(int i = 1; i < 9;i++){
            for(int j = 1; j < 9;j++){
                boarCopy.addPiece(new ChessPosition(i,j),board.getPiece(new ChessPosition(i,j)));
            }
        }
        return boarCopy;
    }

    //Deep Copy of ChessGame
    public  ChessGame chessGameCopy (){
        ChessGame chessGameCopy = new ChessGame();
        chessGameCopy.setTeamTurn(teamTurn);
        chessGameCopy.setBoard(boardCopy());
        return chessGameCopy;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn =  team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //The final list of moves if they are valid
        Collection finalList = new ArrayList<>();

        //Piece
        ChessPiece piece  = board.getPiece(startPosition);
        //Checking if there is no piece
        if(piece == null){
            return  null;
        }
        //If there is a piece get its list of possible moves
        Collection<ChessMove> moves = piece.pieceMoves(board,startPosition);

        for(ChessMove a: moves){
            //Making a copy of ChessGame to make moves inside
            ChessGame tempChessGameCopy = chessGameCopy();
            //Making the move inside my copy
            tempChessGameCopy.justMakeMove(a);
            //Check if king is in Check
            boolean isKingGood = tempChessGameCopy.isInCheck(piece.getTeamColor());
            //If king is safe add to my finalList
            if(!isKingGood){
                finalList.add(a);
            }
        }
        //Returning my final List of Valid moves
        return finalList;
    }

    //Helper Function - This function makes the move without checking
    public void justMakeMove(ChessMove a){
        ChessPiece temPiece = board.getPiece(a.getStartPosition());
        //Update the board
        board.addPiece(a.getEndPosition(),temPiece);
        //Make previous location to null
        board.addPiece(a.getStartPosition(),null);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startingPosition = move.getStartPosition();
        ChessPiece piece = board.getPiece(startingPosition);

        //Making sure that there is a piece there
        if(piece == null){
            throw new InvalidMoveException("No piece to move");
        }

        //If there is a piece then do the following
        Collection<ChessMove> moves = validMoves(startingPosition);
        if(moves.contains(move)){
            for(ChessMove a: moves){
                ChessPiece temPiece = board.getPiece(a.getStartPosition());
                //Update the board
                board.addPiece(a.getEndPosition(),temPiece);
                //Make previous location to null
                board.addPiece(a.getStartPosition(),null);

                //Updating the team turn
                if(teamTurn == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                }else{
                    setTeamTurn(TeamColor.WHITE);
                }
            }
        }else{
            throw  new InvalidMoveException("Invalid Move");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
            //Initializing kings position
            ChessPosition kingPosition = null;
            boolean kingInCheck = false;

            //Finding the king's position
            for(int i= 1; i< 9; i++){
                for(int j = 1; j < 9; j++){
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if(piece == null){continue;}
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                        kingPosition = new ChessPosition(i,j);
                    }
                }
            }
            //Loop to check if opponent pieces have the position of the king as possible move
            for(int i= 1; i< 9; i++){
                for(int j = 1; j < 9; j++){
                    //Getting each piece
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if(piece == null){continue;}
                    //Checking if it is an opponent
                    if(piece.getTeamColor() != teamColor){
                        //I want to use the function pieceMoves from my ChessPiece Class
                       Collection<ChessMove> possibleMove =  piece.pieceMoves(board,new ChessPosition(i,j));

                       Collection<ChessPosition> endPositions = new ArrayList<>();
                       for(ChessMove move : possibleMove){
                           endPositions.add(move.getEndPosition());
                       }

                        //I want to check if King's position is inside the opponent possible moves
                            if(endPositions.contains(kingPosition)){
                                kingInCheck = true;
                            }
                    }
                }
            }
        return kingInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
