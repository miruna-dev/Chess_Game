package model.board;

import exceptions.InvalidMoveException;
import model.enums.Colors;
import model.game.Factory;
import model.pieces.Piece;
import model.pieces.*;
import model.user.Position;

import java.awt.*;
import java.util.List;
import java.util.TreeSet;

public class Board {
    private TreeSet<ChessPair<Position, Piece>> board = new TreeSet<>();

    public void initialize() {

        // ma asigur ca tabla nu are piese
        this.board.clear();

        // adaug piesele albe
        this.board.add(new ChessPair<>(new Position('A', 1), Factory.createPiece('R', Colors.WHITE, new Position('A', 1))));
        this.board.add(new ChessPair<>(new Position('B', 1), Factory.createPiece('N', Colors.WHITE, new Position('B', 1))));
        this.board.add(new ChessPair<>(new Position('C', 1), Factory.createPiece('B', Colors.WHITE, new Position('C', 1))));
        this.board.add(new ChessPair<>(new Position('D', 1), Factory.createPiece('Q', Colors.WHITE, new Position('D', 1))));
        this.board.add(new ChessPair<>(new Position('E', 1), Factory.createPiece('K', Colors.WHITE, new Position('E', 1))));
        this.board.add(new ChessPair<>(new Position('F', 1), Factory.createPiece('B', Colors.WHITE, new Position('F', 1))));
        this.board.add(new ChessPair<>(new Position('G', 1), Factory.createPiece('N', Colors.WHITE, new Position('G', 1))));
        this.board.add(new ChessPair<>(new Position('H', 1), Factory.createPiece('R', Colors.WHITE, new Position('H', 1))));

        // adaug pionii albi
        char i;
        for (i = 'A'; i <= 'H'; i++)
            this.board.add(new ChessPair<>(new Position(i, 2), Factory.createPiece('P', Colors.WHITE, new Position(i, 2))));

        // adaug pisele negre
        this.board.add(new ChessPair<>(new Position('A', 8), Factory.createPiece('R', Colors.BLACK, new Position('A', 8))));
        this.board.add(new ChessPair<>(new Position('B', 8), Factory.createPiece('N', Colors.BLACK, new Position('B', 8))));
        this.board.add(new ChessPair<>(new Position('C', 8), Factory.createPiece('B', Colors.BLACK, new Position('C', 8))));
        this.board.add(new ChessPair<>(new Position('D', 8), Factory.createPiece('Q', Colors.BLACK, new Position('D', 8))));
        this.board.add(new ChessPair<>(new Position('E', 8), Factory.createPiece('K', Colors.BLACK, new Position('E', 8))));
        this.board.add(new ChessPair<>(new Position('F', 8), Factory.createPiece('B', Colors.BLACK, new Position('F', 8))));
        this.board.add(new ChessPair<>(new Position('G', 8), Factory.createPiece('N', Colors.BLACK, new Position('G', 8))));
        this.board.add(new ChessPair<>(new Position('H', 8), Factory.createPiece('R', Colors.BLACK, new Position('H', 8))));

        // adaug pionii negri
        for (i = 'A'; i <= 'H'; i++)
            this.board.add(new ChessPair<>(new Position(i, 7), Factory.createPiece('P', Colors.BLACK, new Position(i, 7))));
    }

    public void movePiece(Position from, Position to, char upgradeTo) throws InvalidMoveException {
        // daca mutarea e valida
        if (isValidMove(from, to)) {
            var idx = board.iterator();
            int ok = 1;
            Piece piece = null;

            // gasesc piesa la from
            // o salvez in piece
            while (idx.hasNext() && ok == 1) {
                var p = idx.next();
                if (p.getKey().equals(from)) {
                    piece = p.getValue();
                    // sterg piesa de la from
                    idx.remove();
                    ok = 0;
                }
            }
            // caut piesa de la to si o sterg
            idx = board.iterator();
            ok = 1;
            while (idx.hasNext() && ok == 1) {
                var p = idx.next();
                if (p.getKey().equals(to)) {
                    idx.remove();
                    ok = 0;
                }
            }

            // setez noua pozitie a piesei
            piece.setPosition(to);
            // adaug piesa inapoi pe tabla la noua pozitie
            board.add(new ChessPair<>(to, piece));

            // daca e pion
            if (piece.type() == 'P') {
                // verific daca a ajuns la capat de rand, in functie de culoare
                if (piece.getColor() == Colors.WHITE && to.y == 8) {
                    // creez piesa aleasa de jucator
                    Piece newPiece = Factory.createPiece(upgradeTo, piece.getColor(), to);
                    // sterg pionul
                    board.remove(new ChessPair<>(to, piece));
                    // pun piesa care il inlocuieste
                    board.add(new ChessPair<>(to, newPiece));

                } else if (piece.getColor() == Colors.BLACK && to.y == 1) {
                    Piece newPiece = Factory.createPiece(upgradeTo, piece.getColor(), to);
                    // sterg pionul
                    board.remove(new ChessPair<>(to, piece));
                    // pun piesa care il inlocuieste
                    board.add(new ChessPair<>(to, newPiece));
                }
            }
        } else {
            throw new InvalidMoveException("Mutare invalida de la " + from + " la " + to);
        }
    }

    public void moveTemporary(Position from, Position to) {
        // mut temporar piesa
        // ajuta la isValidMove, ca dupa sa verific daca regele ramane in sah daca fac mutarea
        var idx = this.board.iterator();
        int ok = 1;
        Piece piece = null;
        // caut piesa de la from si o sterg
        while (idx.hasNext() && ok == 1) {
            var p = idx.next();
            if (p.getKey().equals(from)) {
                piece = p.getValue();
                idx.remove();
                ok = 0;
            }
        }
        idx = this.board.iterator();
        ok = 1;
        // daca exista piesa la from
        if (piece != null) {
            // sterg piesa de la to
            while (idx.hasNext() && ok == 1) {
                var p = idx.next();
                if (p.getKey().equals(to)) {
                    idx.remove();
                    ok = 0;
                }
            }
            // setez noua pozitie a piesei
            piece.setPosition(to);
            // adaug piesa inapoi pe tabla la noua pozitie
            board.add(new ChessPair<>(to, piece));
        }
    }

    public Piece getPieceAt(Position position) {
        var idx = this.board.iterator();
        while (idx.hasNext()) {
            var p = idx.next();
            if (p.getKey().equals(position)) {
                return p.getValue();
            }
        }
        return null;
    }

    public boolean isValidMove(Position from, Position to) {

        var idx = this.board.iterator();
        int ok = 1;
        Piece piece = null;
        Piece king = null;
        // gasesc piesa pe care vreau sa o mut
        while (idx.hasNext() && ok == 1) {
            var p = idx.next();
            if (p.getKey().equals(from)) {
                piece = p.getValue();
                ok = 0;
            }
        }
        // daca nu exista piesa la pozitia respectiva mutarea e invalida
        if (piece == null)
            return false;
        // verific daca e posibil ca piesa sa mearga acolo
        // adica daca respecta regula de cum se muta piesa respectiva
        int valid = 0;
        // iterez prin toate pozitiile posibile
        List<Position> positionList = piece.getPossibleMoves(this);
        for (int i = 0; i < positionList.size(); i++)
            if (positionList.get(i).equals(to))
                valid = 1;
        // daca nu merge atunci returnez 0
        if (valid == 0)
            return false;

        // fac mutarea temporar ca sa vad daca imi ramane regele in sah
        // salvez piesa de la to, pentru ca movetemporary o sterge
        // si daca vreau sa dau undo la mutare trebuie sa pun piesa inapoi la to
        Piece capturedPiece = getPieceAt(to);
        // fac mutarea temporara
        moveTemporary(from, to);

        idx = this.board.iterator();
        ok = 1;
        while (idx.hasNext() && ok == 1) {
            var p = idx.next();
            // caut regele
            if (p.getValue().type() == 'K' && p.getValue().getColor() == piece.getColor()) {
                king = p.getValue();
                ok = 0;
            }
        }

        // verific daca regele e in sah dupa mutare
        idx = board.iterator();
        ok = 1;
        while (idx.hasNext() && ok == 1) {
            var p = idx.next();
            if (p.getValue().getColor() != piece.getColor()) {
                // iau piesele adversarului si verific daca imi dau sah
                if (p.getValue().checkForCheck(this, king.getPosition()) == true) {
                    valid = 0;
                    ok = 0;
                }
            }
        }
        // mut inapoi
        moveTemporary(to, from);
        // adaug inapoi piesa de la to, daca exista piesa inainte
        if (capturedPiece != null)
            board.add(new ChessPair<>(to, capturedPiece));
        if (valid == 0)
            return false;

        return true;
    }

    public TreeSet<ChessPair<Position, Piece>> getPieces() {
        return this.board;
    }

    public boolean isKingInCheck(Position kingPos, Colors kingColor) {
        // parcurg piesele de pe tabla
        var idx = this.getPieces().iterator();
        while (idx.hasNext()) {
            // extrag piesa curenta
            Piece p = idx.next().getValue();

            // verific daca apartine advresarului
            if (p.getColor() != kingColor) {
                // verific daca da sah la rege
                if (p.checkForCheck(this, kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }
}
