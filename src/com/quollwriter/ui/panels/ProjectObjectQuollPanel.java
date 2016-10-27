package com.quollwriter.ui.panels;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.gentlyweb.properties.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import com.quollwriter.*;

import com.quollwriter.data.*;

import com.quollwriter.events.*;

import com.quollwriter.ui.*;
import com.quollwriter.ui.actionHandlers.*;

public abstract class ProjectObjectQuollPanel<E extends AbstractProjectViewer> extends QuollPanel<E>
{

    public static final int UNSAVED_CHANGES_ACTION_EVENT = 0;

    public static final int SAVED = 1;

    public static final String HAS_CHANGES_COMMAND = "hasChanges";
    public static final String NO_CHANGES_COMMAND = "noChanges";

    protected Box                                   content = null;
    // TODO: Make this generic...
    protected NamedObject                           obj = null;
    private java.util.List                          actionListeners = new ArrayList ();
    private java.util.List<PropertyChangedListener> propertyChangedListeners = new ArrayList ();
    private boolean                                 hasUnsavedChanges = false;
    private JToolBar                                toolBar = null;
    private boolean                                 readyForUse = false;
    
    public ProjectObjectQuollPanel (E           pv,
                                    NamedObject obj)
    {

        super (pv);
    
        this.obj = obj;
                
    }
    
    @Override
    public E getViewer ()
    {
        
        return this.viewer;
        
    }
    
    public abstract boolean saveUnsavedChanges ()
                                         throws Exception;

    public abstract <T extends NamedObject> void refresh (T n);

    public java.util.List<PropertyChangedListener> getObjectPropertyChangedListeners ()
    {

        return this.propertyChangedListeners;

    }

    public void removeObjectPropertyChangedListener (PropertyChangedListener l)
    {

        this.propertyChangedListeners.remove (l);

    }

    public void addObjectPropertyChangedListener (PropertyChangedListener l)
    {

        this.propertyChangedListeners.add (l);

        this.obj.addPropertyChangedListener (l);

    }

    public NamedObject getForObject ()
    {

        return this.obj;

    }

    public void saveObject ()
                     throws Exception
    {

        this.viewer.saveObject (this.obj,
                                true);

        this.setHasUnsavedChanges (false);

        // Fire an event to interested parties.
        this.fireActionEvent (new ActionEvent (this,
                                               QuollPanel.SAVED,
                                               "chapterSaved"));

    }

    @Override
    public String getPanelId ()
    {

        return this.obj.getObjectReference ().asString ();

    }

    public boolean hasUnsavedChanges ()
    {

        return this.hasUnsavedChanges;

    }

    protected void setHasUnsavedChanges (boolean hasChanges)
    {

        this.hasUnsavedChanges = hasChanges;

        this.fireActionEvent (new ActionEvent (this,
                                               QuollPanel.UNSAVED_CHANGES_ACTION_EVENT,
                                               (hasChanges ? QuollPanel.HAS_CHANGES_COMMAND : QuollPanel.NO_CHANGES_COMMAND)));

    }
    
}
