package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.Model.BMP.BMPReader;
import ru.nsu.fit.g13201.azhbakov.Model.BMP.BadFileException;
import ru.nsu.fit.g13201.azhbakov.Model.Downscale;
import ru.nsu.fit.g13201.azhbakov.Model.FastMenu;
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
    final String ABOUT = "Game of Life, Azhbakov Artem FIT 13201, 2016\n\nProgram designed to study graphical algoruthms, " +
            ", as part of NSU CG course.";

    Logic logic;
    Zone zoneA; JLabel labelA; ImageIcon imageA;
    Zone zoneB; JLabel labelB; ImageIcon imageB;
    Zone zoneC; JLabel labelC; ImageIcon imageC;
    FastMenu menuBar;
    // Actions
    Action newAction, openAction, saveAction, saveAsAction, exitAction;
    Action atobAction, ctobAction;
    Action grayscaleAction, negativeAction, lerpAction;
    Action floydDitheringAction, orderedDitheringAction, sobelAction, robertsAction;
    Action smoothAction, sharpenAction, stampingAction, aquaAction;
    Action gammaAction, rotateAction, pixelizeAction;
    Action aboutAction;

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
        sharpenAction = new SharpenAction();
        stampingAction = new StampingAction();
        aquaAction = new AquaAction();

        gammaAction = new GammaAction();
        rotateAction = new RotateAction();

        pixelizeAction = new PixelizeAction();

        aboutAction = new AboutAction();
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
                new ToolbarContent(smoothAction, false),
                new ToolbarContent(sharpenAction, false),
                new ToolbarContent(stampingAction, false),
                new ToolbarContent(aquaAction, true),
                new ToolbarContent(gammaAction, false),
                new ToolbarContent(rotateAction, true),
                new ToolbarContent(pixelizeAction, false),
                new ToolbarContent(aboutAction, false),
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

    private void initMenu () {
        menuBar = new FastMenu();
        setJMenuBar(menuBar);
        try {
            createFileMenu();
            createEdgeDetectionMenu();
            createColorMenu();
            createImageMenu();
            createHelpMenu();
            menuBar.setVisible(true);
        } catch (Exception ex) {
            System.out.println("A");
        }
    }

    private void createFileMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("File", null, KeyEvent.VK_F);
        menuBar.addMenuItem(null, "File/New", newAction);
        menuBar.addMenuItem(null, "File/Open", openAction);
        menuBar.addMenuItem(null, "File/Save", saveAction);
        menuBar.addMenuItem(null, "File/Save as...", saveAsAction);
        menuBar.addMenuSeparator("File");
        menuBar.addMenuItem(null, "File/Exit", exitAction);
    }

    private void createEdgeDetectionMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("Edge Detection", null, KeyEvent.VK_E);
        menuBar.addMenuItem(null, "Edge Detection/Sobel Filter", sobelAction);
        menuBar.addMenuItem(null, "Edge Detection/Roberts Filter", robertsAction);
    }

    private void createColorMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("Color", null, KeyEvent.VK_C);
        menuBar.addMenuItem(null, "Color/Grayscale", grayscaleAction);
        menuBar.addMenuItem(null, "Color/Negative", negativeAction);
        menuBar.addMenuItem(null, "Color/Gamma correction", gammaAction);
        menuBar.addMenuSeparator("Color");
        menuBar.addMenuItem(null, "Color/Floyd-Steinberg dithering", floydDitheringAction);
        menuBar.addMenuItem(null, "Color/Ordered dithering", orderedDitheringAction);
    }

    private void createImageMenu () throws ClassNotFoundException, NoSuchMethodException {
        menuBar.addMenu("Image", null, KeyEvent.VK_I);
        menuBar.addMenuItem(null, "Image/Zoom", lerpAction);
        menuBar.addMenuItem(null, "Image/Rotate", rotateAction);
        menuBar.addMenuSeparator("Image");
        menuBar.addMenuItem(null, "Image/Blur", smoothAction);
        menuBar.addMenuItem(null, "Image/Sharpen", sharpenAction);
        menuBar.addMenuItem(null, "Image/Stamping", stampingAction);
        menuBar.addMenuItem(null, "Image/Watercolor", aquaAction);
        menuBar.addMenuSeparator("Image");
        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Pixelize");
        cbMenuItem.setState(logic.isPixelized());
        menuBar.addMenuItem(cbMenuItem, "Image/Pixelize", pixelizeAction);
    }

    private void createHelpMenu () throws ClassNotFoundException, NoSuchMethodException {
        // HELP
        menuBar.addMenu("Help", null, KeyEvent.VK_H);
        menuBar.addMenuItem(null, "Help/About...", aboutAction);
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
    }
    private void checkBActions () {
        boolean enabled;
        if (logic.getImageB() != null) {
            enabled = true;
        } else {
            enabled = false;
        }
        saveAction.setEnabled(enabled);
        saveAsAction.setEnabled(enabled);
        atobAction.setEnabled(enabled);
        grayscaleAction.setEnabled(enabled);
        negativeAction.setEnabled(enabled);
        lerpAction.setEnabled(enabled);
        floydDitheringAction.setEnabled(enabled);
        orderedDitheringAction.setEnabled(enabled);
        sobelAction.setEnabled(enabled);
        robertsAction.setEnabled(enabled);
        smoothAction.setEnabled(enabled);
        sharpenAction.setEnabled(enabled);
        stampingAction.setEnabled(enabled);
        aquaAction.setEnabled(enabled);
        gammaAction.setEnabled(enabled);
        rotateAction.setEnabled(enabled);
        pixelizeAction.setEnabled(enabled);
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
            } catch (Exception ex) {
                ex.printStackTrace();
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
            super("Sobel Filter");
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
            super("Roberts Filter");
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
            super("Blur");
            String desc =  "Apply blur";
            int mnemonic = KeyEvent.VK_U;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control U");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.smoothing();
        }
    }

    public class SharpenAction extends AbstractAction {
        public SharpenAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Sharpening");
            String desc =  "Sharpen image";
            int mnemonic = KeyEvent.VK_P;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control P");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.sharpening();
        }
    }

    public class StampingAction extends AbstractAction {
        public StampingAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Stamping");
            String desc =  "Apply stamp effect";
            int mnemonic = KeyEvent.VK_M;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control M");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.stamping();
        }
    }

    public class AquaAction extends AbstractAction {
        public AquaAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Aqua");
            String desc =  "Apply watercolor effect";
            int mnemonic = KeyEvent.VK_Q;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control Q");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.aqua();
        }
    }

    public class GammaAction extends AbstractAction {
        public GammaAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Gamma");
            String desc =  "Apply gamma correction";
            int mnemonic = KeyEvent.VK_G;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control G");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.gamma();
        }
    }

    public class RotateAction extends AbstractAction {
        public RotateAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Rotate");
            String desc =  "Rotate image";
            int mnemonic = KeyEvent.VK_R;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control R");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.rotate();
        }
    }

    public class PixelizeAction extends AbstractAction {
        public PixelizeAction (/*String text, String desc, int mnemonic, KeyStroke keyStroke*/){
            super("Pixelize");
            String desc =  "Pixelize image";
            int mnemonic = KeyEvent.VK_Z;
            KeyStroke keyStroke = KeyStroke.getKeyStroke("control Z");
            putValue(ACCELERATOR_KEY, keyStroke);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(SMALL_ICON, new ImageIcon("./Filter/icons/load.png"));
        }
        public void actionPerformed(ActionEvent e) {
            logic.pixelize();
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
