package com.quollwriter.ui.panels;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.quollwriter.*;
import com.quollwriter.ui.*;
import com.quollwriter.data.*;

import com.quollwriter.ui.components.Header;

public class BlankQuollPanel extends QuollPanel<AbstractViewer>
{
        
    private String panelId = null;
    
    public BlankQuollPanel (AbstractViewer pv,
                            String         panelId)
    {
        
        super (pv);
        
        this.panelId = panelId;
        
    }

    public String getPanelId ()
    {

        return this.panelId;
    
    }
    
    @Override
    public void close ()
    {
        
    }

    @Override
    public void init ()
    {
/*
        this.add (UIUtils.createHelpTextPane ("<p>You are seeing this because no {chapter}/{asset}/panel has been selected.</p><p>This usually happens when you have just deleted the item you were viewing.</p><p>To continue, just select an item from the sidebar.</p>",
                                              this.projectViewer));
*/

        Header header = UIUtils.createHeader ("No {chapter}/{asset}/panel selected",
                                              Constants.PANEL_TITLE,
                                              Constants.INFO_ICON_NAME,
                                              null);
        
        this.add (header);
        
        JComponent help = UIUtils.createHelpTextPane ("<p>You are seeing this because no {chapter}/{asset}/panel has been selected.</p><p>This usually happens when you have just deleted the item you were viewing.</p><p>To continue, just <a href='action:projectsidebar'>select an item from the sidebar</a>.</p>",
                                                      this.viewer);

        help.setMaximumSize (new Dimension (Short.MAX_VALUE,
                                            Short.MAX_VALUE));
        
        help.setBorder (new EmptyBorder (0, 10, 0, 0));
        
        this.add (help);
        
    }

    public void getState (Map<String, Object> s)
    {
        
    }

    public void setState (Map<String, String> s,
                          boolean             hasFocus)
    {
        
    }

    public String getIconType ()
    {
        
        return "blank";
        
    }

    public String getTitle ()
    {
        
        return "No object selected";
        
    }
    
    public void fillToolBar (JToolBar toolBar,
                             boolean  fullScreen)
    {
                
    }
    
    public void fillPopupMenu (MouseEvent ev,
                               JPopupMenu popup)
    {
        
    }

    public List<Component> getTopLevelComponents ()
    {
        
        return new ArrayList ();
        
    }
    
}