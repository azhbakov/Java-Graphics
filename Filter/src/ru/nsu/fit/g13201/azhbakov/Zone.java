package ru.nsu.fit.g13201.azhbakov;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marting422 on 09.03.2016.
 */
public class Zone extends JPanel {
    //JLabel zoneName;
    Dimension dimension;
    JPanel content;
    int border;

    public Zone(Dimension dimension, int border) {
        super();
        this.dimension = new Dimension(dimension.width+border, dimension.height+border);
        this.border = border;

        setPreferredSize(this.dimension);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BorderLayout(border, border));

        content = new JPanel();
        content.setPreferredSize(dimension);
        content.setLayout(new FlowLayout(FlowLayout.LEFT));
        super.add(content, BorderLayout.CENTER);

//        if (name != null) {
//            zoneName = new JLabel(name);
//        }
//        add((Component) null);
    }

    @Override
    public Component add (Component component) {
        content.removeAll();
        if (component != null) {
            content.add(component);
        }
        validate();
        return component;
    }

}
