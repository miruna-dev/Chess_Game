package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class BishopMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        // aflu piesa de la from si ii gasesc culoarea
        Piece bishop = board.getPieceAt(from);
        List<Position> possibleMoves = new ArrayList<Position>();

        int il, ok;
        char ic;
        ok = 1;
        // verific diaganoala dreapta sus
        for (il = from.y + 1, ic = (char) (from.x + 1); il <= 8 && ic <= 'H' && ok == 1; il++, ic++) {
            Piece piece = board.getPieceAt(new Position(ic, il));
            if (piece != null) {
                // am dat de o alta piesa si ma opresc
                ok = 0;
                // daca e piesa adversa o pot captura si adaug pozitia
                if (piece.getColor() != bishop.getColor())
                    possibleMoves.add(new Position(ic, il));
            } else {
                // nu exista piesa deci pot sa mut acolo
                possibleMoves.add(new Position(ic, il));
            }
        }
        ok = 1;
        // diagonala stanga jos
        for (il = from.y - 1, ic = (char) (from.x - 1); il >= 1 && ic >= 'A' && ok == 1; il--, ic--) {
            Piece piece = board.getPieceAt(new Position(ic, il));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != bishop.getColor())
                    possibleMoves.add(new Position(ic, il));
            } else {
                possibleMoves.add(new Position(ic, il));
            }
        }
        // diagonala stanga sus
        ok = 1;
        for (il = from.y + 1, ic = (char) (from.x - 1); il <= 8 && ic >= 'A' && ok == 1; il++, ic--) {
            Piece piece = board.getPieceAt(new Position(ic, il));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != bishop.getColor())
                    possibleMoves.add(new Position(ic, il));
            } else {
                possibleMoves.add(new Position(ic, il));
            }
        }
        ok = 1;
        // diagonala dreapta jos
        for (il = from.y - 1, ic = (char) (from.x + 1); il >= 1 && ic <= 'H' && ok == 1; il--, ic++) {
            Piece piece = board.getPieceAt(new Position(ic, il));
            if (piece != null) {
                ok = 0;
                if (piece.getColor() != bishop.getColor())
                    possibleMoves.add(new Position(ic, il));
            } else {
                possibleMoves.add(new Position(ic, il));
            }
        }
        return possibleMoves;
    }
}

