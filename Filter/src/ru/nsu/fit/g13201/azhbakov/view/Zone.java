package ru.nsu.fit.g13201.azhbakov.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marting422 on 09.03.2016.
 */
public class Zone extends JPanel {
    //JLabel zoneName;
    Dimension dimension;
    PaintArea content;
    int border;

    public Zone(Dimension dimension, int border) {
        super();
        this.dimension = new Dimension(dimension.width+2*border+1, dimension.height+2*border+1);
        this.border = border;

        setPreferredSize(this.dimension);
        setBorder(BorderFactory.createLineBorder(Color.black));
//        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(new FlowLayout(FlowLayout.LEADING, border, border));
//        super.add(Box.createRigidArea(new Dimension(border,border)));


        content = new PaintArea();
        content.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        super.add(content);

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
            //super.add(component);
        }
        validate();
        return component;
    }

    public void setRect (int left, int right, int up, int bottom) {
        content.setRect(left, right, up, bottom);
    }

}
