package model.user;

public class Position implements Comparable<Position>{

    public char x;
    public int y;

    public Position(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        if (o instanceof Position && o != null) {
            Position p = (Position) o;
            return (this.x == p.x) && (this.y == p.y);
        }
        return false;
    }

    public String toString() {
        String s = "";
        s = s + x + y;
        return s;
    }
    public int compareTo(Position o) {
        if(this.y==o.y)
            return this.x - o.x;
        else
            return this.y - o.y;
    }
}
