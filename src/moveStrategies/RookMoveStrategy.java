package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class RookMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        // aflu piesa de la from si ii gasesc culoarea
        Piece rook = board.getPieceAt(from);
        List<Position> possibleMoves = new ArrayList<Position>();

        int i, ok;
        ok = 1;
        // sus
        for (i = from.y + 1; i <= 8 && ok == 1; i++) {
            Piece piece = board.getPieceAt(new Position(from.x, i));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != rook.getColor())
                    possibleMoves.add(new Position(from.x, i));
            } else {
                possibleMoves.add(new Position(from.x, i));
            }
        }

        ok = 1;
        // jos
        for (i = from.y - 1; i >= 1 && ok == 1; i--) {
            Piece piece = board.getPieceAt(new Position(from.x, i));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != rook.getColor())
                    possibleMoves.add(new Position(from.x, i));
            } else {
                possibleMoves.add(new Position(from.x, i));
            }
        }

        char j;
        ok = 1;
        // dreapta
        for (j = (char) (from.x + 1); j <= 'H' && ok == 1; j++) {
            Piece piece = board.getPieceAt(new Position(j, from.y));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != rook.getColor())
                    possibleMoves.add(new Position(j, from.y));
            } else {
                possibleMoves.add(new Position(j, from.y));
            }
        }

        ok = 1;
        // stanga
        for (j = (char) (from.x - 1); j >= 'A' && ok == 1; j--) {
            Piece piece = board.getPieceAt(new Position(j, from.y));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != rook.getColor())
                    possibleMoves.add(new Position(j, from.y));
            } else {
                possibleMoves.add(new Position(j, from.y));
            }
        }
        return possibleMoves;
    }
}

