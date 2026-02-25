package model.pieces;

import model.board.Board;
import model.enums.Colors;
import model.user.Position;
import moveStrategies.RookMoveStrategy;

import java.util.List;

public class Rook extends Piece {

    public Rook(Colors color, Position position) {
        super(color, position,new RookMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // generez toate mutarile posibile si verific daca regele e printre ele
        List<Position> possibleMoves = getPossibleMoves(board);
        return possibleMoves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'R';
    }
}

