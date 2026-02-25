package moveStrategies;

import interfaces.MoveStrategy;
import model.board.Board;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy {
    public List<Position> getPossibleMoves(Board board, Position from) {
        // aflu piesa de la from si ii gasesc culoarea
        Piece knight = board.getPieceAt(from);
        List<Position> possibleMoves = new ArrayList<Position>();
        int[] dx = {1, -1, 1, -1, 2, 2, -2, -2};
        int[] dy = {2, 2, -2, -2, 1, -1, 1, -1};
        int i;
        // iau cele 8 mutari posibile ale calului
        for (i = 0; i < 8; i++) {
            Position new_pos = new Position((char) (from.x + dx[i]), from.y + dy[i]);
            // verific daca sunt in interiorul tablei
            if (new_pos.x <= 'H' && new_pos.x >= 'A' && new_pos.y <= 8 && new_pos.y >= 1)
                // verific daca am piesa acolo
                if (board.getPieceAt(new_pos) != null) {
                    // daca e de alta culoare pot sa o capturez
                    if (board.getPieceAt(new_pos).getColor() != knight.getColor())
                        possibleMoves.add(new_pos);
                } else {
                    // nu e piesa deci pot sa mut
                    possibleMoves.add(new_pos);
                }
        }
        return possibleMoves;
    }
}
