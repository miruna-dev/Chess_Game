package view;

import model.game.Game;
import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    public ChessFrame() {
        // ca sa se deschida direct full screen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Chess Game 2025");
        // in caz ca se face mica fereastra
        setSize(800, 850);
        // asigur inchiderea procesului la X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // folosesc cardlayout, mainpanel contine toate ferestrele
        // show spune pe care din ele o afisez
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // instantiez paginile
        loginPanel = new LoginPanel(this);
        menuPanel = new MenuPanel(this);

        // le adaug in pnelul principal
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(menuPanel, "MENU");

        add(mainPanel);
        // afisez login ca prima pagina
        showLogin();
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showMenu() {
        // actualizez datele din meniu inainte de afisare
        menuPanel.refreshStats();
        cardLayout.show(mainPanel, "MENU");
    }

    public void showGame(Game game) {
        gamePanel = new GamePanel(this, game);
        mainPanel.add(gamePanel, "JOC");
        cardLayout.show(mainPanel, "JOC");
    }
}