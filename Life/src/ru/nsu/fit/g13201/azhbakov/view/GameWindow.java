package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.game_of_life.Game;
import ru.nsu.fit.g13201.azhbakov.view.hex.HexGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Created by marting422 on 24.02.2016.
 */
public class GameWindow extends MainWindow implements Observer {
    final String ABOUT = "Game of Life, Azhbakov Artem FIT 13201, 2016\n\nProgram designed to visualize classic Conway's Game of Life, " +
            "cellular automaton, as part of NSU CG course.";

    Game game;
    JScrollPane scrollPane;
    HexGrid hexGrid;
    JToolBar toolBar;
    JLabel statusBar;
    File currentFile = null;
    // Actions
    Action newAction, openAction, saveAction, saveAsAction, exitAction;
    Action xorAction, replaceAction, clearAction, preferencesAction, toolbarAction, statusBarAction;
    Action showImpactsAction, runAction, stepAction, settingsAction, aboutAction;

    public GameWindow (Game game) throws Exception{
        super(800, 600, "Game of Life");
        this.game = game;
        game.addObserver(this);

        hexGrid = new HexGrid(game);
        add(new JScrollPane(hexGrid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                if (2 == promptToSave()) {
                    return;
                }
                System.exit(0);
            }
        });

        newAction = new NewAction();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        exitAction = new ExitAction();
        xorAction = new XORAction();
        replaceAction = new ReplaceAction();
        clearAction = new ClearAction();
        preferencesAction = new PreferencesAction();
        toolbarAction = new ToolbarAction();
        statusBarAction = new StatusBarAction();
        showImpactsAction = new ShowImpactsAction();
        runAction = new RunAction();
        stepAction = new StepAction();
        settingsAction = new SettingsAction();
        aboutAction = new AboutAction ();

        ToolBarContent[] toolBarContents = {
                new ToolBarContent(newAction, false),
                new ToolBarContent(openAction, false),
                new ToolBarContent(saveAction, true),

                new ToolBarContent(preferencesAction, false),
                new ToolBarContent(replaceAction, false),
                new ToolBarContent(xorAction, false),
                new ToolBarContent(showImpactsAction, true),

                new ToolBarContent(clearAction, false),
                new ToolBarContent(stepAction, false),
                new ToolBarContent(runAction, true),

                new ToolBarContent(aboutAction, false),
                new ToolBarContent(exitAction, false)};
        toolBar = createToolBar(toolBarContents);
        add(toolBar, BorderLayout.PAGE_START);

        statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        createFileMenu();
        createEditMenu ();
        createViewMenu ();
        createSimulationMenu ();
        createHelpMenu ();

        setVisible(true);
    }

    private class ToolBarContent {
        public final Action action;
        public final boolean separateAfter;

        public ToolBarContent(Action action, boolean separateAfter) {
            this.action = action;
            this.separateAfter = separateAfter;
        }
    }

    private JToolBar createToolBar (ToolBarContent[] contents) {
        JToolBar toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setVisible(true);

        for (final ToolBarContent c : contents) {
            final JButton button = new JButton(c.action);
            button.setText(null);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (button.getToolTipText() != null)
                        statusBar.setText(button.getToolTipText());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    statusBar.setText("Ready");
                }
            });
            toolBar.add(button);
            if (c.separateAfter) toolBar.addSeparator();
        }
        return toolBar;
    }


    private JLabel createStatusBar () {
        JLabel statusBar = new JLabel();
        statusBar.setText("Ready");
        statusBar.setVisible(true);
        return statusBar;
    }

    public void update (Observable observable, Object obj) {
        stepAction.setEnabled(game.modAllowed());
        clearAction.setEnabled(game.modAllowed());
    }

    @Override
    public JMenuItem addMenuItem(JMenuItem item, String title, final Action action) throws SecurityException, ClassNotFoundException, NoSuchMethodException {
        final JMenuItem i = super.addMenuItem(item, title, action);
        i.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (i.getToolTipText() != null)
                    statusBar.setText(i.getToolTipText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                statusBar.setText("Ready");
            }
        });
        return i;
    }

    private void createFileMenu () throws ClassNotFoundException, NoSuchMethodException {
        addMenu("File", null, KeyEvent.VK_F);
        addMenuItem(null, "File/New", newAction);
        addMenuItem(null, "File/Open", openAction);
        addMenuItem(null, "File/Save", saveAction);
        addMenuItem(null, "File/Save as...", saveAsAction);
        addMenuSeparator("File");
        addMenuItem(null, "File/Exit", exitAction);
    }

    private void createEditMenu () throws ClassNotFoundException, NoSuchMethodException {
        // EDIT
        addMenu("Edit", null, KeyEvent.VK_E);

        ButtonGroup group = new ButtonGroup();
        // EDIT - MODES - XOR
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("XOR");
        rbMenuItem.setSelected(false);
        group.add(rbMenuItem);
        addMenuItem(rbMenuItem, "Edit/XOR", xorAction);

        // EDIT - MODES - REPLACE
        rbMenuItem = new JRadioButtonMenuItem("Replace");
        rbMenuItem.setSelected(game.getClickMode() == ClickMode.REPLACE);
        group.add(rbMenuItem);
        addMenuItem(rbMenuItem, "Edit/Replace", replaceAction);

        addMenuSeparator("Edit");
        addMenuItem(null, "Edit/Clear", clearAction);
        addMenuItem(null, "Edit/Preferences", preferencesAction);

    }

    private void createViewMenu () throws ClassNotFoundException, NoSuchMethodException {
        // VIEW
        addMenu("View", null,KeyEvent.VK_V);
        // Items VIEW - TOOLBAR
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Toolbar");
        cbMenuItem.setState(true);
        addMenuItem(cbMenuItem, "View/Toolbar", toolbarAction);
        // Items VIEW - STATUS BAR
        cbMenuItem = new JCheckBoxMenuItem("Status Bar");
        cbMenuItem.setState(true);
        addMenuItem(cbMenuItem, "View/StatusBar", statusBarAction);
        // Items VIEW - IMPACTS
        addMenuSeparator("View");
        cbMenuItem = new JCheckBoxMenuItem("Show Impacts");
        cbMenuItem.setState(game.getShowImpacts());
        cbMenuItem.setToolTipText("");
        addMenuItem(cbMenuItem, "View/Show Impacts", showImpactsAction);
    }

    private void createSimulationMenu () throws ClassNotFoundException, NoSuchMethodException {
        // SIMULATION
        addMenu("Simulation", null, KeyEvent.VK_S);
        addMenuItem(null, "Simulation/Step", stepAction);
        // Items SIMULATION - RUN
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Run");
        cbMenuItem.setState(false);
        addMenuItem(cbMenuItem, "Simulation/Run", runAction);
        addMenuSeparator("Simulation");
        addMenuItem(null, "Simulation/Settings",settingsAction);

    }

    private void createHelpMenu () throws ClassNotFoundException, NoSuchMethodException {
        // HELP
        addMenu("Help", null, KeyEvent.VK_H);
        addMenuItem(null, "Help/About...", aboutAction);
    }

    //
    // ACTIONS
    //
    public class XORAction extends AbstractAction {
        public XORAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("XOR");
            String desc = "Switch cell state on click";
            int mnemonic = KeyEvent.VK_X;
            KeyStroke keyStroke = null;
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);;
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/xor.png"));
        }
        public void actionPerformed(ActionEvent e) {
            game.setClickMode (ClickMode.XOR);
        }
    }

    public class ReplaceAction extends AbstractAction {
        public ReplaceAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Replace");
            String desc = "Cell will be set to \"alive\" state on mouse click";
            int mnemonic = KeyEvent.VK_R;
            KeyStroke keyStroke = null;
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/replace.png"));
        }
        public void actionPerformed(ActionEvent e) {
            game.setClickMode (ClickMode.REPLACE);
        }
    }

    public class NewAction extends AbstractAction {
        public NewAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("New");
            String desc = "Select file with field preset";
            int mnemonic = KeyEvent.VK_N;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control N");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/new.png"));
        }
        public void actionPerformed(ActionEvent e) {
            newFile ();
        }
    }
    public void newFile () {
        switch (promptToSave()) {
            case 0:
                saveFile();
                break;
            case 1:
                break;
            case 2:
                return;
        };
        currentFile = null;
        //applySettings(new GameSettings(10, 10, 10, 25, 0, null, null));
        game.clear();
    }
    public int promptToSave () {
        if (!game.hasUnsavedChanges()) return 1;
        return JOptionPane.showOptionDialog(this,
                "Would you like to save file?",
                "Save file?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
    }

    public class OpenAction extends AbstractAction {
        public OpenAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Open");
            String desc =  "Select field config from disk";
            int mnemonic = KeyEvent.VK_O;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control O");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            openFile ();
        }
    }
    public void openFile () {
        File f = FileUtils.getOpenFileName(this, "txt", "Text files");
        if (f == null) return;
        try {
            game.loadFile(f);
            statusBar.setText("File opened");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Bad file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        currentFile = f;
    }

    public class SaveAction extends AbstractAction {
        public SaveAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Save");
            String desc =  "Save field to disk";
            int mnemonic = KeyEvent.VK_S;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/save.png"));
        }
        public void actionPerformed(ActionEvent e) {
            saveFile ();
        }
    }
    public void saveFile () {
        try {
            if (currentFile == null)
                saveFileAs();
            if (currentFile == null) return;
            game.saveToFile(currentFile);
            statusBar.setText("Saved");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public class SaveAsAction extends AbstractAction {
        public SaveAsAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Save As...");
            String desc =  "Save field to disk";
            int mnemonic = KeyEvent.VK_A;
            KeyStroke keyStroke = null;
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            //putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            saveFileAs ();
        }
    }
    public void saveFileAs () {
        File f = FileUtils.getSaveFileName(this, "txt", "Text files");
        if (f == null)
            return;
        else {
            currentFile = f;
            saveFile();
        }
    }

    public class ExitAction extends AbstractAction {
        public ExitAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Exit");
            String desc =  "Quit program and return to desktop";
            int mnemonic = KeyEvent.VK_X;
            KeyStroke keyStroke = null;
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/exit.png"));
        }
        public void actionPerformed(ActionEvent e) {
            exit ();
        }
    }
    public void exit () {
        MainWindow window = this;
        //dispose();
        dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    public class SettingsAction extends AbstractAction {
        public SettingsAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Settings");
            String desc = "Edit simulator settings - coefficients, field size";
            int mnemonic = KeyEvent.VK_P;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control P");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            showSettings ();
        }
    }

    public class StepAction extends AbstractAction {
        public StepAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Step");
            String desc = "Calculate next simulation step";
            int mnemonic = KeyEvent.VK_T;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control T");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/step.png"));
        }
        public void actionPerformed(ActionEvent e) {
            step ();
        }
    }
    public void step () {
        game.tick();
    }

    public class RunAction extends AbstractAction {
        public RunAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Run");
            String desc =  "Run simulation";
            int mnemonic = KeyEvent.VK_R;
            KeyStroke keyStroke = null;
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/run.png"));
        }
        public void actionPerformed(ActionEvent e) {
            game.run ();
        }
    }


    public class ClearAction extends AbstractAction {
        public ClearAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Clear");
            String desc = "Remove all cells from the field";
            int mnemonic = KeyEvent.VK_C;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control C");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/clear.png"));
        }
        public void actionPerformed(ActionEvent e) {
            clear ();
        }
    }
    public void clear () {
        game.clear();
    }

    public class PreferencesAction extends AbstractAction {
        public PreferencesAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Preferences");
            String desc = "Edit visual settings - cell size, border width";
            int mnemonic = KeyEvent.VK_T;
            //KeyStroke keyStroke = KeyStroke.getKeyStroke("control T");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/preferences.png"));
        }
        public void actionPerformed(ActionEvent e) {
            showPreferences ();
        }
    }
    public void showPreferences () {
        PreferencesWindow preferencesWindow = new PreferencesWindow(game);
        preferencesWindow.setLocationRelativeTo(this);
    }

    public class ToolbarAction extends AbstractAction {
        public ToolbarAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Toolbar");
            String desc = "Show/hide toolbar";
            int mnemonic = KeyEvent.VK_T;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control T");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            toolBar.setVisible(!toolBar.isVisible());
        }
    }

    public class StatusBarAction extends AbstractAction {
        public StatusBarAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("StatusBar");
            String desc = "Show/hide status bar";
            int mnemonic = KeyEvent.VK_S;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            statusBar.setVisible(!statusBar.isVisible());
        }
    }

    public class ShowImpactsAction extends AbstractAction {
        public ShowImpactsAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Show Impacts");
            String desc = "Show/hide impact values";
            int mnemonic = KeyEvent.VK_I;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control I");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/impacts.png"));
        }
        public void actionPerformed(ActionEvent e) {
            game.setShowImpacts (!game.getShowImpacts());
        }
    }

    public class AboutAction extends AbstractAction {
        public AboutAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("About...");
            String desc = "About this programm";
            int mnemonic = KeyEvent.VK_A;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control A");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Life/icons/about.png"));

        }
        public void actionPerformed(ActionEvent e) {
            showAbout ();
        }
    }
    public void showAbout () {
        JOptionPane.showMessageDialog (null, ABOUT, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSettings () {
        SettingsWindow settingsWindow = new SettingsWindow(game);
        settingsWindow.setLocationRelativeTo(this);
    }
}