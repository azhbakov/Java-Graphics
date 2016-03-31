package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;
import ru.nsu.fit.g13201.azhbakov.view.toolbarUtils.ToolbarContent;
import ru.nsu.fit.g13201.azhbakov.view.toolbarUtils.ToolbarUtils;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 09.03.2016.
 */
public class AppWindow extends JFrame implements Observer {
    final String ABOUT = "Isolines, Azhbakov Artem FIT 13201, 2016\n\nProgram designed to study graphical algoruthms, " +
            ", as part of NSU CG course.";

    Logic logic;
    FastMenu menuBar;

    Action openAction;
    Action aboutAction;

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
        aboutAction = new AboutAction();
    }

    private void initToolbar () {
        ToolbarContent[] toolbarContents = {
                new ToolbarContent(openAction, false),
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
            createColorMenu();
            createHelpMenu();
            menuBar.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createFileMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("File", null, KeyEvent.VK_F);
        menuBar.addMenuItem(null, "File/Open", openAction);
    }

    private void createColorMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("Color", null, KeyEvent.VK_C);
    }

    private void createHelpMenu () throws ClassNotFoundException, NoSuchMethodException {
        // HELP
        menuBar.addMenu("Help", null, KeyEvent.VK_H);
        menuBar.addMenuItem(null, "Help/About...", aboutAction);
    }

    public void update (Observable observable, Object object) {

    }

    //
    // ACTIONS
    //
    public class OpenAction extends AbstractAction {
        public OpenAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Open");
            String desc =  "Select image on the disk";
            int mnemonic = KeyEvent.VK_O;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control O");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            openFile ();
        }
    }
    public void openFile () {
        File f = FileUtils.getOpenFileName(this, "txt", "Text file");
        if (f == null) return;
        try {
            logic.init(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Bad file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
}
