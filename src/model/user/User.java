package model.user;

import model.game.Game;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private List<Game> games;
    private int points;

    public User() {
        this("-","-");
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.points = 0;
        this.games = new ArrayList<>();
    }
    public void addGame(Game game) {
        this.games.add(game);
    }

    public void removeGame(Game game) {
        this.games.remove(game);
    }

    public List<Game> getActiveGames() {
        return games;
    }
    public int getPoints(){
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
        if(this.points < 0)
            this.points = 0;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
