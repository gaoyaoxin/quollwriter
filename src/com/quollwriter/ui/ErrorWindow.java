package com.quollwriter.ui;

import java.awt.Component;
import java.awt.event.*;

import java.util.*;

import java.text.*;

import javax.swing.*;
import javax.swing.border.*;

import com.gentlyweb.utils.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import com.quollwriter.*;

import com.quollwriter.ui.components.*;


public class ErrorWindow extends PopupWindow
{

    private String message = null;

    public ErrorWindow (AbstractViewer v,
                        String         message)
    {

        super (v,
               Component.LEFT_ALIGNMENT);
        
        this.message = message;

    }

    public String getWindowTitle ()
    {

        return "Oops, an error has occurred...";

    }

    public String getHeaderTitle ()
    {

        return "Oops, an error has occurred...";

    }

    public String getHeaderIconType ()
    {

        return Constants.ERROR_ICON_NAME;

    }

    public String getHelpText ()
    {

        return this.message + "<br /><br /><a href='qw:/report-a-bug'>Contact Quoll Writer support about this problem.</a>";

    }

    public void init ()
    {

        super.init ();

    }

    public void setVisible (boolean v)
    {
        
        super.setVisible (v);
        
        if (v)
        {
            
            this.toFront ();
            
        }
        
    }
    
    public JComponent getContentPanel ()
    {

        Box b = new Box (BoxLayout.Y_AXIS);

        return b;

    }

    public JButton[] getButtons ()
    {

        final ErrorWindow _this = this;

        JButton closeBut = new JButton ();
        closeBut.setText ("Close");

        closeBut.addActionListener (new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {

                _this.close ();

            }

        });
        
        JButton[] buts = new JButton[1];
        buts[0] = closeBut;

        return buts;

    }

}
