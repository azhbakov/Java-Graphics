package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 09.03.2016.
 */
public class AppWindow extends JFrame implements Observer {
    /*final String ABOUT = "Isolines, Azhbakov Artem FIT 13201, 2016\n\nProgram designed to study graphical algoruthms, " +
            "as part of NSU CG course.";

    Logic logic;
    FastMenu menuBar;

    Action openAction, settingsAction;
    Action aboutAction;
    Action showGridAction, showIsolinesAction, lerpAction;
    JCheckBoxMenuItem showGridCb, showIsolinesCb, lerpCb;

    public AppWindow (Logic logic) {
        super ("Filter");
        this.logic = logic;
        logic.addObserver(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initActions();
        initMenu ();
        initToolbar ();
        initScrollPane ();

        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception e)
        {
            System.out.println("Unable to use Windows look and feel");
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setSize(new Dimension(500, 370));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        update(logic, null);
    }

    private void initActions () {
        openAction = new OpenAction();
        settingsAction = new SettingsAction();
        aboutAction = new AboutAction();
        showGridAction = new ShowGridAction();
        showIsolinesAction = new ShowIsolinesAction();
        lerpAction = new LerpAction();
    }

    private void initToolbar () {
        ToolbarContent[] toolbarContents = {
                new ToolbarContent(openAction, false),
                new ToolbarContent(settingsAction, true),
                new ToolbarContent(showGridAction, false),
                new ToolbarContent(showIsolinesAction, false),
                new ToolbarContent(lerpAction, true),
                new ToolbarContent(aboutAction, false),
        };

        JToolBar toolBar = ToolbarUtils.createToolBar(toolbarContents, null);
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
    }

    private void initScrollPane () {
        //JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        //panel.add(zoneA);
        JPanel content = new JPanel();
        content.add(new Isomap(logic, new Dimension(500, 500)));

        JScrollPane scrollPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private void initMenu () {
        menuBar = new FastMenu();
        setJMenuBar(menuBar);
        try {
            createFileMenu();
            createViewMenu();
            createHelpMenu();
            menuBar.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createFileMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("File", null, KeyEvent.VK_F);
        menuBar.addMenuItem(null, "File/Open", openAction);
        menuBar.addMenuItem(null, "File/Settings", settingsAction);
    }

    private void createViewMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("View", null, KeyEvent.VK_V);

        showGridCb = new JCheckBoxMenuItem("Show grid");
        showGridCb.setState(logic.showGrid());
        menuBar.addMenuItem(showGridCb, "View/Show grid", showGridAction);

        showIsolinesCb = new JCheckBoxMenuItem("Show isolines");
        showIsolinesCb.setState(logic.showIsolines());
        menuBar.addMenuItem(showIsolinesCb, "View/Show isolines", showIsolinesAction);

        lerpCb = new JCheckBoxMenuItem("Enable interpolation");
        lerpCb.setState(logic.lerp());
        menuBar.addMenuItem(lerpCb, "View/Enable interpolation", lerpAction);
    }

    private void createHelpMenu () throws ClassNotFoundException, NoSuchMethodException {
        // HELP
        menuBar.addMenu("Help", null, KeyEvent.VK_H);
        menuBar.addMenuItem(null, "Help/About...", aboutAction);
    }
*/
    public void update (Observable observable, Object object) {
        //showGridCb.setState(logic.showGrid());
        //showIsolinesCb.setState(logic.showIsolines());
        //lerpCb.setState(logic.lerp());
    }
/*
    //
    // ACTIONS
    //
    public class OpenAction extends AbstractAction {
        public OpenAction (){
            super("Open");
            String desc =  "Select image on the disk";
            int mnemonic = KeyEvent.VK_O;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control O");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Iso/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            openFile ();
        }
    }
    public void openFile () {
        File f = FileUtils.getOpenFileName(this, "txt", "Text file");
        if (f == null) return;
        try {
            logic.readSettings(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Bad file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public class AboutAction extends AbstractAction {
        public AboutAction (){
            super("About...");
            String desc = "About this programm";
            int mnemonic = KeyEvent.VK_A;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control A");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Iso/icons/about.png"));

        }
        public void actionPerformed(ActionEvent e) {
            showAbout ();
        }
    }
    public void showAbout () {
        JOptionPane.showMessageDialog (null, ABOUT, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public class ShowGridAction extends AbstractAction {
        public ShowGridAction (){
            super("Show grid");
            String desc = "Show/hide grid";
            int mnemonic = KeyEvent.VK_G;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control G");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Iso/icons/grid.png"));

        }
        public void actionPerformed(ActionEvent e) {
            logic.setShowGrid(!logic.showGrid());
        }
    }


    public class SettingsAction extends AbstractAction {
        public SettingsAction (){
            super("Settings");
            String desc = "Edit settings";
            int mnemonic = KeyEvent.VK_S;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
            //putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Iso/icons/settings.png"));
        }
        public void actionPerformed(ActionEvent e) {
            showSettings();
        }
    }
    public void showSettings () {
        SettingsWindow settingsWindow = new SettingsWindow(logic);
        settingsWindow.setLocationRelativeTo(this);
    }*/
}
