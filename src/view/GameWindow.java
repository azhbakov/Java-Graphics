package view;

import game_of_life.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 24.02.2016.
 */
public class GameWindow extends MainWindow implements Observer {
    Game game;
    JScrollPane scrollPane;
    HexGrid hexGrid;

    public GameWindow (Game game) throws Exception{
        super(800, 600, "Game of Life");
        this.game = game;
        hexGrid = new HexGrid(game, game.getWidth(), game.getHeight(), 25, 10);
        setContentPane(createContentPane(hexGrid));
        // FILE
        addMenu("File", null, KeyEvent.VK_F);
        addMenuItem("File/New", "Select file with field preset", KeyEvent.VK_N, KeyStroke.getKeyStroke("control N"), null, this.getClass().getName(), "hello");
        addMenuItem("File/Open...", "Select field config on disk", KeyEvent.VK_O, KeyStroke.getKeyStroke("control O"), null, this.getClass().getName(), "openFile");
        addMenuItem("File/Save", "Save field to disk", KeyEvent.VK_S, KeyStroke.getKeyStroke("control S"), null, this.getClass().getName(), "saveFile");
        addMenuItem("File/Save as...", "Select field config on disk", KeyEvent.VK_S, null, null, this.getClass().getName(), "saveFileAs");
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit", KeyEvent.VK_X, null, null, this.getClass().getName(), "exit");

        // EDIT
        addMenu("Edit", null, KeyEvent.VK_E);

        ButtonGroup group = new ButtonGroup();
        // EDIT - MODES - XOR
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("XOR");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_X);
        group.add(rbMenuItem);
        addMenuItem("Edit/XOR", rbMenuItem);
        // EDIT - MODES - REPLACE
        rbMenuItem = new JRadioButtonMenuItem("Replace");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        addMenuItem("Edit/Replace", rbMenuItem);

        addMenuSeparator("Edit");
        addMenuItem("Edit/Clear", "Clear field", KeyEvent.VK_C, null, null, this.getClass().getName(), "clear");
        addMenuItem("Edit/Settings", "Show settings", KeyEvent.VK_S, null, null, this.getClass().getName(), "showSettings");

        // VIEW
        addMenu("View", null,KeyEvent.VK_V);
        // Items VIEW - TOOLBAR
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Toolbar");
        cbMenuItem.setMnemonic(KeyEvent.VK_T);
        cbMenuItem.setState(true);
        addMenuItem("View/Toolbar", cbMenuItem);
        // Items VIEW - STATUS BAR
        cbMenuItem = new JCheckBoxMenuItem("Status Bar");
        cbMenuItem.setMnemonic(KeyEvent.VK_S);
        cbMenuItem.setState(true);
        addMenuItem("View/StatusBar", cbMenuItem);
        // Items VIEW - IMPACTS
        addMenuSeparator("View");
        cbMenuItem = new JCheckBoxMenuItem("Show Impacts");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.setState(false);
        cbMenuItem.setToolTipText("");
        addMenuItem("View/Show Impacts", cbMenuItem);

        // SIMULATION
        addMenu("Simulation", null, KeyEvent.VK_S);
        addMenuItem("Simulation/Step", "Step to next field state", KeyEvent.VK_T, null, null, this.getClass().getName(), "step");
        addMenuSeparator("Simulation");
        addMenuItem("Simulation/Parameters", "Setup game of life parameters", KeyEvent.VK_P, null, null, this.getClass().getName(), "showParameters");

        // HELP
        addMenu("Help", null, KeyEvent.VK_H);
        addMenuItem("Help/About...", null, KeyEvent.VK_A, null, null, this.getClass().getName(), "showAbout");

        setVisible(true);
    }

    public void hello () {
        System.out.println("HELLO");
    }

    public void openFile () {
        System.out.println("Open file");
    }

    public void saveFile () {
        System.out.println("Save file");
    }

    public void saveFileAs () {
        System.out.println("Save file as");
    }

    public void exit () {
        MainWindow window = this;
        dispose();
        //dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSED));
    }

    public void clear () {
        game.clear();
    }

    public void showSettings () {
        System.out.println("Settings");
    }

    public void step () {
        game.tick();
    }

    public void showParameters () {
        System.out.println("Parameters");
    }

    public void showAbout () {
        JOptionPane.showMessageDialog (null, "Game of Life, Azhbakov Artem FIT 13201", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private Container createContentPane(JPanel content) {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        //contentPane.setPreferredSize(new Dimension(sizeX, sizeY));
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        JPanel panel = new JPanel();
        //panel.setPreferredSize(new Dimension(sizeX, sizeY));
        scrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.
        //scrollPane.setPreferredSize(new Dimension(sizeX, sizeY));
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
