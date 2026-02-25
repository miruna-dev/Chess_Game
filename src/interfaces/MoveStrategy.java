package interfaces;

import model.board.Board;
import model.user.Position;

import java.util.List;

public interface MoveStrategy {
    List<Position> getPossibleMoves(Board board, Position from);
}
