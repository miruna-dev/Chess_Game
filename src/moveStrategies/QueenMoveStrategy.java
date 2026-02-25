package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class QueenMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> possibleMoves = new ArrayList<Position>();

        // folosesc strategia de la tura
        RookMoveStrategy rookStrategy = new RookMoveStrategy();
        List<Position> rookMoves = rookStrategy.getPossibleMoves(board, from);
        possibleMoves.addAll(rookMoves);

        // folosesc strategia de la nebun
        BishopMoveStrategy bishopStrategy = new BishopMoveStrategy();
        List<Position> bishopMoves = bishopStrategy.getPossibleMoves(board, from);
        possibleMoves.addAll(bishopMoves);

        return possibleMoves;
    }
}