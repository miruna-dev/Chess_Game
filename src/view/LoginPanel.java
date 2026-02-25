package view;

import main.Main;
import model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class LoginPanel extends JPanel {
    // declar campurile pentru email si parola
    private JTextField emailField;
    private JPasswordField passField;
    // declar o variabila de tip chessFrame ca sa fac legatura
    // intre pagina de login si restul jocului
    private ChessFrame mainFrame;

    // variabila pentru imagine
    private BufferedImage image;

    public LoginPanel(ChessFrame frame) {
        this.mainFrame = frame;
        // configurez un borderlayout
        setLayout(new BorderLayout());

        // configurez partea stanga, cea cu imaginea
        JPanel leftPanel = new JPanel();
        // setez dimensiunea
        leftPanel.setPreferredSize(new Dimension(600, 0));
        leftPanel.setLayout(new BorderLayout());

        try {
            // incarc imaginea in meorie
            image = ImageIO.read(new File("chess.png"));

            // creez un JLabel care se foloseste de o metoda separata
            // ca sa redimensioneze frumos imaginea
            JLabel imageLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    // sterg ce era inainte
                    super.paintComponent(g);
                    // daca am imaginea o pun cu dimensiunea dorita
                    if (image != null) {
                        // getWidth si getHeight sunt latimea si inaltimea curente a panoului
                        drawCoverImage(g, image, getWidth(), getHeight());
                    }
                }
            };
            // adaug imaginea in panel
            leftPanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            // daca nu reusesc sa pun imaginea setez un GridLayout ca sa pun lucrurile centrate
            leftPanel.setLayout(new GridBagLayout());
            JLabel noImage = new JLabel("Jupânul Șahului");
            noImage.setForeground(Color.WHITE);
            leftPanel.add(noImage);
        }

        // zona cu formularul pentru email si parola
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0x1E140F));
        rightPanel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("Jupânul Șahului");
        welcomeLabel.setFont(new Font("Gerogia", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(245, 230, 211));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel instructionLabel = new JLabel("...pentru toți magnații care știu să joace șah");
        instructionLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(245, 230, 211));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        emailLabel.setForeground(new Color(245, 230, 211));

        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 30));
        emailField.setBackground(new Color(0x1E140F));
        emailField.setFont(new Font("Georgia", Font.PLAIN, 14));
        emailField.setForeground(new Color(245, 230, 211));
        // culoare cursor
        emailField.setCaretColor(new Color(0xFFFFFF));

        JLabel passLabel = new JLabel("Parola:");
        passLabel.setFont(new Font("Georgia", Font.BOLD, 14));
        passLabel.setForeground(new Color(245, 230, 211));

        passField = new JPasswordField(20);
        passField.setPreferredSize(new Dimension(200, 30));
        passField.setBackground(new Color(0x1E140F));
        passField.setFont(new Font("Georgia", Font.PLAIN, 14));
        passField.setForeground(new Color(245, 230, 211));
        // cursor
        passField.setCaretColor(new Color(245, 230, 211));

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBackground(new Color(212, 147, 72));
        loginBtn.setForeground(new Color(0x1E140F));
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Georgia", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(200, 40));

        JButton registerBtn = new JButton("Creeaza cont nou");
        registerBtn.setBackground(new Color(212, 147, 72));
        registerBtn.setForeground(new Color(0x1E140F));
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Georgia", Font.BOLD, 12));
        registerBtn.setPreferredSize(new Dimension(200, 35));

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // extrag textul din casetele de completat
                String email = emailField.getText().trim();
                String pass = new String(passField.getPassword()).trim();
                // aplelez login din main
                User user = Main.getInstance().login(email, pass);
                if (user != null) {
                    mainFrame.showMenu();
                    // resetez campurile de email si text
                    emailField.setText("");
                    passField.setText("");
                } else {
                    // fereastra cu eroare daca se greseste mailul sau parola
                    // setez culorile
                    UIManager.put("OptionPane.background", new Color(0x1E140F));
                    UIManager.put("Panel.background", new Color(0x1E140F));
                    UIManager.put("OptionPane.messageForeground", new Color(0xF5E6D3));

                    // creez un label cu mesajul
                    JLabel message = new JLabel("Date incorecte, boss!");
                    message.setFont(new Font("Georgia", Font.BOLD, 14));
                    message.setForeground(new Color(0xF5E6D3));

                    // afisez panelul
                    JOptionPane.showMessageDialog(LoginPanel.this, message, "Eroare", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // extrag textul
                String email = emailField.getText().trim();
                String pass = new String(passField.getPassword()).trim();
                // daca mailul e gol
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Completeaza emailul, boss!", "Eroare", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                if (pass.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Completeaza parola, boss!", "Eroare", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                User user = Main.getInstance().newAccount(email, pass);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Am creat contul, boss! Esti logat.");
                    mainFrame.showMenu();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Ai deja un cont cu acest email, boss!", "Eroare", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // fac un set de reguli pentru GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        // setez paddingul
        gbc.insets = new Insets(10, 10, 10, 10);
        // maresc butoanele
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        // numarul randului pe care pun piesa
        gbc.gridy = 0;
        rightPanel.add(welcomeLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10);
        rightPanel.add(instructionLabel, gbc);
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 2;
        rightPanel.add(emailLabel, gbc);
        gbc.gridy = 3;
        rightPanel.add(emailField, gbc);
        gbc.gridy = 4;
        rightPanel.add(passLabel, gbc);
        gbc.gridy = 5;
        rightPanel.add(passField, gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(loginBtn, gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        rightPanel.add(registerBtn, gbc);

        // adaug panelul stang si drept in cel final
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    // metoda care redimensioneaza imaginea ca sa se puna bine
    private void drawCoverImage(Graphics g, BufferedImage image, int panelWidth, int panelHeight) {
        // calculez raportul dintre latime si inaltime
        double imageRatio = (double) image.getWidth() / image.getHeight();
        double panelRatio = (double) panelWidth / panelHeight;
        int newWidth;
        int newHeight;
        int x = 0;
        int y = 0;

        // daca panoul e mai lat decat imaginea
        if (panelRatio > imageRatio) {
            newWidth = panelWidth;
            newHeight = (int) (newWidth / imageRatio);
            // centrez pe verticala
            y = (panelHeight - newHeight) / 2;
        }
        // daca panoul e mai inalt
        else {
            newHeight = panelHeight;
            newWidth = (int) (newHeight * imageRatio);
            // centrez pe orizontala
            x = (panelWidth - newWidth) / 2;
        }
        g.drawImage(image, x, y, newWidth, newHeight, null);
    }
}