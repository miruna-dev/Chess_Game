package view;

import main.Main;
import model.board.ChessPair;
import model.game.Game;
import model.game.Player;
import model.pieces.Piece;
import model.user.Position;
import model.enums.Colors;
import model.user.User;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {
    // declar o variabila de tip chessFrame ca sa fac legatura
    // intre pagina de login si restul jocului
    private ChessFrame mainFrame;
    private Game game;

    private JLabel status;
    private JLabel wScore;
    private JLabel bScore;
    private JTextArea wCaptured;
    private JTextArea bCaptured;
    private BoardCanvas board;

    private Position selected = null;
    private List<Position> validMoves = new ArrayList<>();

    public GamePanel(final ChessFrame frame, final Game game) {
        this.mainFrame = frame;
        this.game = game;

        setLayout(new BorderLayout());
        setBackground(new Color(0x1E140F));

        // status
        JPanel statPanel = new JPanel();
        statPanel.setBackground(new Color(0x1E140F));
        statPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        status = new JLabel("Joc început!");
        status.setFont(new Font("Georgia", Font.BOLD, 24));
        status.setForeground(new Color(212, 147, 72));
        statPanel.add(status);
        add(statPanel, BorderLayout.NORTH);

        // tabla de sah
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(new Color(0x1E140F));

        board = new BoardCanvas();
        board.setPreferredSize(new Dimension(600, 600));
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // calculez in ce patrat s-a dat click in functie de coordonatele pixelilor
                handleBoardClick(e.getX(), e.getY());
            }
        });

        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        // piese capturate
        JPanel capturedPanel = new JPanel();
        // folosesc box layout ca sa pun elementele unul sub altul
        capturedPanel.setLayout(new BoxLayout(capturedPanel, BoxLayout.Y_AXIS));
        capturedPanel.setBackground(new Color(0x1E140F));
        capturedPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        capturedPanel.setPreferredSize(new Dimension(280, 0));

        Player p1 = game.getPlayer1();
        String p1L;
        if (p1.getAlias().equals("Computer"))
            p1L = "Jupanaul Digital (";
        else
            p1L = p1.getAlias() + " (";

        if (p1.getColor() == Colors.WHITE)
            p1L += "Alb)";
        else
            p1L += "Negru)";

        Player p2 = game.getPlayer2();
        String p2L;
        if (p2.getAlias().equals("Computer"))
            p2L = "Jupanaul Digital (";
        else
            p2L = p1.getAlias() + " (";

        if (p2.getColor() == Colors.WHITE)
            p2L += "Alb)";
        else
            p2L += "Negru)";

        // chenar pentru alb
        JLabel lj1 = new JLabel(p1L);
        lj1.setFont(new Font("Georgia", Font.BOLD, 16));
        lj1.setForeground(new Color(212, 147, 72));
        lj1.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(lj1);

        wScore = new JLabel("Valoare Capturi: 0");
        wScore.setFont(new Font("Georgia", Font.BOLD, 14));
        wScore.setForeground(new Color(100, 200, 100));
        wScore.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(wScore);

        JLabel cP = new JLabel("Piese Capturate:");
        cP.setFont(new Font("Georgia", Font.PLAIN, 12));
        cP.setForeground(new Color(0xF5E6D3));
        cP.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(cP);

        wCaptured = new JTextArea(3, 10);
        wCaptured.setBackground(new Color(0x2E241F));
        wCaptured.setForeground(new Color(0xF5E6D3));
        wCaptured.setEditable(false);
        wCaptured.setFont(new Font("Serif", Font.PLAIN, 24));
        wCaptured.setLineWrap(true);
        // pentru spatiu intre chenare
        wCaptured.setBorder(new EmptyBorder(0, 0, 40, 0));
        wCaptured.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(wCaptured);

        // chenar pentru negru
        JLabel lj2 = new JLabel(p2L);
        lj2.setFont(new Font("Georgia", Font.BOLD, 16));
        lj2.setForeground(new Color(212, 147, 72));
        lj2.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(lj2);
        bScore = new JLabel("Valoare Capturi: 0");
        bScore.setFont(new Font("Georgia", Font.BOLD, 14));
        bScore.setForeground(new Color(100, 200, 100));
        bScore.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(bScore);

        JLabel cl = new JLabel("Piese Capturate:");
        cl.setFont(new Font("Georgia", Font.PLAIN, 12));
        cl.setForeground(new Color(0xF5E6D3));
        cl.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(cl);

        bCaptured = new JTextArea(3, 10);
        bCaptured.setBackground(new Color(0x2E241F));
        bCaptured.setForeground(new Color(0xF5E6D3));
        bCaptured.setEditable(false);
        bCaptured.setFont(new Font("Serif", Font.PLAIN, 24));
        bCaptured.setLineWrap(true);
        bCaptured.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPanel.add(bCaptured);

        add(capturedPanel, BorderLayout.EAST);

        // butoane
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(new Color(0x1E140F));
        btnPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JButton btnResign = new JButton("Renunță (-150pct)");
        btnResign.setBackground(new Color(212, 147, 72));
        btnResign.setForeground(new Color(0x1E140F));
        btnResign.setFont(new Font("Georgia", Font.BOLD, 14));
        btnResign.setFocusPainted(false);
        btnResign.setPreferredSize(new Dimension(200, 40));

        JButton btnSave = new JButton("Salvează și Ieși");
        btnSave.setBackground(new Color(212, 147, 72));
        btnSave.setForeground(new Color(0x1E140F));
        btnSave.setFont(new Font("Georgia", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(200, 40));

        JButton btnMenu = new JButton("Înapoi la meniu");
        btnMenu.setBackground(new Color(212, 147, 72));
        btnMenu.setForeground(new Color(0x1E140F));
        btnMenu.setFont(new Font("Georgia", Font.BOLD, 14));
        btnMenu.setFocusPainted(false);
        btnMenu.setPreferredSize(new Dimension(200, 40));

        btnResign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(GamePanel.this,
                        "Dacă renunți, pierzi 150 de puncte! Ești sigur, boss?",
                        "Renunță",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    User currentUser = Main.getInstance().getCurrentUser();
                    currentUser.setPoints(currentUser.getPoints() - 150);

                    JOptionPane.showMessageDialog(GamePanel.this,
                            "Ai cedat. Ai pierdut 150 puncte.\nScor total: " + currentUser.getPoints(),
                            "Rezultat",
                            JOptionPane.PLAIN_MESSAGE);

                    Main.getInstance().removeGame(game);
                    Main.getInstance().write();
                    frame.showMenu();
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().write();
                JOptionPane.showMessageDialog(GamePanel.this,
                        "Joc salvat!",
                        "Salvat",
                        JOptionPane.PLAIN_MESSAGE);
                frame.showMenu();
            }
        });

        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        GamePanel.this,
                        "Ce ai continuat în joc nu va fi salvat, boss! Ești sigur că revii la meniu?",
                        "Atenție!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    Main.getInstance().read();
                    frame.showMenu();
                }
            }
        });

        btnPanel.add(btnResign);
        btnPanel.add(btnSave);
        btnPanel.add(btnMenu);

        add(btnPanel, BorderLayout.SOUTH);

        // actualizez statusul
        updateStatus();

        // verific daca jocul incarcat este terminat
        boolean end = checkGameStatus();
        // declansez mutarea calculatorului
        if (!end) {
            doComputerMove();
        }
    }

    private void handleBoardClick(int x, int y) {
        // calculez indexul pozitiei
        x = x / 75;
        y = y / 75;

        // daca joc cu negru intorc tabla
        if (game.getPlayer1().getColor() == Colors.BLACK) {
            x = 7 - x;
            y = 7 - y;
        }
        // ma asigur ca fac parte din tabla
        if (x < 0 || x > 7 || y < 0 || y > 7) return;

        // transform in linie si coloana
        char l = (char) ('A' + x);
        int c = 8 - y;
        Position clickedPos = new Position(l, c);

        Player player;
        if (game.getCurrentPlayer() == 1) {
            player = game.getPlayer1();
        } else {
            player = game.getPlayer2();
        }

        // verific daca e randul jucatorului
        if (!player.getAlias().equals("Computer")) {
            boolean moved = false;
            if (selected != null) {
                // verific daca s-a ales o pozitie valida si execut mutarea
                Iterator<Position> it = validMoves.iterator();
                while (it.hasNext() && moved == false) {
                    Position valid = it.next();
                    if (valid.equals(clickedPos)) {
                        executeMove(selected, clickedPos);
                        moved = true;
                    }
                }
            }

            // daca nu am executat nicio mutare
            if (!moved) {
                Piece p = game.getBoard().getPieceAt(clickedPos);

                // verific daca piesa e de aceeasi culoare cu a jucatorului care muta
                if (p != null && p.getColor() == player.getColor()) {
                    selected = clickedPos;
                    // iau toate mutarile posibile
                    validMoves = p.getPossibleMoves(game.getBoard());
                    // verific daca nu lasa regele in sah
                    List<Position> nonCheck = new ArrayList<>();
                    Iterator<Position> it = validMoves.iterator();
                    while (it.hasNext()) {
                        Position poz = it.next();
                        if (game.getBoard().isValidMove(p.getPosition(), poz)) {
                            nonCheck.add(poz);
                        }
                    }
                    validMoves = nonCheck;
                } else {
                    // setez piesa selectata pe fals si resetez mutarile posibile
                    selected = null;
                    validMoves.clear();
                }
            }
            board.repaint();
        }
    }

    private void executeMove(Position from, Position to) {
        try {
            char type = 'Q';
            Piece p = game.getBoard().getPieceAt(from);

            // daca pionul ajunge la capat
            if (p.type() == 'P') {
                if ((p.getColor() == Colors.WHITE && to.y == 8) || (p.getColor() == Colors.BLACK && to.y == 1)) {

                    Player player;
                    if (game.getCurrentPlayer() == 1) {
                        player = game.getPlayer1();
                    } else {
                        player = game.getPlayer2();
                    }

                    // daca e randul playerului
                    if (!player.getAlias().equals("Computer")) {
                        UIManager.put("OptionPane.background", new Color(0x1E140F));
                        UIManager.put("Panel.background", new Color(0x1E140F));
                        UIManager.put("OptionPane.messageForeground", new Color(0xF5E6D3));
                        UIManager.put("Button.background", new Color(212, 147, 72));
                        UIManager.put("Button.foreground", new Color(0x1E140F));

                        Object[] promoteIn = {"Regină", "Turn", "Nebun", "Cal"};
                        int c = JOptionPane.showOptionDialog(this,
                                "Promovare Pion! Ce alegi?", "Promovare",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, promoteIn, promoteIn[0]);

                        switch (c) {
                            case 1:
                                type = 'R';
                                break;
                            case 2:
                                type = 'B';
                                break;
                            case 3:
                                type = 'N';
                                break;
                            default:
                                type = 'Q';
                                break;
                        }
                    }
                }
            }

            // mut piesa
            if (game.getCurrentPlayer() == 1) {
                game.getPlayer1().makeMove(from, to, game.getBoard(), type);
                game.addMove(game.getPlayer1(), from, to, game.getPlayer1().getLastCapturedPiece());
            } else {
                game.getPlayer2().makeMove(from, to, game.getBoard(), type);
                game.addMove(game.getPlayer2(), from, to, game.getPlayer2().getLastCapturedPiece());
            }

            // schimb jucatorul
            game.switchPlayer();
            selected = null;
            validMoves.clear();

            updateStatus();
            board.repaint();
            // verific daca jocul e terminat
            boolean end = checkGameStatus();
            if (!end)
                doComputerMove();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Mutare imposibilă: " + ex.getMessage());
        }
    }

    private void doComputerMove() {

        Player player;
        if (game.getCurrentPlayer() == 1)
            player = game.getPlayer1();
        else
            player = game.getPlayer2();

        if (player.getAlias().equals("Computer")) {
            status.setText("Se gândește Jupânul Digital...");

            // creez un timer care sa astepte 800 ms inainte sa execute mutarea
            Timer wait = new Timer(800, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    // gasesc toate piesele calculatorului
                    List<Piece> computer = new ArrayList<>();
                    Iterator<ChessPair<Position, Piece>> it = game.getBoard().getPieces().iterator();
                    while (it.hasNext()) {
                        ChessPair<Position, Piece> p = it.next();
                        Piece piece = p.getValue();
                        if (piece.getColor() == player.getColor())
                            computer.add(piece);
                    }

                    // gasesc toate mutarile posibile ale calculatorului
                    List<Position[]> candidates = new ArrayList<Position[]>();
                    // iau fiecare piesa
                    Iterator<Piece> itp = computer.iterator();
                    while (itp.hasNext()) {
                        Piece p = itp.next();
                        List<Position> moves = p.getPossibleMoves(game.getBoard());
                        // gasesc mutarile posibile pentru piesa curenta
                        Iterator<Position> itm = moves.iterator();
                        while (itm.hasNext()) {
                            Position p1 = itm.next();
                            if (game.getBoard().isValidMove(p.getPosition(), p1))
                                candidates.add(new Position[]{p.getPosition(), p1});
                        }
                    }
                    // execut mutarea
                    if (!candidates.isEmpty()) {
                        Random rand = new Random();
                        // iau mutarea random
                        Position[] move = candidates.get(rand.nextInt(candidates.size()));
                        Position from = move[0];
                        Position to = move[1];
                        try {
                            // mut piesa, pentru calculator daca e pion se promoveaza automat in regina
                            player.makeMove(from, to, game.getBoard(), 'Q');
                            game.addMove(player, from, to, player.getLastCapturedPiece());
                            game.switchPlayer();
                            updateStatus();
                            repaint();

                            // verific daca s-a incheiat jocul
                            checkGameStatus();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // calculatorul nu mai are mutari valide
                        // sah mat sau remiza
                        checkGameStatus();
                    }
                }
            });
            // altfel calculatorul face o mutare la fiecare 800 ms
            wait.setRepeats(false);
            wait.start();
        }
    }

    private boolean checkGameStatus() {
        User user = Main.getInstance().getCurrentUser();
        if (game.checkForCheckMate()) {
            // vad daca a castigat userul
            int loserId = game.getCurrentPlayer();
            Player loser;
            if (loserId == 1)
                loser = game.getPlayer1();
            else
                loser = game.getPlayer2();

            int currentPoints = user.getPoints();

            Player human;
            if (game.getPlayer1().getAlias().equals("Computer"))
                human = game.getPlayer2();
            else
                human = game.getPlayer1();

            // iau lista de capturi a jucatorului om
            List<Piece> hCaptures = human.getCapturedPieces();

            // calculez punctele
            int points = calculateCapturePoints(hCaptures);

            Object[] options = {"Înapoi la Meniu", "Închide Aplicația"};
            int total;
            String titlu, mesaj;

            if (loser.getAlias().equals("Computer")) {
                // daca castiga omul
                // +puncte din capturi +300
                total = currentPoints + points + 300;
                // actualizez punctele
                user.setPoints(total);
                titlu = "FELICITĂRI BOSS!";
                mesaj = "Ai câștigat prin Șah Mat!\n" +
                        "Puncte capturi: " + points + "\n" +
                        "Bonus victorie: 300\n" +
                        "Total nou: " + total;
            } else {
                // daca pierde omul
                // +puncte capturi -300
                total = currentPoints + points - 300;
                // actualizez punctele
                user.setPoints(total);
                titlu = "GHINION!";
                mesaj = "Te-a bătut Jupanul digital, barosane.\n" +
                        "Puncte capturi: " + points + "\n" +
                        "Penalizare înfrângere: -300\n" +
                        "Total nou: " + total;
            }

            // salvez datele
            Main.getInstance().removeGame(game);
            Main.getInstance().write();

            // afisez fereastra de final cu cele două optiuni
            int selectie = JOptionPane.showOptionDialog(
                    this,
                    mesaj,
                    titlu,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selectie == 1)
                // inchid aplicatia
                System.exit(0);
            else
                // ma intorc la meniu
                mainFrame.showMenu();
            return true;

        } else if (game.isInCheck(game.getCurrentPlayer())) {
            // avertizez jucatorul ca e in sah
            status.setText("ȘAH! Ai grijă la Rege!");
            status.setForeground(Color.RED);
            return false;
        } else {
            int draw = game.checkDrawStatus();
            if (draw != 0) {
                // draw 1 = Pat, 2 = piese insuficiente
                String reason;
                if (draw == 1)
                    reason = "PAT (Nu ai mutări, dar nu ești în șah)";
                else if (draw == 2)
                    reason = "Piese insuficiente!";
                else
                    reason = "Remiza prin repetare!";

                int currentPoints = user.getPoints();
                Player player;
                if (game.getPlayer1().getEmail().equals("Computer"))
                    player = game.getPlayer2();
                else
                    player = game.getPlayer1();

                int pointsFromCaptures = calculateCapturePoints(player.getCapturedPieces());

                int total = currentPoints + pointsFromCaptures + 150;
                user.setPoints(total);

                // salvez datele si sterg jocul
                Main.getInstance().removeGame(game);
                Main.getInstance().write();

                Object[] options = {"Înapoi la Meniu", "Închide Aplicația"};
                String mesajRemiza = "JOC TERMINAT: REMIZĂ!\n" +
                        "Motiv: " + reason + "\n" +
                        "Puncte capturi: " + pointsFromCaptures + "\n" +
                        "Bonus remiză: +150\n" +
                        "Total nou: " + total;

                int selectie = JOptionPane.showOptionDialog(
                        this,
                        mesajRemiza,
                        "REMIZĂ",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (selectie == 1)
                    System.exit(0);
                else
                    mainFrame.showMenu();
                return true;
            } else {
                // jocul continua
                // determin jucatorul curent
                Player current;
                if (game.getCurrentPlayer() == 1) {
                    current = game.getPlayer1();
                } else {
                    current = game.getPlayer2();
                }

                // determin numele de afisat
                String name;
                if (current.getAlias().equals("Computer")) {
                    name = "Jupânul Digital";
                } else {
                    name = current.getAlias();
                }

                // determin culoarea
                String color;
                if (current.getColor() == Colors.WHITE) {
                    color = "ALB";
                } else {
                    color = "NEGRU";
                }

                status.setText("Mută " + name + " (" + color + ")");
                status.setForeground(new Color(212, 147, 72));
                return false;
            }
        }
    }

    private void updateStatus() {
        // iau listele cu piesele capturate
        List<Piece> cw = game.getPlayer1().getCapturedPieces();
        List<Piece> cb = game.getPlayer2().getCapturedPieces();

        // calculez punctele pentru piesele capturate
        int sw = calculateCapturePoints(cw);
        int sb = calculateCapturePoints(cb);

        wScore.setText("Valoare Capturi: " + sw);
        bScore.setText("Valoare Capturi: " + sb);

        // afisez piesele capturate
        wCaptured.setText(getCapturedString(cw));
        bCaptured.setText(getCapturedString(cb));
    }

    private int calculateCapturePoints(List<Piece> pieces) {
        int score = 0;
        Iterator<Piece> it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = it.next();
            switch (p.type()) {
                case 'Q':
                    score += 90;
                    break;
                case 'R':
                    score += 50;
                    break;
                case 'B':
                    score += 30;
                    break;
                case 'N':
                    score += 30;
                    break;
                case 'P':
                    score += 10;
                    break;
                default:
                    break;
            }
        }
        return score;
    }

    private String getCapturedString(List<Piece> pieces) {
        String s = "";
        int r = 0;
        Iterator<Piece> it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = it.next();
            s += getPieceUnicode(p) + " ";
            r++;
            if (r % 6 == 0) {
                s += "\n";
            }
        }
        return s;
    }

    private class BoardCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // formez patratele
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) % 2 == 0)
                        g.setColor(new Color(240, 236, 214));
                    else
                        g.setColor(new Color(119, 149, 86));
                    g.fillRect(j * 75, i * 75, 75, 75);
                }
            }

            // evidentiez piesa selectata
            if (selected != null) {
                int i = selected.x - 'A';
                int j = 8 - selected.y;

                // inversez coordonatele daca joc cu negru
                if (game.getPlayer1().getColor() == Colors.BLACK) {
                    i = 7 - i;
                    j = 7 - j;
                }

                g.setColor(new Color(255, 255, 0, 100));
                g.fillRect(i * 75, j * 75, 75, 75);

                // evidentiez mutarile valide
                g.setColor(new Color(100, 255, 100, 150));
                Iterator<Position> itValid = validMoves.iterator();
                while (itValid.hasNext()) {
                    Position pos = itValid.next();
                    int mc = pos.x - 'A';
                    int mr = 8 - pos.y;

                    if (game.getPlayer1().getColor() == Colors.BLACK) {
                        mc = 7 - mc;
                        mr = 7 - mr;
                    }
                    g.fillRect(mc * 75, mr * 75, 75, 75);
                }
            }
            g.setFont(new Font("Serif", Font.PLAIN, 60));
            FontMetrics fm = g.getFontMetrics();

            // desenez piesele pe tabla
            if (game != null && game.getBoard() != null) {
                // parcurg piesele de pe tabla
                Iterator<ChessPair<Position, Piece>> itPieces = game.getBoard().getPieces().iterator();
                while (itPieces.hasNext()) {
                    Piece p = itPieces.next().getValue();

                    // calculez pozitia logica a piesei
                    int c = p.getPosition().x - 'A';
                    int r = 8 - p.getPosition().y;

                    int i = c;
                    int j = r;
                    if (game.getPlayer1().getColor() == Colors.BLACK) {
                        i = 7 - c;
                        j = 7 - r;
                    }

                    // iau simbolul piesei
                    String uc = getPieceUnicode(p);

                    // centrez piesa in patratul calculat pentru desenare
                    int x = i * 75 + (75 - fm.stringWidth(uc)) / 2;
                    int y = j * 75 + (75 - fm.getHeight()) / 2 + fm.getAscent();

                    // daca am o piesa alba ii pun un contur negru
                    if (p.getColor() == Colors.WHITE) {
                        g.setColor(Color.BLACK);
                        g.drawString(uc, x + 1, y + 1);
                        g.setColor(new Color(255, 250, 240));
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.drawString(uc, x, y);
                }
            }
        }
    }

    private String getPieceUnicode(Piece p) {
        switch (p.type()) {
            case 'K':
                return "\u265A";
            case 'Q':
                return "\u265B";
            case 'R':
                return "\u265C";
            case 'B':
                return "\u265D";
            case 'N':
                return "\u265E";
            case 'P':
                return "\u265F";
            default:
                return "?";
        }
    }
}