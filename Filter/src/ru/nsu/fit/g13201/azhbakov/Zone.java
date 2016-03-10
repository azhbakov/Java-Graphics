package ru.nsu.fit.g13201.azhbakov;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marting422 on 09.03.2016.
 */
public class Zone extends JPanel {
    JLabel zoneName;
    Dimension dimension;
    int border;

    public Zone(String name, Dimension dimension, int border) {
        super();
        this.dimension = new Dimension(dimension.width+border, dimension.height+border);
        this.border = border;
        setMaximumSize(this.dimension);
        setPreferredSize(this.dimension);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new FlowLayout(FlowLayout.LEFT, border, border));

        if (name != null) {
            zoneName = new JLabel(name);
        }
    }

    @Override
    public Component add (Component component) {
        removeAll();
        if (component != null) {
            super.add(component);
        } else {
            if (zoneName != null) {
                super.add(zoneName);
            }
        }
        validate();
        return component;
    }

}
