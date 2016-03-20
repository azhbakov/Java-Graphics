package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.Model.BMP.BMPReader;
import ru.nsu.fit.g13201.azhbakov.Model.BMP.BadFileException;
import ru.nsu.fit.g13201.azhbakov.Model.Downscale;
import ru.nsu.fit.g13201.azhbakov.Model.FileUtils;
import ru.nsu.fit.g13201.azhbakov.Model.Logic;
import ru.nsu.fit.g13201.azhbakov.ToolbarUtils.ToolbarContent;
import ru.nsu.fit.g13201.azhbakov.ToolbarUtils.ToolbarUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 09.03.2016.
 */
public class AppWindow extends JFrame implements Observer {
    Logic logic;
    Zone zoneA; JLabel labelA; ImageIcon imageA;
    Zone zoneB; JLabel labelB; ImageIcon imageB;
    Zone zoneC; JLabel labelC; ImageIcon imageC;
    // Actions
    Action newAction, openAction, saveAction, saveAsAction, exitAction;
    Action atobAction, ctobAction;
    Action grayscaleAction, negativeAction, lerpAction;
    Action floydDitheringAction, orderedDitheringAction, sobelAction, robertsAction;
    Action smoothAction;

    public AppWindow (Logic logic) {
        super ("Filter");
        this.logic = logic;
        logic.addObserver(this);

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

        initZones();
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
        update(logic, null);
    }

    private void initZones () {
        zoneA = new Zone(logic.zoneSize, logic.border);
        zoneB = new Zone(logic.zoneSize, logic.border);
        zoneC = new Zone(logic.zoneSize, logic.border);

        imageA = new ImageIcon();
        labelA = new JLabel();
        labelA.setText("Zone A");
        labelA.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (labelA.getIcon() != null) {
                    //System.out.println("PRESSED");
                    logic.selectArea(e.getX(), e.getY());
                }
            }
        });
        labelA.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (labelA.getIcon() != null) {
                    //System.out.println("PRESSED");
                    logic.selectArea(e.getX(), e.getY());
                }
            }
        });
        zoneA.add(labelA);

        imageB = new ImageIcon();
        labelB = new JLabel();
        labelB.setText("Zone B");
        zoneB.add(labelB);

        imageC = new ImageIcon();
        labelC = new JLabel();
        labelC.setText("Zone C");
        zoneC.add(labelC);

        repaint();
    }

    private void initActions () {
        openAction = new OpenAction();
        newAction = new NewAction();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        exitAction = new ExitAction();

        atobAction = new AtoBAction();
        ctobAction = new CtoBAction();

        grayscaleAction = new GrayscaleAction();
        negativeAction = new NegativeAction();
        lerpAction = new LerpAction();

        floydDitheringAction = new FloydDitheringAction();
        orderedDitheringAction = new OrderedDitheringAction();
        sobelAction = new SobelAction();
        robertsAction = new RobertsAction();

        smoothAction = new SmoothAction();
    }

    private void initToolbar () {
        ToolbarContent[] toolbarContents = {
                new ToolbarContent(newAction, false),
                new ToolbarContent(openAction, false),
                new ToolbarContent(saveAction, true),
                new ToolbarContent(atobAction, false),
                new ToolbarContent(ctobAction, false),
                new ToolbarContent(grayscaleAction, false),
                new ToolbarContent(negativeAction, false),
                new ToolbarContent(lerpAction, true),
                new ToolbarContent(floydDitheringAction, false),
                new ToolbarContent(orderedDitheringAction, true),
                new ToolbarContent(sobelAction, false),
                new ToolbarContent(robertsAction, true),
                new ToolbarContent(smoothAction, false)
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

    private void setCurrentFile (File f) {
        if (f == null) {
            setTitle("Filter");
        } else {
            setTitle(f.getName() + " - Filter");
        }
    }

    public void update (Observable observable, Object object) {
        setCurrentFile(logic.getCurrentFile());
        zoneA.setRect(logic.getFrameLeft(), logic.getFrameRight(), logic.getFrameUp(), logic.getFrameBottom());
        checkAActions();
        checkBActions();
        checkCActions();

        if (logic.getImageA() == null) {
            labelA.setIcon(null);
            labelA.setText("Zone A");
        } else {
            imageA.setImage(logic.getImageA());
            labelA.setIcon(imageA);
            labelA.setText(null);
        }
        labelA.revalidate();
        labelA.repaint();

        if (logic.getImageB() == null) {
            labelB.setIcon(null);
            labelB.setText("Zone B");
        } else {
            imageB.setImage(logic.getImageB());
            labelB.setIcon(imageB);
            labelB.setText(null);
        }
        labelB.revalidate();
        labelB.repaint();

        if (logic.getImageC() == null) {
            labelC.setIcon(null);
            labelC.setText("Zone C");
        } else {
            imageC.setImage(logic.getImageC());
            labelC.setIcon(imageC);
            labelC.setText(null);
        }
        labelC.revalidate();
        labelC.repaint();

        //System.out.println("UPDATED");
    }

    private void checkAActions () {
        boolean enabled;
        if (logic.getImageA() != null) {
            enabled = true;
        } else {
            enabled = false;
        }
        saveAction.setEnabled(enabled);
        saveAsAction.setEnabled(enabled);
    }
    private void checkBActions () {
        boolean enabled;
        if (logic.getImageB() != null) {
            enabled = true;
        } else {
            enabled = false;
        }
        atobAction.setEnabled(enabled);
        grayscaleAction.setEnabled(enabled);
        negativeAction.setEnabled(enabled);
        lerpAction.setEnabled(enabled);
        floydDitheringAction.setEnabled(enabled);
        orderedDitheringAction.setEnabled(enabled);
        sobelAction.setEnabled(enabled);
        robertsAction.setEnabled(enabled);
        smoothAction.setEnabled(enabled);
    }
    private void checkCActions () {
        boolean enabled;
        if (logic.getImageC() != null) {
            enabled = true;
        } else {
            enabled = false;
        }
        ctobAction.setEnabled(enabled);
    }

    //
    // ACTIONS
    //
    public class NewAction extends AbstractAction {
        public NewAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("New");
            String desc = "Create a new image from scratch";
            int mnemonic = KeyEvent.VK_N;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control N");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/new.png"));
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
        logic.clear();
    }
    public int promptToSave () {
        if (!logic.hasUnsavedChanges()) return 1;
        return JOptionPane.showOptionDialog(this,
                "Would you like to save file?",
                "Save file?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
    }

    public class SaveAction extends AbstractAction {
        public SaveAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Save");
            String desc =  "Save image to disk";
            int mnemonic = KeyEvent.VK_S;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/save.png"));
        }
        public void actionPerformed(ActionEvent e) {
            saveFile ();
        }
    }
    public void saveFile () {
        try {
            if (logic.getCurrentFile() == null) {
                saveFileAs();
                return;
            }
            logic.saveToFile(logic.getCurrentFile());
            //statusBar.setText("Saved");
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
            try {
                logic.saveToFile(f);
            } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
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
        AppWindow window = this;
        //dispose();
        dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

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
            logic.loadBMP(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Bad file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        setCurrentFile(f);
    }

    public class AtoBAction extends AbstractAction {
        public AtoBAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("A to B");
            String desc =  "Copy image in zone A to zone B";
            int mnemonic = KeyEvent.VK_A;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control A");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/step.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.AtoB();
        }
    }

    public class CtoBAction extends AbstractAction {
        public CtoBAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("C to B");
            String desc =  "Copy image in zone C to zone B";
            int mnemonic = KeyEvent.VK_B;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control B");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/step_back.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.CtoB();
        }
    }

    public class GrayscaleAction extends AbstractAction {
        public GrayscaleAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Grayscale");
            String desc =  "Make image in zone C grayscaled";
            int mnemonic = KeyEvent.VK_O;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control G");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.grayscale();
        }
    }

    public class NegativeAction extends AbstractAction {
        public NegativeAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Negative");
            String desc =  "Make image in zone C negative";
            int mnemonic = KeyEvent.VK_N;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control N");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.negative();
        }
    }

    public class LerpAction extends AbstractAction {
        public LerpAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Lerp");
            String desc =  "Zoom image B center";
            int mnemonic = KeyEvent.VK_L;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control L");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.bilinearLerp();
        }
    }

    public class FloydDitheringAction extends AbstractAction {
        public FloydDitheringAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Dithering");
            String desc =  "Apply Floyd-Steinberg dithering";
            int mnemonic = KeyEvent.VK_Y;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control Y");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.ditheringFloyd();
        }
    }

    public class OrderedDitheringAction extends AbstractAction {
        public OrderedDitheringAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Dithering");
            String desc =  "Apply ordered dithering";
            int mnemonic = KeyEvent.VK_R;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control R");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.ditheringOrdered();
        }
    }

    public class SobelAction extends AbstractAction {
        public SobelAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Edge Detection: Sobel Filter");
            String desc =  "Apply Sobel edge detection";
            int mnemonic = KeyEvent.VK_B;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control B");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.sobel();
        }
    }

    public class RobertsAction extends AbstractAction {
        public RobertsAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Edge Detection: Roberts Filter");
            String desc =  "Apply Roberts edge detection";
            int mnemonic = KeyEvent.VK_R;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control R");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.roberts();
        }
    }

    public class SmoothAction extends AbstractAction {
        public SmoothAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Smoothing");
            String desc =  "Apply smoothing with weighted median operator";
            int mnemonic = KeyEvent.VK_H;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control H");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.smoothing();
        }
    }
}
