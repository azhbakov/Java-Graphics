package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.BMP.BMPReader;
import ru.nsu.fit.g13201.azhbakov.BMP.BadFileException;
import ru.nsu.fit.g13201.azhbakov.ToolbarUtils.ToolbarContent;
import ru.nsu.fit.g13201.azhbakov.ToolbarUtils.ToolbarUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by marting422 on 09.03.2016.
 */
public class AppWindow extends JFrame {
    final static Dimension zoneSize = new Dimension(350, 350);
    final static int border = 10;
    File currentFile = null;
    Zone zoneA;
    Zone zoneB;
    Zone zoneC;
    // Actions
    Action openAction;

    public AppWindow () {
        super ();
        setCurrentFile(null);
        zoneA = new Zone("Zone A", zoneSize, border);
        zoneB = new Zone("Zone B", zoneSize, border);
        zoneC = new Zone("Zone C", zoneSize, border);
        initActions();
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
    }

    private void initActions () {
        openAction = new OpenAction();
    }

    private void initToolbar () {
        ToolbarContent[] toolbarContents = {
                new ToolbarContent(openAction, false),
        };

        JToolBar toolBar = ToolbarUtils.createToolBar(toolbarContents, null);
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setRollover(true);
    }

    private void initScrollPane () {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(zoneA);
        panel.add(zoneB);
        panel.add(zoneC);

        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private void loadBMP (File f) throws IOException, BadFileException {
        BufferedImage bufferedImage = BMPReader.readBMP(f);
        JLabel label = new JLabel(new ImageIcon(bufferedImage));
        zoneA.add(label);
        repaint();
    }

    private void setCurrentFile (File f) {
        currentFile = f;
        if (f == null) {
            setTitle("Filter");
        } else {
            setTitle(currentFile.getName() + " - Filter");
        }
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
        File f = FileUtils.getOpenFileName(this, "bmp", "Bitmap Image");
        if (f == null) return;
        try {
            loadBMP(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Bad file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        setCurrentFile(f);
    }
}
