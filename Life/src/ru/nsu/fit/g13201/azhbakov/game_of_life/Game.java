package ru.nsu.fit.g13201.azhbakov.game_of_life;

import ru.nsu.fit.g13201.azhbakov.view.ClickMode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Martin on 09.02.2016.
 */
public class Game extends Observable {
    private float LIVE_BEGIN = 2.0f;
    private float LIVE_END = 3.3f;
    private float BIRTH_BEGIN = 2.3f;
    private float BIRTH_END = 2.9f;
    private float FST_IMPACT = 1.0f;
    private float SND_IMPACT = 0.3f;
    private int minWidth = 2, width = 10, maxWidth = 100;
    private int minHeight = 1, height = 10, maxHeight = 100;
    private int minCellSize = 10, cellSize = 25, maxCellSize = 100;
    private int minBorderWidth = 1, borderWidth = 5, maxBorderWidth = 100;
    private Timer timer;
    int runSpeed = 1000;
    private boolean allowMod = true;
    private boolean unsavedChanges = false;

    private boolean showImpacts = false;

    private ClickMode clickMode = ClickMode.REPLACE;
    private ClickMode prevClickMode = ClickMode.REPLACE;

    private ArrayList<ArrayList<Cell>> field;
    private ArrayList<ArrayList<Float>> impacts;

    class Vector2 {
        public int x, y;
        public Vector2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private ArrayList<Vector2> fst_neighbors, snd_neighbors;

    private Game () {}
    public Game (int width, int height) {
        this.height = height;
        this.width = width;
        field = new ArrayList<ArrayList<Cell>>();
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> line = new ArrayList<Cell>();
            for (int x = 0; x < getLineWidth(y); x++) { // TODO check if it works
                line.add(new Cell());
            }
            field.add(line);
        }

        fst_neighbors = new ArrayList<Vector2>();
        fst_neighbors.add(new Vector2(0, 1));
        fst_neighbors.add(new Vector2(1, 1));

        fst_neighbors.add(new Vector2(-1, 0));
        fst_neighbors.add(new Vector2(1, 0));

        fst_neighbors.add(new Vector2(0, -1));
        fst_neighbors.add(new Vector2(1, -1));

        snd_neighbors = new ArrayList<Vector2>();
        snd_neighbors.add((new Vector2(0, 2)));

        snd_neighbors.add((new Vector2(-1, 1)));
        snd_neighbors.add((new Vector2(2, 1)));

        snd_neighbors.add((new Vector2(-1, -1)));
        snd_neighbors.add((new Vector2(2, -1)));

        snd_neighbors.add((new Vector2(0, -2)));

        impacts = new ArrayList<ArrayList<Float>>();
        for (int y = 0; y < height; y++) {
            ArrayList<Float> line = new ArrayList<Float>();
            for (int x = 0; x < getLineWidth(y); x++) { // TODO check if it works
                line.add(new Float(0));
            }
            impacts.add(line);
        }
        updateImpacts();
        unsavedChanges = false;

        // TIMER
        timer = new Timer(runSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick ();
                timer.restart();
            }
        });
    }

    // Odd and even rows has different length, this method returns length of certain row
    private int getLineWidth (int y) {
        return  width - (y & 1);
    }

    public boolean isAlive (int x, int y) {
        if (x < 0 || x >= getLineWidth(y)) return false;
        if (y < 0 || y >= height) return false;
        return field.get(y).get(x).getState();
    }

    // Calculate impact value for cell
    public float getImpact (int x, int y) {
        float impact = 0;
        int sign;
        if (getLineWidth(y) < width) sign = 1;
        else sign = -1;
        for (Vector2 vec : fst_neighbors) {
            if (isAlive(x + vec.x*sign, y + vec.y)) impact += FST_IMPACT;
        }

        for (Vector2 vec : snd_neighbors) {
            if (isAlive(x + vec.x*sign, y + vec.y)) impact += SND_IMPACT;
        }
        return impact;
    }

    // Fill impact matrix
    private void updateImpacts () {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < getLineWidth(y); x++) {
                impacts.get(y).set(x, new Float(getImpact(x,y)));
            }
        }
        setChanged();
        notifyObservers();
    }

    // Calculate next simulation step
    public void tick () {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < getLineWidth(y); x++) {
                Cell c = field.get(y).get(x);
                float impact = impacts.get(y).get(x).floatValue();
                if (field.get(y).get(x).getState()) { // cell is alive
                    if (impact <= LIVE_BEGIN || LIVE_END <= impact) c.kill();
                } else { // dead
                    if (BIRTH_BEGIN <= impact && impact <= BIRTH_END) c.liven();
                }
            }
        }
        updateImpacts();
    }

    // Provide console representation
    public void print () {
        for (int y = 0; y < height; y++) {
            if (getLineWidth(y) < width) System.out.print(" ");
            for (int x = 0; x < getLineWidth(y); x++) {
                float impact = impacts.get(y).get(x).floatValue();
                Cell c = field.get(y).get(x);
                System.out.print(x+"("+impact + ") ");
            }
            System.out.println();
        }
    }

    // Leave no alive cells
    public void clear () {
        for (int y = 0; y < height; y++) {
            if (getLineWidth(y) < width) System.out.print(" ");
            for (int x = 0; x < getLineWidth(y); x++) {
                Cell c = field.get(y).get(x);
                c.kill();
            }
        }
        updateImpacts();
    }

    public void liven_cell (int x, int y) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        field.get(y).get(x).liven();
        updateImpacts();
    }

    public void kill_cell (int x, int y) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        field.get(y).get(x).kill();
        updateImpacts();
    }

    public void switch_cell (int x, int y) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        field.get(y).get(x).switchState();
        updateImpacts();
    }

    public void setWidth (int width) {
        int delta;
        if (width < this.width) {
            delta = this.width - width;
            this.width = width;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < delta; x++) {
                    field.get(y).remove(field.get(y).size()-1);
                    impacts.get(y).remove(impacts.get(y).size()-1);
                }
            }
            setChanged();
            notifyObservers();
            return;
        }
        if (width > this.width) {
            delta = width - this.width;
            this.width = width;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < delta; x++) {
                    field.get(y).add(new Cell());
                    impacts.get(y).add(new Float(0));
                    //System.out.println("add " + x + " total width " + width + " adding " + delta + " size " + field.get(y).size());
                }
            }
            setChanged();
            notifyObservers();
            return;
        }
    }

    public void setHeight (int height) { // update can cause multiple graphics repainting when called setHeight and setWidth is series
        int delta;
        if (height < this.height) {
            delta = this.height - height;
            this.height = height;
            for (int y = 0; y < delta; y++) {
                field.remove(field.size()-1);
                impacts.remove(impacts.size()-1);
            }
            setChanged();
            notifyObservers();
            return;
        }
        if (height > this.height) {
            delta = height - this.height;
            this.height = height;
            for (int y = 0; y < delta; y++) {
                field.add(new ArrayList<Cell>());
                impacts.add(new ArrayList<Float>());
                for (int x = 0; x < getLineWidth(field.size()-1); x++) {
                    field.get(field.size()-1).add(new Cell());
                    impacts.get(impacts.size()-1).add(new Float(0));
                    //System.out.println("Adding to line " + (field.size()-1) + " x " + field.get(field.size()-1).size());
                }
            }
            setChanged();
            notifyObservers();
            return;
        }
    }

    public void loadFile (File f) throws FileNotFoundException, BadSettingsException{
        FileInitializer fileInitializer = new FileInitializer(f);
        GameSettings gameSettings = fileInitializer.parseFile();
        if ((minWidth <= gameSettings.cols && gameSettings.cols <= maxWidth) &&
            (minHeight <= gameSettings.rows && gameSettings.rows <= maxHeight) &&
            (minCellSize <= gameSettings.cellSize && gameSettings.cellSize <= maxCellSize) &&
            (minBorderWidth <= gameSettings.borderWidth && gameSettings.borderWidth <= maxBorderWidth)) {
            for (int i = 0; i < gameSettings.n; i++) {
                if (!(0 <= gameSettings.y[i] && gameSettings.y[i] < gameSettings.rows) ||
                !(0 <= gameSettings.x[i] && gameSettings.x[i] < gameSettings.cols)) {
                    throw new BadSettingsException();
                }
            }
            // Everything is correct
            clear();
            setHeight(gameSettings.rows);
            setWidth(gameSettings.cols);
            setCellSize(gameSettings.cellSize);
            setBorderWidth(gameSettings.borderWidth);
            for (int i = 0; i < gameSettings.n; i++) {
                liven_cell(gameSettings.x[i], gameSettings.y[i]);
            }
        } else {
            throw new BadSettingsException();
        }
    }
    public void saveToFile (File f) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        bufferedWriter.write(Integer.toString(width) + " " + Integer.toString(height));
        bufferedWriter.newLine();
        bufferedWriter.write(Integer.toString(borderWidth));
        bufferedWriter.newLine();
        bufferedWriter.write(Integer.toString(cellSize));
        bufferedWriter.newLine();
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isAlive(x, y)) {
                    ar.add(x);
                    ar.add(y);
                }
            }
        }
        bufferedWriter.write(Integer.toString(ar.size()/2));
        bufferedWriter.newLine();
        for (int i = 0; i < ar.size(); i+=2) {
            bufferedWriter.write(ar.get(i).toString() + " " + ar.get(i+1).toString());
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        unsavedChanges = false;
    }


    public void run () {
        if (timer.isRunning()) {
            timer.stop();
            setClickMode(prevClickMode); // TODO save prev state
            allowMod = true;
            //stepAction.setEnabled(true);
        } else {
            timer.start();
            prevClickMode = clickMode;
            setClickMode(ClickMode.DISABLE);
            allowMod = false;
            //stepAction.setEnabled(false);
        }
        setChanged();
        notifyObservers();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getLIVE_BEGIN() {
        return LIVE_BEGIN;
    }

    public float getLIVE_END() {
        return LIVE_END;
    }

    public float getBIRTH_BEGIN() {
        return BIRTH_BEGIN;
    }

    public float getBIRTH_END() {
        return BIRTH_END;
    }

    public float getFST_IMPACT() {
        return FST_IMPACT;
    }

    public float getSND_IMPACT() {
        return SND_IMPACT;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinCellSize() {
        return minCellSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getMaxCellSize() {
        return maxCellSize;
    }

    public int getMinBorderWidth() {
        return minBorderWidth;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public int getMaxBorderWidth() {
        return maxBorderWidth;
    }

    public void setLIVE_BEGIN(float LIVE_BEGIN) {
        this.LIVE_BEGIN = LIVE_BEGIN;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setLIVE_END(float LIVE_END) {
        this.LIVE_END = LIVE_END;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setBIRTH_BEGIN(float BIRTH_BEGIN) {
        this.BIRTH_BEGIN = BIRTH_BEGIN;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setBIRTH_END(float BIRTH_END) {
        this.BIRTH_END = BIRTH_END;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setFST_IMPACT(float FST_IMPACT) {
        this.FST_IMPACT = FST_IMPACT;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setSND_IMPACT(float SND_IMPACT) {
        this.SND_IMPACT = SND_IMPACT;
        updateImpacts();
        setChanged();
        notifyObservers();
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        setChanged();
        notifyObservers();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        setChanged();
        notifyObservers();
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        setChanged();
        notifyObservers();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        setChanged();
        notifyObservers();
    }

    public void setMinCellSize(int minCellSize) {
        this.minCellSize = minCellSize;
        setChanged();
        notifyObservers();
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        setChanged();
        notifyObservers();
    }

    public void setMaxCellSize(int maxCellSize) {
        this.maxCellSize = maxCellSize;
        setChanged();
        notifyObservers();
    }

    public void setMinBorderWidth(int minBorderWidth) {
        this.minBorderWidth = minBorderWidth;
        setChanged();
        notifyObservers();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        setChanged();
        notifyObservers();
    }

    public void setMaxBorderWidth(int maxBorderSize) {
        this.maxBorderWidth = maxBorderSize;
        setChanged();
        notifyObservers();
    }

    public ClickMode getClickMode() {
        return clickMode;
    }

    public void setClickMode(ClickMode clickMode) {
        this.clickMode = clickMode;
        notifyObservers();
    }

    public boolean getShowImpacts() {
        return showImpacts;
    }

    public void setShowImpacts(boolean showImpacts) {
        this.showImpacts = showImpacts;
        setChanged();
        notifyObservers();
    }

    public int getRunSpeed() {
        return runSpeed;
    }

    public void setRunSpeed(int runSpeed) {
        setChanged();
        notifyObservers();
        this.runSpeed = runSpeed;
    }

    public boolean modAllowed() {
        return allowMod;
    }

    public void setAllowMod(boolean allowMod) {
        this.allowMod = allowMod;
        setChanged();
        notifyObservers();
    }

    public void setSimulationParameters (float liveBegin, float liveEnd, float birthBegin, float birthEnd, float fstImpact, float sndImpact) {
        LIVE_BEGIN = liveBegin;
        LIVE_END = liveEnd;
        BIRTH_BEGIN = birthBegin;
        BIRTH_END = birthEnd;
        FST_IMPACT = fstImpact;
        SND_IMPACT = sndImpact;
        setChanged();
        notifyObservers();
    }

    public boolean hasUnsavedChanges () {
        return unsavedChanges;
    }

    @Override
    protected void setChanged () {
        super.setChanged();
        unsavedChanges = true;
    }
}
