package view;

import main.Main;
import model.game.Game;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class MenuPanel extends JPanel {
    // declar o variabila de tip chessFrame ca sa fac legatura
    // intre pagina de login si restul jocului
    private ChessFrame mainFrame;
    private JLabel points;

    private String[] qts = {
            "\"N-am rival, n-am adversar, vă dau mat direct din cal!\"",
            "\"Niciun preț nu e prea mare pentru un atac cu valoare!\"",
            "\"În șah, singuru' adversar care-mi stă în cale sunt tot eu, că am prea multă valoare!\"",
            "\"Fiecare pion este o regină în devenire, boss!\"",
            "\"Am talent de mare campion, dau sah mat și din pion!\""
    };

    private JLabel quoteL;

    public MenuPanel(final ChessFrame frame) {
        this.mainFrame = frame;
        setBackground(new Color(0x1E140F));
        setLayout(new GridBagLayout());

        Random rand = new Random();
        String qRandom = qts[rand.nextInt(qts.length)];

        quoteL = new JLabel(qRandom);
        quoteL.setFont(new Font("Georgia", Font.ITALIC, 16));
        quoteL.setForeground(new Color(212, 147, 72));
        quoteL.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("Meniul Jupânului");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(new Color(245, 230, 211));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        points = new JLabel("Salut, barosane! Puncte: 0");
        points.setFont(new Font("Georgia", Font.ITALIC, 16));
        points.setForeground(new Color(212, 147, 72));
        points.setHorizontalAlignment(SwingConstants.CENTER);

        JButton newGameBtn = createButton("Joc Nou");
        JButton loadGameBtn = createButton("Continuă un Joc");
        JButton deleteGameBtn = createButton("Șterge un Joc Salvat");
        JButton logoutBtn = createButton("Ieși din Cont");

        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stilizez fereastra
                UIManager.put("OptionPane.background", new Color(0x1E140F));
                UIManager.put("Panel.background", new Color(0x1E140F));
                UIManager.put("OptionPane.messageForeground", new Color(0xF5E6D3));
                UIManager.put("Button.background", new Color(212, 147, 72));
                UIManager.put("Button.foreground", new Color(0x1E140F));
                UIManager.put("ComboBox.background", new Color(245, 230, 211));
                UIManager.put("ComboBox.foreground", new Color(0x1E140F));

                // creez un panou pentru input
                JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
                panel.setBackground(new Color(0x1E140F));

                // camp pentru alias
                JLabel lAlias = new JLabel("Cum te numesti jupane?");
                lAlias.setForeground(new Color(0xF5E6D3));
                JTextField txtAlias = new JTextField();

                JLabel lblColor = new JLabel("Cu ce piese joci?");
                lblColor.setForeground(new Color(0xF5E6D3));
                String[] colors = {"ALB", "NEGRU"};
                JComboBox<String> cmbColor = new JComboBox<>(colors);

                panel.add(lAlias);
                panel.add(txtAlias);
                panel.add(lblColor);
                panel.add(cmbColor);
                int result = JOptionPane.showConfirmDialog(
                        MenuPanel.this,
                        panel,
                        "Configurare Joc Nou",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String alias = txtAlias.getText().trim();
                    String selectedColorStr = (String) cmbColor.getSelectedItem();

                    // daca nu s-a introdus un alias
                    if (alias.isEmpty()) {
                        alias = "Conducatorul mapamondului";
                    }

                    String colorCode;
                    if (selectedColorStr.equals("ALB")) {
                        colorCode = "WHITE";
                    } else {
                        colorCode = "BLACK";
                    }
                    Game game = Main.getInstance().createNewGame(alias, colorCode);

                    mainFrame.showGame(game);
                }
            }
        });

        loadGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stilizez fereastra cu jocurile
                UIManager.put("OptionPane.background", new Color(0x1E140F));
                UIManager.put("Panel.background", new Color(0x1E140F));
                UIManager.put("OptionPane.messageForeground", new Color(0xF5E6D3));
                UIManager.put("Button.background", new Color(212, 147, 72));
                UIManager.put("Button.foreground", new Color(0x1E140F));
                UIManager.put("ComboBox.background", new Color(245, 230, 211));
                UIManager.put("ComboBox.foreground", new Color(0x1E140F));
                List<Game> activeGames = Main.getInstance().getUserActiveGames();

                if (activeGames.isEmpty()) {
                    JOptionPane.showMessageDialog(MenuPanel.this, "Nu ai niciun joc început, șefule!", "Eroare", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                Object[] games = activeGames.toArray();
                // afisez lista
                Game selected = (Game) JOptionPane.showInputDialog(
                        MenuPanel.this,
                        "Alege care joc vrei să o continui:",
                        "Încarcă Joc",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        games,
                        null);

                if (selected != null) {
                    // extrag jocul
                    mainFrame.showGame(selected);
                }
            }
        });

        deleteGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stilizez fereastra cu jocurile
                UIManager.put("OptionPane.background", new Color(0x1E140F));
                UIManager.put("Panel.background", new Color(0x1E140F));
                UIManager.put("OptionPane.messageForeground", new Color(0xF5E6D3));
                UIManager.put("Button.background", new Color(212, 147, 72));
                UIManager.put("Button.foreground", new Color(0x1E140F));
                UIManager.put("ComboBox.background", new Color(245, 230, 211));
                UIManager.put("ComboBox.foreground", new Color(0x1E140F));

                List<Game> activeGames = Main.getInstance().getUserActiveGames();

                if (activeGames.isEmpty()) {
                    JOptionPane.showMessageDialog(MenuPanel.this, "Nu ai ce să ștergi, lista e goală!", "Info", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                Object[] games = activeGames.toArray();
                // afisez lista
                Game selectedToDelete = (Game) JOptionPane.showInputDialog(
                        MenuPanel.this,
                        "Alege jocul pe care vrei să-l ȘTERGI:",
                        "Ștergere Joc",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        games,
                        null);

                if (selectedToDelete != null) {
                    // confirmare suplimentara
                    int confirm = JOptionPane.showConfirmDialog(
                            MenuPanel.this,
                            "Ești sigur boss? Jocul va fi pierdut definitiv.",
                            "Confirmare Ștergere",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // sterg jocul din memorie
                        Main.getInstance().removeGame(selectedToDelete);
                        // salvez in fisier
                        Main.getInstance().write();

                        JOptionPane.showMessageDialog(MenuPanel.this, "Jocul a fost sters, sefule!");
                    }
                }
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // salvez
                Main.getInstance().write();
                Main.getInstance().logout();
                mainFrame.showLogin();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(quoteL);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titleLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 40, 10);
        add(points, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(newGameBtn, gbc);

        gbc.gridy = 4;
        add(loadGameBtn, gbc);

        gbc.gridy = 5;
        add(deleteGameBtn, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(logoutBtn, gbc);
    }

    // metoda pentru afisarea punctelor
    public void refreshStats() {
        User currentUser = Main.getInstance().getCurrentUser();
        if (currentUser != null) {
            points.setText("Jupânul: " + currentUser.getEmail() + " | Valoare (Puncte): " + currentUser.getPoints());
        }
    }

    // ca sa nu mai scriu specificatiile de culori la fiecare buton in parte
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(212, 147, 72));
        btn.setForeground(new Color(0x1E140F));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Georgia", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(250, 45));
        return btn;
    }
}