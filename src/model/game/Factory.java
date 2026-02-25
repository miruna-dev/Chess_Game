package model.game;

import model.enums.Colors;
import model.pieces.*;
import model.user.Position;

public class Factory {
    public static Piece createPiece(char type, Colors color, Position position) {
        char upperType = Character.toUpperCase(type);

        switch (upperType) {
            case 'P':
                return new Pawn(color, position);
            case 'R':
                return new Rook(color, position);
            case 'N':
                return new Knight(color, position);
            case 'B':
                return new Bishop(color, position);
            case 'Q':
                return new Queen(color, position);
            case 'K':
                return new King(color, position);
            default:
                return null;
        }
    }
}
