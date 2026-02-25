package model.pieces;

import model.board.Board;
import model.enums.Colors;
import model.user.Position;
import moveStrategies.KingMoveStrategy;

import java.util.List;

public class King extends Piece {
    public King(Colors color, Position position) {
        super(color, position, new KingMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> possibleMoves = getPossibleMoves(board);
        return possibleMoves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'K';
    }
}
