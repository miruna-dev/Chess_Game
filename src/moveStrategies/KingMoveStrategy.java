package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class KingMoveStrategy implements MoveStrategy {
    public List<Position> getPossibleMoves(Board board, Position from) {
        // aflu piesa de la from si ii gasesc culoarea
        Piece king = board.getPieceAt(from);
        List<Position> possibleMoves = new ArrayList<Position>();
        int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
        int i;
        // iau cele 8 pozitii psibile
        for (i = 0; i < 8; i++) {
            Position new_pos = new Position((char) (from.x + dx[i]), from.y + dy[i]);
            // verific daca e in interiorul tablei
            if (new_pos.x <= 'H' && new_pos.x >= 'A' && new_pos.y <= 8 && new_pos.y >= 1)
                // verific daca am piesa
                if (board.getPieceAt(new_pos) != null) {
                    // daca am si e de culoare adversa atunci pot sa o iau si adaug mutarea
                    if (board.getPieceAt(new_pos).getColor() != king.getColor())
                        possibleMoves.add(new_pos);
                } else {
                    // daca nu am piesa pot sa mut acolo
                    possibleMoves.add(new_pos);
                }
        }
        return possibleMoves;
    }
}
