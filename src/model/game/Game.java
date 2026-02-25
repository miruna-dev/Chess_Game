package model.game;

import model.board.Board;
import model.board.ChessPair;
import model.enums.Colors;
import model.pieces.Piece;
import model.user.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Game {
    private int gameId;
    private Board board;
    private Player player1, player2;
    private List<Move> moves;
    private int currentPlayer;


    public Game(int gameId, Board board, Player player1, Player player2) {
        this.gameId = gameId;
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.moves = new ArrayList<Move>();
        this.currentPlayer = 0;
    }

    public Game(int gameId, Player player1, Player player2) {
        this.gameId = gameId;
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.moves = new ArrayList<Move>();
        this.currentPlayer = 0;
    }

    public int getId() {
        return gameId;
    }

    public void setId(int id) {
        this.gameId = id;
    }

    @Override
    public String toString() {
        return "Id joc : " + this.gameId;
    }

    public void start() {
        // pornesc un joc nou
        board.initialize();
        moves.clear();
        // albul incepe primul, deci setez playerul curent pe cel cu alb
        if (player1.getColor() == Colors.WHITE)
            currentPlayer = 1;
        else if (player2.getColor() == Colors.WHITE)
            currentPlayer = 2;
    }

    public void resume(Colors currentPlayerColor) {
        // incarc un joc deja pornit
        if (player1.getColor() == currentPlayerColor)
            currentPlayer = 1;
        else if (player2.getColor() == currentPlayerColor)
            currentPlayer = 2;
    }

    public void switchPlayer() {
        if (currentPlayer == 1)
            currentPlayer = 2;
        else if (currentPlayer == 2)
            currentPlayer = 1;
    }

    public boolean isInCheck(int playerIdx) {
        // determin playerul pe care il verific
        Player player;
        if (playerIdx == 1)
            player = player1;
        else
            player = player2;

        Colors color = player.getColor();

        // gasesc regele
        Position king = null;
        TreeSet<ChessPair<Position, Piece>> pieces = board.getPieces();
        var idx = pieces.iterator();

        while (idx.hasNext()) {
            var p = idx.next();
            if (p.getValue().type() == 'K' && p.getValue().getColor() == color) {
                king = p.getKey();
                break;
            }
        }
        // verific daca vreo piesa adversa ataca regele
        idx = pieces.iterator();
        while (idx.hasNext()) {
            Piece p = idx.next().getValue();
            // daca e piesa adversa
            if (p.getColor() != color) {
                // verific daca piesa ataca regele
                if (p.checkForCheck(board, king)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkForCheckMate() {

        // verific daca ma aflu in sah
        if (!isInCheck(currentPlayer)) {
            return false;
        }

        // aflu playerul curent
        Player player = player2;
        if (currentPlayer == 1)
            player = player1;

        TreeSet<ChessPair<Position, Piece>> pieces = board.getPieces();
        // fac o copie a tablei pentru a verifica daca pot sa ies din sah
        List<ChessPair<Position, Piece>> piecesCopy = new ArrayList<>();
        var idx = pieces.iterator();
        while (idx.hasNext()) {
            var p = idx.next();
            if (p.getValue().getColor() == player.getColor()) {
                piecesCopy.add(p);
            }
        }
        // parcurg toate mutarile posibile
        idx = piecesCopy.iterator();
        while (idx.hasNext()) {
            var pi = idx.next();
            Piece piece1 = pi.getValue();
            // pt fiecare piesa iau mutarile ei posibile
            List<Position> possiblePos = piece1.getPossibleMoves(board);
            for (Position p : possiblePos)
                // am gasit o mutare valida deci regele poate fi scos din sah
                if (board.isValidMove(pi.getKey(), p))
                    return false;

        }
        // regele nu mai poate fi mutat deci este in sah mat
        return true;
    }

    public int checkDrawStatus() {

        //intorc 0 daca nu e remiza
        // 1 daca e remiza prin PAT
        // 2 daca e remiza prin piese insuficiente
        // 3 daca e remiza prin repetare de mutari

        if (moves.size() >= 12) {
            int n = moves.size();

            Move m1 = moves.get(n - 1);
            Move m2 = moves.get(n - 2);
            Move m3 = moves.get(n - 3);
            Move m4 = moves.get(n - 4);

            Move m5 = moves.get(n - 5);
            Move m6 = moves.get(n - 6);
            Move m7 = moves.get(n - 7);
            Move m8 = moves.get(n - 8);

            Move m9 = moves.get(n - 9);
            Move m10 = moves.get(n - 10);
            Move m11 = moves.get(n - 11);
            Move m12 = moves.get(n - 12);

            if (m1.equals(m5) && m1.equals(m9) &&
                    m2.equals(m6) && m2.equals(m10) &&
                    m3.equals(m7) && m3.equals(m11) &&
                    m4.equals(m8) && m4.equals(m12)) {
                return 3;
            }
        }
        TreeSet<ChessPair<Position, Piece>> pieces = board.getPieces();
        // daca am mai putin de 3 piese pe tabla
        if (pieces.size() <= 3) {

            // daca am exact 2 inseamna ca am doar regii deci e remiza
            if (pieces.size() == 2) return 2;

            // daca am 3 piese inseamna ca am regii si inca o piesa
            // ca sa fie remiza trebuie ca a 3 a piesa sa fie cal/nebun
            if (pieces.size() == 3) {
                var idx = pieces.iterator();
                while (idx.hasNext()) {
                    Piece p = idx.next().getValue();
                    if (p.type() == 'N' || p.type() == 'B')
                        return 2;
                }
            }
        }
        // e sah mat sau jocul continua
        if (isInCheck(currentPlayer)) {
            return 0;
        }

        // aflu cine e playerul curent
        Player player = player2;
        if (currentPlayer == 1)
            player = player1;

        // daca regele nu este in sah
        // fac o copie a pieselor jucatorului curent ca sa verific daca am unde sa mut
        List<ChessPair<Position, Piece>> piecesCopy = new ArrayList<>();
        var idx = pieces.iterator();
        while (idx.hasNext()) {
            var p = idx.next();
            if (p.getValue().getColor() == player.getColor()) {
                piecesCopy.add(p);
            }
        }

        // verific daca exista vreo mutare posibila
        idx = piecesCopy.iterator();
        // parcurg piesele
        while (idx.hasNext()) {
            var p1 = idx.next();
            Piece piece1 = p1.getValue();
            List<Position> possiblePos = piece1.getPossibleMoves(board);
            for (Position p : possiblePos)
                // am gasit o mutare valida deci nu e remiza
                if (board.isValidMove(p1.getKey(), p))
                    return 0;
        }
        // nu am gasit nicio mutare valida deci e remiza
        return 1;
    }


    public void addMove(Player p, Position from, Position to, Piece capturedPiece) {
        Move move = new Move(p.getColor(), from, to, capturedPiece);
        moves.add(move);
    }

    public Board getBoard() {
        return board;
    }

    public int getGameId() {
        return gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
