package view;

import game_of_life.Game;
import sun.applet.Main;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 10.02.2016.
 */
public class HexWindow extends JFrame implements Observer {
    private int sizeX = 800;
    private int sizeY = 600;
    JScrollPane scrollPane;
    HexGrid hexGrid;
    HexWindow thisWindow = this; // for exit event
    Game game;

    public HexWindow (Game game) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());
        this.game = game;
        hexGrid = new HexGrid(game, game.getWidth(), game.getHeight(), 26, 10);
        setContentPane(createContentPane(hexGrid));
        setSize(sizeX, sizeY);
        setMinimumSize(new Dimension(sizeX, sizeY));
        setVisible(true);
    }

    private JMenuBar createMenuBar () {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        // Menu FILE
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        // Items FILE - NEW
        menuItem = new JMenuItem("New",
                KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("AZAZA");
            }
        });
        menu.add(menuItem);

        // Items FILE - OPEN
        menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        menu.add(menuItem);

        // Items FILE - SAVE
        menuItem = new JMenuItem("Save", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        menu.add(menuItem);

        // Items FILE - SAVE AS
        menuItem = new JMenuItem("Save As...", KeyEvent.VK_S);
        menu.add(menuItem);

        // Items FILE - EXIT
        menu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(thisWindow, WindowEvent.WINDOW_CLOSED));
            }
        });
        menu.add(menuItem);

        //Menu EDIT.
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                "wat accessible context");
        menuBar.add(menu);

        // Items EDIT - MODES
        ButtonGroup group = new ButtonGroup();
        // EDIT - MODES - XOR
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("XOR");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_X);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);
        // EDIT - MODES - REPLACE
        rbMenuItem = new JRadioButtonMenuItem("Replace");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        // Items EDIT - CLEAR
        menu.addSeparator();
        menuItem = new JMenuItem("Clear", KeyEvent.VK_C);
        menu.add(menuItem);

        // Items EDIT - SETTING
        menu.addSeparator();
        menuItem = new JMenuItem("Settings", KeyEvent.VK_S);
        menu.add(menuItem);

        // Menu VIEW
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                "wat accessible context");
        menuBar.add(menu);

        // Items VIEW - TOOLBAR
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Toolbar");
        cbMenuItem.setMnemonic(KeyEvent.VK_T);
        cbMenuItem.setState(true);
        menu.add(cbMenuItem);

        // Items VIEW - STATUS BAR
        cbMenuItem = new JCheckBoxMenuItem("Status Bar");
        cbMenuItem.setMnemonic(KeyEvent.VK_S);
        cbMenuItem.setState(true);
        menu.add(cbMenuItem);

        // Items VIEW - IMPACTS
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("Show Impacts");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.setState(false);
        menu.add(cbMenuItem);

        // Menu SIMULATION
        menu = new JMenu("Simulation");
        menu.setMnemonic(KeyEvent.VK_S);
        menu.getAccessibleContext().setAccessibleDescription(
                "wat accessible context");
        menuBar.add(menu);

        // Items SIMULATION - STEP
        menuItem = new JMenuItem("Step", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.tick();
            }
        });
        menu.add(menuItem);

        // Items SIMULATION - SETTINGS
        menu.addSeparator();
        menuItem = new JMenuItem("Settings", KeyEvent.VK_S);
        menu.add(menuItem);

        // Menu Help
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription(
                "wat accessible context");
        menuBar.add(menu);

        // Items HELP - ABOUT
        menuItem = new JMenuItem("About...", KeyEvent.VK_A);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog (null, "Game of Life, Azhbakov Artem FIT 13201", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    public Container createContentPane(JPanel content) {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(sizeX, sizeY));
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(sizeX, sizeY));
        scrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.
        scrollPane.setPreferredSize(new Dimension(sizeX, sizeY));
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
        return contentPane;
    }

    public void update (Observable observable, Object obj) {
        Game g = (Game) observable;
        for (int i = 0; i < g.getWidth(); i++) {
            for (int j = 0; j < g.getHeight(); j++) {
                if (g.isAlive(i, j)) hexGrid.livenHex(i, j);
                else hexGrid.killHex(i, j);
            }
        }
    }
}
