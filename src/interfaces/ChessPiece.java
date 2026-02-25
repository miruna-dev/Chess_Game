package interfaces;

import model.board.Board;
import model.user.Position;

import java.util.List;

public interface ChessPiece {

    List<Position> getPossibleMoves(Board board);

    boolean checkForCheck(Board board, Position kingPosition);

    char type();
}
