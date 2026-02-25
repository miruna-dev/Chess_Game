package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.enums.Colors;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveStrategy implements MoveStrategy {
    public List<Position> getPossibleMoves(Board board, Position from) {
        // aflu piesa de la from si ii gasesc culoarea
        Piece pawn = board.getPieceAt(from);

        // verific daca piesa exista
        if (pawn == null) {
            return new ArrayList<>();
        }

        List<Position> possibleMoves = new ArrayList<Position>();
        // daca pionul are voie sa mearga inainte
        int valid = 0;
        // retin linia urmatoare fara sa modific pozitia curenta
        int nY = 0;

        // daca e alb merge in sus, y creste
        if (pawn.getColor() == Colors.WHITE) {
            // ca sa mut 2 patratele verific daca sunt la linia de start si daca nu am piesa inainte cu 1 si 2 pozitii
            if (from.y == 2 &&
                    board.getPieceAt(new Position(from.x, from.y + 1)) == null &&
                    board.getPieceAt(new Position(from.x, from.y + 2)) == null) {
                possibleMoves.add(new Position(from.x, from.y + 2));
            }
            // coordonata pentru pasul inainte
            // ca sa verific mutarile posibile de pe randul din fata
            if (from.y + 1 <= 8) {
                nY = from.y + 1;
                // nu sunt pe ultimul rand
                valid = 1;
            }
        } else if (pawn.getColor() == Colors.BLACK) {
            // daca e negru merge in jos
            // ca sa mut 2 patratele verific daca sunt la linia de start si daca nu am piesa inainte cu 1 si 2 pozitii
            if (from.y == 7 &&
                    board.getPieceAt(new Position(from.x, from.y - 1)) == null &&
                    board.getPieceAt(new Position(from.x, from.y - 2)) == null) {
                possibleMoves.add(new Position(from.x, from.y - 2));
            }
            // coordonata pentru pasul inainte
            // ca sa verific mutarile posibile de pe randul din fata
            if (from.y - 1 >= 1) {
                nY = from.y - 1;
                // nu sunt pe ultimul rand
                valid = 1;
            }
        }

        // pot sa mut in fata
        if (valid == 1) {
            // daca nu am piesa in fata adaug mutarea
            if (board.getPieceAt(new Position(from.x, nY)) == null)
                possibleMoves.add(new Position(from.x, nY));

            // verific daca am piese in lateral, daca da pot sa mut acolo si sa capturez piesa
            if (from.x + 1 <= 'H') {
                Position capturePos = new Position((char)(from.x + 1), nY);
                Piece piece = board.getPieceAt(capturePos);
                // daca e piesa adversa adaug mutarea
                if (piece != null && piece.getColor() != pawn.getColor())
                    possibleMoves.add(capturePos);
            }
            if (from.x - 1 >= 'A') {
                Position capturePos = new Position((char)(from.x - 1), nY);
                Piece piece = board.getPieceAt(capturePos);
                // daca e piesa adversa adaug mutarea
                if (piece != null && piece.getColor() != pawn.getColor())
                    possibleMoves.add(capturePos);
            }
        }
        return possibleMoves;
    }
}