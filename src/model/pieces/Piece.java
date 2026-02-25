package model.pieces;

import interfaces.ChessPiece;
import interfaces.MoveStrategy;
import model.board.Board;
import model.enums.Colors;
import model.user.Position;

import java.util.List;

abstract public class Piece implements ChessPiece {
    private final Colors color;
    private Position position;
    private MoveStrategy moveStrategy;

    public Piece(Colors color, Position pos,  MoveStrategy strategy) {
        this.color = color;
        this.position = pos;
        this.moveStrategy = strategy;
    }
    public Colors getColor(){
        return color;
    }
    public Position getPosition(){
        return position;
    }
    public void setPosition(Position new_pos){
        this.position = new_pos;
    }

    public List<Position> getPossibleMoves(Board board) {
        return this.moveStrategy.getPossibleMoves(board, this.position);
    }
}
