package main;

import model.enums.Colors;
import model.game.Game;
import model.game.Move;
import model.game.Player;
import model.pieces.Piece;

import model.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.ChessFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {

    private List<User> users = new ArrayList<>();
    private Map<Integer, Game> games = new HashMap<>();
    private User currentUser;
    private boolean dataLoaded = false;
    private static Main instance = null;

    private Main() {
        users = new ArrayList<>();
        games = new HashMap<>();
        currentUser = null;
        dataLoaded = false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public void logout() {
        this.currentUser = null;
    }

    public List<Game> getUserActiveGames() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        return currentUser.getActiveGames();
    }

    public void removeGame(Game game) {
        if (currentUser != null) {
            currentUser.removeGame(game);
            games.remove(game.getGameId());
        }
    }

    public void read() {
        try {
            //citesc jocurile
            games = JsonReaderUtil.readGamesAsMap(Paths.get("games.json"));
            // parcurg mutarile din fiecare joc
            Iterator<Game> idx = games.values().iterator();
            while (idx.hasNext()) {
                Game g = idx.next();
                Iterator<Move> moveIdx = g.getMoves().iterator();
                while (moveIdx.hasNext()) {
                    Move m = moveIdx.next();
                    // daca mutarea are o piesa capturata
                    if (m.getCapturedPiece() != null) {
                        Player player = null;
                        // gasesc playerul care a mutat
                        if (g.getPlayer1().getColor() == m.getColor())
                            player = g.getPlayer1();
                        else
                            player = g.getPlayer2();

                        // adaug piesa in lista jucatorului
                        player.getCapturedPieces().add(m.getCapturedPiece());
                    }
                }
            }

            // citesc utilizatorii
            users = JsonReaderUtil.readUsers(Paths.get("accounts.json"));

            linkGames(Paths.get("accounts.json"));
            dataLoaded = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void linkGames(Path path) {
        JSONParser parser = new JSONParser();
        // folosesc bufferedreader ca sa citesc blocuri mai mari de text
        try (Reader reader = Files.newBufferedReader(path)) {
            // parse returneaza niste obiecte
            // fac cast la JSONArray
            JSONArray accountsArray = (JSONArray) parser.parse(reader);

            var idx = accountsArray.iterator();
            // parcurg toate conturile
            while (idx.hasNext()) {
                var item = idx.next();

                JSONObject obj = (JSONObject) item;
                // iau emailul
                String email = (String) obj.get("email");

                // gasesc userul corespunzator emailului
                User user = findUser(email);

                if (user != null) {
                    JSONArray gamesIds = (JSONArray) obj.get("games");
                    // parcurg lista de id-uri de jocuri ale utilizatorului curent
                    if (gamesIds != null) {
                        var gamesIterator = gamesIds.iterator();
                        while (gamesIterator.hasNext()) {
                            Object id = gamesIterator.next();
                            // json-simple stocheaza automat nr citit ca long
                            // nu se poate transforma direct din long in int
                            // fac cast la number care e clasa parinte a lui long si care contine si int, float etc
                            // folosesc metoda intValue care returneaza un int
                            int gameId = ((Number) id).intValue();

                            // in map caut game-ul cu id-ul dat
                            Game game = games.get(gameId);
                            if (game != null) {
                                // atribui jocul utilizatorului
                                user.addGame(game);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User findUser(String email) {
        var idx = users.iterator();
        // parcurg lista de useri si intorc userul cu mailul dat
        int ok = 1;
        User user = null;
        while (idx.hasNext() && ok == 1) {
            User u = idx.next();
            if (u.getEmail().equals(email)) {
                user = u;
                ok = 0;
            }
        }
        return user;
    }

    public void write() {
        if (!dataLoaded) {
            return;
        }
        // pun jocurile in games.json
        writeGames(Paths.get("games.json"));
        // pun conturile in accounts.json
        writeUsers(Paths.get("accounts.json"));
    }

    public void writeUsers(Path path) {
        // fac o lista cu toti utilizatorii pe care dupa o pun in fisier
        // folosesc lista ca java sa se ocupe automat de punctuatia din fisier
        JSONArray usersList = new JSONArray();

        // iau fiecare user
        var idx = users.iterator();
        while (idx.hasNext()) {
            User u = idx.next();
            // fac un obiect pe care sa il pot pune in JSON
            JSONObject userObj = new JSONObject();

            userObj.put("email", u.getEmail());
            userObj.put("password", u.getPassword());
            userObj.put("points", u.getPoints());

            // fac lista de jocuri corespunzatoare userului
            JSONArray gamesIds = new JSONArray();
            // iau jocurile active
            List<Game> activeGames = u.getActiveGames();
            if (activeGames != null) {
                var gameIdx = activeGames.iterator();
                while (gameIdx.hasNext()) {
                    // pun in lista doar id-ul jocului
                    Game g = gameIdx.next();
                    gamesIds.add(g.getGameId());
                }
            }
            // pun lista creata de jocuri in obiectul utilizatorului
            userObj.put("games", gamesIds);

            // adaug userul in lista de useri
            usersList.add(userObj);
        }

        try (FileWriter file = new FileWriter(path.toString())) {
            // scriu lista de utilizatori in fisier
            file.write(usersList.toJSONString());
        } catch (IOException e) {
            System.out.println("Eroare la scrierea utilizatorilor: " + e.getMessage());
        }
    }

    public void writeGames(Path path) {
        // fac lista cu jocuri
        // folosesc lista ca java sa se ocupe automat de punctuatia din fisier
        JSONArray gamesList = new JSONArray();

        // parcurg toate jocurile
        var idx = games.values().iterator();
        while (idx.hasNext()) {
            Game g = idx.next();
            // le fac de tip obiect
            JSONObject gameObj = new JSONObject();

            gameObj.put("id", g.getGameId());
            String color;
            if (g.getCurrentPlayer() == 1) color = "WHITE";
            else color = "BLACK";

            gameObj.put("currentPlayerColor", color);

            JSONArray playersArr = new JSONArray();
            Player player1 = g.getPlayer1();
            JSONObject obj1 = new JSONObject();
            obj1.put("email", player1.getEmail());
            obj1.put("alias", player1.getAlias());
            obj1.put("color", player1.getColor().toString());
            playersArr.add(obj1);
            Player player2 = g.getPlayer2();
            JSONObject obj2 = new JSONObject();
            obj2.put("email", player2.getEmail());
            obj2.put("alias", player2.getAlias());
            obj2.put("color", player2.getColor().toString());
            playersArr.add(obj2);
            gameObj.put("players", playersArr);

            // fac o lista cu tabla la momentul dat
            JSONArray boardArr = new JSONArray();

            // parcurg toate piesele de pe tabla
            var piecesIdx = g.getBoard().getPieces().iterator();
            while (piecesIdx.hasNext()) {
                var pair = piecesIdx.next();
                Piece p = pair.getValue();

                // fac piesa de tip obiect
                JSONObject pieceObj = new JSONObject();
                pieceObj.put("type", String.valueOf(p.type()));
                pieceObj.put("color", p.getColor().toString());
                pieceObj.put("position", p.getPosition().toString());

                // adug piesa in lista de piese
                boardArr.add(pieceObj);
            }
            // adug tabla in obiectul game
            gameObj.put("board", boardArr);

            // fac o lista de mutari
            JSONArray movesArr = new JSONArray();
            var moveIdx = g.getMoves().iterator();
            // parcurg mutarile
            while (moveIdx.hasNext()) {
                Move m = moveIdx.next();
                // fac un obiect pentru fiecare mutare
                JSONObject moveObj = new JSONObject();
                moveObj.put("playerColor", m.getColor().toString());
                moveObj.put("from", m.getFrom().toString());
                moveObj.put("to", m.getTo().toString());

                // adug si piesa capturata daca exista
                if (m.getCapturedPiece() != null) {
                    JSONObject capturedObj = new JSONObject();
                    capturedObj.put("type", String.valueOf(m.getCapturedPiece().type()));
                    capturedObj.put("color", m.getCapturedPiece().getColor().toString());
                    moveObj.put("captured", capturedObj);
                }

                // adug mutarea in lista
                movesArr.add(moveObj);
            }

            // adaug mutarile in obiectul jocului
            gameObj.put("moves", movesArr);

            // adug obiectul in lista de obiecte
            gamesList.add(gameObj);
        }

        try (FileWriter file = new FileWriter(path.toString())) {
            // scriu lista de jocuri in JSON
            file.write(gamesList.toJSONString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public User login(String email, String password) {

        // setez userul curent
        var idx = users.iterator();
        while (idx.hasNext()) {
            User u = idx.next();
            // verific parola
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                this.currentUser = u;
                return u;
            }
        }
        return null;
    }

    public User newAccount(String email, String password) {
        // verific daca exista deja utilizatorul
        var idx = users.iterator();
        while (idx.hasNext()) {
            User u = idx.next();
            if (u.getEmail().equals(email)) {
                System.out.println("Exista deja un cont cu acest email!");
                return null;
            }
        }
        // creez contul pentru ca nu exista
        User newUser = new User(email, password);
        this.users.add(newUser);
        this.currentUser = newUser;
        return newUser;
    }

    public Game createNewGame(String alias, String playerColorStr) {
        // calculez ID-ul noului joc
        int newId;
        int maxId = 0;

        if (!games.isEmpty()) {
            Iterator<Integer> keyIdx = games.keySet().iterator();
            while (keyIdx.hasNext()) {
                int currentId = keyIdx.next();
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
        }
        newId = maxId + 1;

        // configurez culorile
        Colors pColor;
        Colors cColor;

        if (playerColorStr.equalsIgnoreCase("WHITE")) {
            pColor = Colors.WHITE;
            cColor = Colors.BLACK;
        } else {
            pColor = Colors.BLACK;
            cColor = Colors.WHITE;
        }

        // creez jucatorii
        Player player = new Player(currentUser.getEmail(), pColor);
        player.setAlias(alias);
        Player computer = new Player("Computer", cColor);

        // creez jocul
        Game game = new Game(newId, player, computer);
        game.start();
        this.games.put(newId, game);
        this.currentUser.addGame(game);
        return game;
    }


    public static void main(String[] args) {
        Main app = Main.getInstance();
        app.read();
        ChessFrame frame = new ChessFrame();
        frame.setVisible(true);
    }
}