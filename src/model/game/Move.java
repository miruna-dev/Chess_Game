package model.game;

import model.enums.Colors;
import model.pieces.Piece;
import model.user.Position;

public class Move {
    private Colors color;
    private Position from;
    private Position to;
    private Piece capturedPiece;

    public Move(Colors color, Position from, Position to) {
        this.color = color;
        this.from = from;
        this.to = to;
        capturedPiece = null;
    }

    public Move(Colors color, Position from, Position to, Piece capturedPiece) {
        this.color = color;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
    }

    public Colors getColor() {
        return color;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setPlayerColor(Colors color) {
        this.color = color;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public void setTo(Position to) {
        this.to = to;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

}
