package ru.nsu.fit.g13201.azhbakov.ToolbarUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by marting422 on 10.03.2016.
 */
public class ToolbarUtils {

    public static JToolBar createToolBar (ToolbarContent[] contents, JLabel tipLabel) {
        JToolBar toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setVisible(true);

        for (final ToolbarContent c : contents) {
            final JButton button = new JButton(c.action);
            button.setText(null);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    if (button.getToolTipText() != null && tipLabel != null)
                        tipLabel.setText(button.getToolTipText());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if (tipLabel != null)
                        tipLabel.setText("Ready");
                }
            });
            toolBar.add(button);
            if (c.separateAfter) toolBar.addSeparator();
        }
        return toolBar;
    }
}
