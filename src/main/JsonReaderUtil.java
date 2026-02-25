package main;

import model.board.ChessPair;
import model.game.Game;
import model.game.Player;
import model.pieces.*;
import model.user.User;
import model.user.Position;
import model.enums.Colors;
import model.game.Move;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class JsonReaderUtil {

    public static List<User> readUsers(Path path) throws IOException, ParseException {
        // folosesc try-with-resources ca sa se inchida automat fisierul dupa cititre
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            // creez un parser ca sa pot sa citesc din JSON
            // citesc tot textul din fisier si il transform intr-o structura de obiecte
            // fac cast la JsonArray
            JSONArray jar = (JSONArray) (parser.parse(reader));
            List<User> users = new ArrayList<>();
            // parcurg lista de JSON
            Iterator idx = jar.iterator();
            while (idx.hasNext()) {
                // fac castul la JSONObject
                JSONObject obj = (JSONObject) idx.next();
                // extrag datele
                String email = (String) obj.get("email");
                String password = (String) obj.get("password");
                int points = ((Number) obj.get("points")).intValue();
                // creez un user cu datele extrase si il adaug in lista
                User user = new User(email, password);
                user.setPoints(points);
                users.add(user);
            }
            return users;
        }
    }

    public static Map<Integer, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Integer, Game> map = new HashMap<>();

        // folosesc try-with-resources ca sa se inchida automat fisierul dupa cititre
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            // creez un parser ca sa pot sa citesc din JSON
            // citesc tot textul din fisier si il transform intr-o structura de obiecte
            // fac cast la JsonArray
            JSONParser parser = new JSONParser();
            JSONArray jar = (JSONArray) (parser.parse(reader));

            // parcurg lista de JSON
            Iterator idx = jar.iterator();
            while (idx.hasNext()) {
                JSONObject obj = (JSONObject) idx.next();
                // citesc id-ul jocului
                int id = ((Number) obj.get("id")).intValue();
                // citesc cei 2 jucatori
                JSONArray playersArr = (JSONArray) obj.get("players");

                // extrag player1
                JSONObject p1 = (JSONObject) playersArr.get(0);
                String p1n = (String) p1.get("email");
                Player player1 = new Player(p1n,
                        Colors.valueOf((String) p1.get("color")));
                p1n = (String) p1.get("alias");
                if (p1n != null)
                    player1.setAlias((String) p1.get("alias"));

                // extrag player2
                JSONObject p2 = (JSONObject) playersArr.get(1);
                String p2n = (String) p2.get("email");
                Player player2 = new Player(p2n,
                        Colors.valueOf((String) p2.get("color")));
                p2n = (String) p2.get("alias");
                if (p2n != null)
                    player2.setAlias((String) p2.get("alias"));

                // creez jocul
                Game game = new Game(id, player1, player2);

                // constructorul din game pune deja piese pe tabla in pozitia initiala
                // ma asigur ca tabla e goala, pentru ca vreau sa incarc un joc si sa pun piesele curente
                game.getBoard().getPieces().clear();
                // parcurg lista de piese
                JSONArray boardArr = (JSONArray) obj.get("board");
                Iterator idx1 = boardArr.iterator();
                while (idx1.hasNext()) {
                    Object o1 = idx1.next();
                    // extrag piesa
                    JSONObject Jpiece = (JSONObject) o1;
                    String type = (String) Jpiece.get("type");
                    Colors color = Colors.valueOf((String) Jpiece.get("color"));

                    // extrag pozitia
                    String Jpos = (String) Jpiece.get("position");
                    Position pos = new Position(Jpos.charAt(0), Integer.parseInt(Jpos.substring(1)));

                    Piece piece = null;
                    // creez piesa in functie de tipul ei
                    switch (type) {
                        case "P":
                            piece = new Pawn(color, pos);
                            break;
                        case "R":
                            piece = new Rook(color, pos);
                            break;
                        case "N":
                            piece = new Knight(color, pos);
                            break;
                        case "B":
                            piece = new Bishop(color, pos);
                            break;
                        case "Q":
                            piece = new Queen(color, pos);
                            break;
                        case "K":
                            piece = new King(color, pos);
                            break;
                    }
                    game.getBoard().getPieces().add(new ChessPair<>(pos, piece));
                }

                // citesc mutarile
                JSONArray movesArr = (JSONArray) obj.get("moves");
                Iterator moveIdx = movesArr.iterator();
                while (moveIdx.hasNext()) {
                    JSONObject moveObj = (JSONObject) moveIdx.next();

                    String pColorStr = (String) moveObj.get("playerColor");
                    String fromStr = (String) moveObj.get("from");
                    String toStr = (String) moveObj.get("to");

                    Position from = new Position(fromStr.charAt(0), Integer.parseInt(fromStr.substring(1)));
                    Position to = new Position(toStr.charAt(0), Integer.parseInt(toStr.substring(1)));
                    Colors pColor = Colors.valueOf(pColorStr);

                    // citesc piesa capturata
                    Piece capturedPiece = null;
                    JSONObject capObj = (JSONObject) moveObj.get("captured");
                    if (capObj != null) {
                        String capType = (String) capObj.get("type");
                        String capColorStr = (String) capObj.get("color");
                        Colors capColor = Colors.valueOf(capColorStr);

                        // refac obiectul piesa (pozitia e null pt ca e capturata)
                        switch (capType) {
                            case "P":
                                capturedPiece = new Pawn(capColor, null);
                                break;
                            case "R":
                                capturedPiece = new Rook(capColor, null);
                                break;
                            case "N":
                                capturedPiece = new Knight(capColor, null);
                                break;
                            case "B":
                                capturedPiece = new Bishop(capColor, null);
                                break;
                            case "Q":
                                capturedPiece = new Queen(capColor, null);
                                break;
                            case "K":
                                capturedPiece = new King(capColor, null);
                                break;
                        }
                    }

                    // creez mutarea si o adaug in lista jocului
                    Move move = new Move(pColor, from, to, capturedPiece);
                    game.getMoves().add(move);

                }

                // restaurez playerul curent
                String nextPlayer = (String) obj.get("currentPlayerColor");
                game.resume(Colors.valueOf(nextPlayer));

                // pun jocul in map
                map.put(id, game);
            }
        }
        return map;
    }
}