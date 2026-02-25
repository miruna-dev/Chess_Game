package model.game;

import exceptions.InvalidMoveException;
import model.board.Board;
import model.board.ChessPair;
import model.enums.Colors;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    private String email;
    private Colors color;
    private List<Piece> capturedPieces;
    private TreeSet<ChessPair<Position, Piece>> availablePieces;
    private int points;
    private Piece lastPiece = null;
    private String alias;

    public Player(String email, Colors color) {
        this.email = email;
        this.color = color;
        this.points = 0;
        this.capturedPieces = new ArrayList<>();
        this.availablePieces = new TreeSet<>();
        this.alias = email;
    }

    public void makeMove(Position from, Position to, Board board, char promotion) throws InvalidMoveException {
        // resetez la null in cazul in care nu e capturata nicio piesa
        this.lastPiece = null;
        // salvez piesa de la to daca exista
        Piece capturedPiece = board.getPieceAt(to);
        // fac mutarea
        board.movePiece(from, to, promotion);
        // daca am capturat piesa de la to o pun in lista de piese capturate
        if (capturedPiece != null && capturedPiece.getColor() != color) {
            capturedPieces.add(capturedPiece);
            // adaug punctele pentru piesa capturata
            this.lastPiece = capturedPiece;
            points = points + getPieceValue(capturedPiece);
        }
    }

    // intoarce numarul de puncte pentru tipul de piesa capturata
    public int getPieceValue(Piece capturedPiece) {
        if (capturedPiece.type() == 'Q')
            return 90;
        if (capturedPiece.type() == 'R')
            return 50;
        if (capturedPiece.type() == 'B')
            return 30;
        if (capturedPiece.type() == 'N')
            return 30;
        if (capturedPiece.type() == 'P')
            return 10;
        return 0;
    }

    public Piece getLastCapturedPiece() {
        return this.lastPiece;
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getAlias() {
        return alias;
    }

    public Colors getColor() {
        return color;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

}
