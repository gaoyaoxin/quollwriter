package com.quollwriter.ui;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import com.quollwriter.ui.components.Header;
import com.quollwriter.ui.components.FormItem;
import com.quollwriter.ui.components.IconProvider;
import com.quollwriter.ui.components.GradientPainter;
import com.quollwriter.ui.components.ImagePanel;

import com.quollwriter.*;

public abstract class EditPanel extends Box
{

    public static final int EDIT_VISIBLE = 0;
    public static final int SAVED = 1;
    public static final int CANCELLED = 2;
    public static final int VIEW_VISIBLE = 3;
/*
    public Color headerBorderColor = new Color (171,
                                                171,
                                                171);
*/
    protected JComponent                   editPanel = null;
    protected JComponent                   viewPanel = null;
    protected JComponent                   cancel = null;
    protected JComponent                   edit = null;
    private JComponent                     help = null;
    private JPanel                         helpBox = null;
    protected Header                         header = null;
    private Box                            panel = null;
    private JComponent                     visiblePanel = null;
    private boolean                        contentVisible = true;
    private boolean                        inited = false;
    private java.util.List<ActionListener> listeners = new ArrayList ();

    public EditPanel(boolean allowSingleClickContentClose)
    {

        super (BoxLayout.Y_AXIS);

    }

    protected void fireActionEvent (int    id,
                                    String event)
    {

        ActionEvent ev = new ActionEvent (this,
                                          id,
                                          event);

        for (ActionListener l : this.listeners)
        {

            l.actionPerformed (ev);

        }

    }

    public void removeActionListener (ActionListener l)
    {

        this.listeners.remove (l);

    }

    public void addActionListener (ActionListener l)
    {

        this.listeners.add (l);

    }

    public void init ()
    {

        if (this.inited)
        {

            return;

        }

        final EditPanel _this = this;

        IconProvider ip = this.getIconProvider ();

        this.panel = new Box (BoxLayout.Y_AXIS);
        this.panel.setAlignmentX (Component.LEFT_ALIGNMENT);
        this.add (this.panel);

        this.header = new Header (this.getTitle (),
                                  ip.getIcon ("header",
                                              Constants.ICON_PANEL_SECTION),
                                  null);
/*
        this.header = Header.createBoldSubHeader (this.getTitle (),
                                                  ip.getIcon ("header",
                                                              false));
                                                              */
        this.header.setAlignmentX (Component.LEFT_ALIGNMENT);
        this.header.setOpaque (false);
    
        this.header.setBorder (new CompoundBorder (new MatteBorder (0,
                                                           0,
                                                           1,
                                                           0,
                                                           Environment.getBorderColor ()),
                                          new EmptyBorder (0,
                                                           3,
                                                           3,
                                                           0)
                                          )); 

        this.header.setFont (this.header.getFont ().deriveFont ((float) UIUtils.scaleToScreenSize (14d)).deriveFont (Font.PLAIN));
        this.header.setTitleColor (UIUtils.getTitleColor ());
        this.header.setPaintProvider (new GradientPainter (com.quollwriter.ui.UIUtils.getComponentColor (),
                                                           com.quollwriter.ui.UIUtils.getComponentColor ()));

/*
 Needs work... set size when reopens (store old size, see accordion) also allow for drag open like accordion
        if (allowSingleClickContentClose)
        {

            this.header.setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
            this.header.setToolTipText ("Click to close the section");

            this.header.addMouseListener (new MouseAdapter ()
            {

                public void mousePressed (MouseEvent eve)
                {

                    _this.contentVisible = !_this.contentVisible;

                    if (_this.visiblePanel != null)
                    {

                        _this.visiblePanel.setVisible (_this.contentVisible);

                        if (_this.getParent () instanceof JSplitPane)
                        {

                            JSplitPane sp = ((JSplitPane) _this.getParent ());

                            sp.setDividerLocation (sp.getSize ().height - _this.getPreferredSize ().height - _this.getInsets ().top - _this.getInsets ().bottom);

                        }

                    }

                }

            });

        }
*/
/*
        this.setBorder (new CompoundBorder (UIUtils.internalPanelDropShadow, // new DropShadowBorder (),
                                            com.quollwriter.ui.UIUtils.createLineBorder ()));
*/
        this.setBorder (null);
        this.setOpaque (false);
        this.panel.setOpaque (true);
        this.panel.add (this.header);

        // See if there are view items, if so create a form, otherwise
        // call "getEditPanel".
        java.util.List<FormItem> viewItems = this.getViewItems ();

        if ((viewItems != null) &&
            (viewItems.size () > 0))
        {

            this.viewPanel = this.buildPanel (viewItems,
                                              ip,
                                              false,
                                              false);

        } else
        {

            this.viewPanel = this.getViewPanel ();

        }

        if (this.viewPanel != null)
        {

            this.viewPanel.setBackground (com.quollwriter.ui.UIUtils.getComponentColor ());

            this.panel.add (this.viewPanel);
            this.viewPanel.setVisible (true);
            this.viewPanel.setAlignmentX (Component.LEFT_ALIGNMENT);
            this.viewPanel.setBorder (null);
/*
            this.viewPanel.setBorder (new EmptyBorder (5,
                                                       5,
                                                       5,
                                                       5));
*/
            this.visiblePanel = this.viewPanel;

            this.fireActionEvent (EditPanel.VIEW_VISIBLE,
                                  "view-visible");

        }

        // See if there are edit items, if so create a form, otherwise
        // call "getEditPanel".
        java.util.List<FormItem> editItems = this.getEditItems ();

        if ((editItems != null) &&
            (editItems.size () > 0))
        {

            this.editPanel = this.buildPanel (editItems,
                                              ip,
                                              true,
                                              false);

        } else
        {

            this.editPanel = this.getEditPanel ();

            if (this.editPanel != null)
            {
/*
                this.editPanel.setBorder (new EmptyBorder (3,
                                                           3,
                                                           3,
                                                           3));
*/
                this.editPanel.setMinimumSize (new Dimension (100,
                                                              100));

                this.editPanel = new JScrollPane (this.editPanel);
                this.editPanel.setBorder (null);
                /*
                this.editPanel.setBorder (new EmptyBorder (3,
                                                           3,
                                                           3,
                                                           3));
                */
            }

        }

        if (this.editPanel != null)
        {

            this.editPanel.setOpaque (false);

            this.editPanel.setAlignmentX (Component.LEFT_ALIGNMENT);

            String ht = this.getHelpText ();

            if (ht != null)
            {

                FormLayout fl = new FormLayout ("p, 3px, fill:90px:grow",
                                                "top:p");

                PanelBuilder pb = new PanelBuilder (fl);

                CellConstraints cc = new CellConstraints ();

                ImagePanel helpII = new ImagePanel (ip.getIcon ("help",
                                                                Constants.ICON_PANEL_SECTION_ACTION),
                                                    null);
                helpII.setAlignmentX (Component.LEFT_ALIGNMENT);

                pb.add (helpII,
                        cc.xy (1,
                               1));

                JTextArea helpT = new JTextArea ();
                helpT.setText (this.getHelpText ());
                helpT.setEditable (false);
                helpT.setOpaque (false);
                helpT.setAlignmentX (Component.LEFT_ALIGNMENT);
/*
                helpT.setFont (new Font ("Tahoma",
                                         Font.ITALIC,
                                         11));
*/
                helpT.setWrapStyleWord (true);
                helpT.setLineWrap (true);

                JScrollPane hsp = new JScrollPane (helpT);
                hsp.setOpaque (false);
                hsp.setBorder (null);
                hsp.getViewport ().setOpaque (false);

                pb.add (hsp,
                        cc.xy (3,
                               1));

                this.helpBox = pb.getPanel ();
                this.helpBox.setOpaque (false);
                this.helpBox.setVisible (false);
                this.helpBox.setBorder (new EmptyBorder (0,
                                                         0,
                                                         5,
                                                         0));

                fl = new FormLayout ("fill:100px:grow",
                                     "top:p, fill:100px:grow");

                pb = new PanelBuilder (fl);

                cc = new CellConstraints ();

                pb.add (this.helpBox,
                        cc.xy (1,
                               1));

                pb.add (this.editPanel,
                        cc.xy (1,
                               2));

                this.editPanel = pb.getPanel ();

            }

            this.editPanel.setBorder (new EmptyBorder (5,
                                                       5,
                                                       5,
                                                       5));

            this.panel.add (this.editPanel);

            this.editPanel.setOpaque (false);

            this.editPanel.setVisible (false);
            this.editPanel.setAlignmentX (Component.LEFT_ALIGNMENT);

            java.util.List<JComponent> buttons = new ArrayList ();
            
            this.edit = UIUtils.createButton (ip.getIcon ("edit",
                                                           Constants.ICON_PANEL_SECTION_ACTION),
                                               "Click to edit this section",
                                               null);

            buttons.add (this.edit);

            this.cancel = UIUtils.createButton (ip.getIcon ("cancel",
                                                            Constants.ICON_PANEL_SECTION_ACTION),
                                               "Click to cancel editing",
                                               null);
            this.cancel.setVisible (false);            

            buttons.add (this.cancel);

            if (ht != null)
            {

                this.help = UIUtils.createButton (ip.getIcon ("help",
                                                              Constants.ICON_PANEL_SECTION_ACTION),
                                                   "Click to view/hide the help for this section",
                                                   null);
                this.help.setVisible (false);                

                buttons.add (this.help);

                this.help.addMouseListener (new MouseAdapter ()
                    {

                        public void mousePressed (MouseEvent ev)
                        {

                            _this.helpBox.setVisible (!_this.helpBox.isVisible ());

                            _this.repaint ();

                        }

                    });

            }

            this.header.setControls (UIUtils.createButtonBar (buttons));

            this.edit.addMouseListener (new MouseAdapter ()
                {

                    public void mousePressed (MouseEvent ev)
                    {

                        if (_this.editPanel.isVisible ())
                        {

                            _this.doSave ();

                            _this.fireActionEvent (EditPanel.SAVED,
                                                   "saved");

                            return;

                        }

                        _this.editPanel.setVisible (true);

                        _this.fireActionEvent (EditPanel.EDIT_VISIBLE,
                                               "edit-visible");

                        _this.visiblePanel = _this.editPanel;

                        if (_this.viewPanel != null)
                        {

                            _this.viewPanel.setVisible (false);

                        }

                        _this.edit.setToolTipText ("Click to save the changes");

                        _this.cancel.setVisible (true);
                        _this.help.setVisible (true);

                        _this.handleEditStart ();

                        _this.repaint ();

                    }

                });

            this.cancel.addMouseListener (new MouseAdapter ()
                {

                    public void mousePressed (MouseEvent ev)
                    {

                        if (!_this.handleCancel ())
                        {

                            _this.repaint ();

                            return;

                        }

                        _this.visiblePanel = null;

                        if (_this.viewPanel != null)
                        {

                            _this.viewPanel.setVisible (true);

                            _this.fireActionEvent (EditPanel.CANCELLED,
                                                   "cancelled");

                            _this.fireActionEvent (EditPanel.VIEW_VISIBLE,
                                                   "view-visible");

                            _this.visiblePanel = _this.viewPanel;

                        }

                        _this.editPanel.setVisible (false);
                        _this.edit.setToolTipText ("Click to edit this section");

                        _this.cancel.setVisible (false);
                        _this.help.setVisible (false);
                        _this.helpBox.setVisible (false);

                        _this.repaint ();

                    }

                });

            if (this.viewPanel == null)
            {

                this.editPanel.setVisible (true);
                this.visiblePanel = this.editPanel;

                this.fireActionEvent (EditPanel.EDIT_VISIBLE,
                                      "edit-visible");

            }

        }

        this.inited = true;

    }

    public Header getHeader ()
    {

        this.init ();

        return this.header;

    }

    public Box getPanel ()
    {

        this.init ();

        return this.panel;

    }

    public void repaint ()
    {

        super.repaint ();

        if (this.getParent () != null)
        {

            this.getParent ().repaint ();

        }

    }

    public boolean doSave ()
    {

        if (!this.handleSave ())
        {

            this.repaint ();

            return false;

        }

        this.editPanel.setVisible (false);

        if (this.viewPanel != null)
        {

            this.viewPanel.setVisible (true);

            this.fireActionEvent (EditPanel.VIEW_VISIBLE,
                                  "view-visible");

            this.visiblePanel = this.viewPanel;

        } else
        {

            this.editPanel.setVisible (true);

            this.fireActionEvent (EditPanel.EDIT_VISIBLE,
                                  "edit-visible");

            this.visiblePanel = this.editPanel;

        }

        this.edit.setToolTipText ("Click to edit this section");
        this.cancel.setVisible (false);
        this.help.setVisible (false);
        this.helpBox.setVisible (false);
        this.repaint ();

        return true;

    }

    public abstract void refreshViewPanel ();

    public abstract String getTitle ();

    public abstract String getHelpText ();

    public abstract JComponent getEditPanel ();

    public abstract JComponent getViewPanel ();

    public abstract java.util.List<FormItem> getEditItems ();

    public abstract java.util.List<FormItem> getViewItems ();

    public abstract boolean handleSave ();

    public abstract boolean handleCancel ();

    public abstract void handleEditStart ();

    public abstract IconProvider getIconProvider ();

    private Header createHeader (String       title,
                                 String       iconType,
                                 IconProvider ip,
                                 boolean      bottomBorder)
    {

        Header h = new Header (title,
                               ((iconType == null) ? null : ip.getIcon (iconType,
                                                                        Constants.ICON_PANEL_SECTION)),
                               null);

        h.setFont (h.getFont ().deriveFont (Font.BOLD,
                                            12f));
        h.setTitleColor (UIUtils.getTitleColor ());
        h.setOpaque (false);
        h.setBackground (UIUtils.getComponentColor ());
        h.setPaintProvider (null);

        h.setAlignmentX (Component.LEFT_ALIGNMENT);

        if (bottomBorder)
        {

            h.setBorder (new CompoundBorder (new MatteBorder (0,
                                                              0,
                                                              1,
                                                              0,
                                                              Environment.getBorderColor ()),
                                             new EmptyBorder (0,
                                                              0,
                                                              2,
                                                              0)));

        } else {

            h.setBorder (new EmptyBorder (0,
                                          0,
                                          0,
                                          0));

        }

        return h;

    }

    private JPanel buildPanel (java.util.List<FormItem> items,
                               IconProvider             ip,
                               boolean                  scrollPaneBorder,
                               boolean                  headerBottomBorder)
    {

        StringBuilder rowSpec = new StringBuilder ();

        for (FormItem item : items)
        {

            if (rowSpec.length () > 0)
            {

                rowSpec.append (", 10px, ");

            }

            rowSpec.append ("p, 5px, ");
            rowSpec.append (((item.formatSpec != null) ? item.formatSpec : "p"));

        }

        FormLayout fl = new FormLayout ("3px, 100px:grow, 3px",
                                        rowSpec.toString ());

        PanelBuilder pb = new PanelBuilder (fl);

        CellConstraints cc = new CellConstraints ();

        int row = 1;

        for (FormItem item : items)
        {

            if (item.label != null)
            {
        
                pb.add (this.createHeader (item.label.toString (),
                                           null,
                                           ip,
                                           headerBottomBorder),
                        cc.xywh (1,
                                 row,
                                 3,
                                 1));

            }
                                 
            row += 2;

            Component c = item.component;

            if (c instanceof JComboBox)
            {

                Box tb = new Box (BoxLayout.X_AXIS);
                tb.add (c);
                tb.add (Box.createHorizontalGlue ());

                c = tb;

            }

            if ((item.component instanceof JTextArea) ||
                (item.component instanceof JEditorPane))
            {

                JScrollPane sp = new JScrollPane (item.component);

                if (!scrollPaneBorder)
                {

                    sp.setBorder (null);
                    sp.setOpaque (false);
                    sp.getViewport ().setOpaque (false);

                }

                c = sp;

            }

            if ((!scrollPaneBorder) &&
                (item.component instanceof JTextField))
            {

                ((JComponent) item.component).setBorder (null);
                ((JComponent) item.component).setOpaque (false);

            }

            pb.add (c,
                    cc.xy (2,
                           row));

            row += 2;

        }

        return pb.getPanel ();

    }

}