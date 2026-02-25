package model.pieces;

import model.board.Board;
import model.user.Position;
import model.enums.Colors;
import moveStrategies.PawnMoveStrategy;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(Colors color, Position position) {
        super(color, position, new PawnMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // generez toate mutarile posibile si verific daca regele e printre ele
        List<Position> possibleMoves = getPossibleMoves(board);
        return possibleMoves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'P';
    }
}
