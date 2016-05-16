package view;

import javax.swing.*;

/**
 * Created by marting422 on 17.05.2016.
 */
public class ProgressBar extends JFrame {

    JProgressBar pBar;

    public ProgressBar () {
        pBar = new JProgressBar();
        add(pBar);
        pack();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateBar(float newValue) {
        int t = (int)Math.ceil(newValue*100);
        pBar.setValue(t);
        if (t == 100) dispose();
    }
}
