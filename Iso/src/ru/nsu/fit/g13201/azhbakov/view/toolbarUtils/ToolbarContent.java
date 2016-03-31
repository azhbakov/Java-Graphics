package ru.nsu.fit.g13201.azhbakov.view.toolbarUtils;

import javax.swing.*;

/**
 * Created by marting422 on 10.03.2016.
 */
public class ToolbarContent {
    public final Action action;
    public final boolean separateAfter;

    public ToolbarContent(Action action, boolean separateAfter) {
        this.action = action;
        this.separateAfter = separateAfter;
    }
}
