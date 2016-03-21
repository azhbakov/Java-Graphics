package ru.nsu.fit.g13201.azhbakov.view;

import javax.swing.*;
import java.security.InvalidParameterException;

/**
 * Created by Martin on 21.03.2016.
 */
public class FastMenu extends JMenuBar {

    public void addMenu (String title, String tooltip, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setToolTipText(tooltip);
        menu.setMnemonic(mnemonic);
        add(menu);
    }

    public JMenu createSubMenu(String title, int mnemonic)
    {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    public void addMenuSeparator(String title)
    {
        MenuElement element = getMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        if(element instanceof JMenu)
            ((JMenu)element).addSeparator();
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    public void addSubMenu(String title, int mnemonic)
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if(element instanceof JMenuBar)
            ((JMenuBar)element).add(subMenu);
        else if(element instanceof JMenu)
            ((JMenu)element).add(subMenu);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    public JMenuItem addMenuItem(JMenuItem item, String title, Action action) throws SecurityException, ClassNotFoundException, NoSuchMethodException
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        if (item == null)
            item = new JMenuItem();
        item.setAction(action);
        item.setIcon(null);
        //fillMenuItem(item, getMenuPathName(title), tooltip, mnemonic, accelerator, icon, classname, actionMethod);
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
        return item;
    }

    private MenuElement getParentMenuElement(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return this;
    }

    public MenuElement getMenuElement(String menuPath)
    {
        MenuElement element = this;
        for(String pathElement: menuPath.split("/"))
        {
            MenuElement newElement = null;
            for(MenuElement subElement: element.getSubElements())
            {
                if((subElement instanceof JMenu && ((JMenu)subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem)subElement).getText().equals(pathElement)))
                {
                    if(subElement.getSubElements().length==1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if(newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    private String getMenuPathName(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return menuPath.substring(pos+1);
        else
            return menuPath;
    }
}
