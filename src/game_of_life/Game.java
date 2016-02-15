package game_of_life;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

/**
 * Created by Martin on 09.02.2016.
 */
public class Game extends Observable {
    private int width = 10, height = 10;
    private float LIVE_BEGIN = 2.0f;
    private float LIVE_END = 3.3f;
    private float BIRTH_BEGIN = 2.3f;
    private float BIRTH_END = 2.9f;
    private float FST_IMPACT = 1.0f;
    private float SND_IMPACT = 0.3f;

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
    }
    // TODO resize method and copy field method

    private int getLineWidth (int y) {
        return  width - (y & 1);
    }

    public boolean isAlive (int x, int y) {
        if (x < 0 || x >= getLineWidth(y)) return false;
        if (y < 0 || y >= height) return false;
        return field.get(y).get(x).getState();
    }

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

    private void updateImpacts () {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < getLineWidth(y); x++) {
                impacts.get(y).set(x, new Float(getImpact(x,y)));
            }
        }
        setChanged();
        notifyObservers(null);
    }

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

    public void liven_cell (int x, int y) {
        field.get(y).get(x).liven();
        updateImpacts();
    }

    public void kill_cell (int x, int y) {
        field.get(y).get(x).kill();
        updateImpacts();
    }

    public void switch_cell (int x, int y) {
        field.get(y).get(x).switchState();
        updateImpacts();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
