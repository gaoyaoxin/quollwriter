package com.quollwriter.ui;

import java.awt.*;
import java.awt.event.*;

import java.io.File;

import java.net.*;

import java.text.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import com.gentlyweb.utils.*;

import com.gentlyweb.xml.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import com.quollwriter.*;

import com.quollwriter.data.*;
import com.quollwriter.data.comparators.*;

import com.quollwriter.exporter.*;

import com.quollwriter.ui.components.*;
import com.quollwriter.ui.renderers.*;


public class DictionaryManager extends PopupWindow
{

    private JComboBox   exportOthersType = null;
    private JComboBox   exportChaptersType = null;
    private JComboBox   fileType = null;
    private JTextField  fileField = null;
    private JTree       itemsTree = null;
    private JScrollPane itemsTreeScroll = null;
    private Project     proj = null;

    public DictionaryManager(AbstractProjectViewer pv)
    {

        super (pv);

        this.proj = pv.getProject ();

    }

    public String getWindowTitle ()
    {

        return "Manage your personal Dictionary";

    }

    public String getHeaderTitle ()
    {

        return "Manage your personal Dictionary";

    }

    public String getHeaderIconType ()
    {

        return "dictionary";

    }

    public String getHelpText ()
    {

        return null;

    }

    public JComponent getContentPanel ()
    {

        final DictionaryManager _this = this;

        Box b = new Box (BoxLayout.Y_AXIS);

        b.setAlignmentX (Component.LEFT_ALIGNMENT);
        b.setOpaque (true);
        b.setBackground (null);

        b.add (UIUtils.createBoldSubHeader ("New Words",
                                            null));

        JTextPane tp = UIUtils.createHelpTextPane ("Enter the new words to add below, separate the words with commas or semi-colons.");

        tp.setBorder (new EmptyBorder (5,
                                       5,
                                       0,
                                       5));

        b.add (tp);

        final JTextField newWords = UIUtils.createTextField ();
        newWords.setAlignmentX (Component.LEFT_ALIGNMENT);

        // Get the words.
        File userDict = Environment.getUserDictionaryFile ();

        Vector<Vector> words = new Vector ();

        String w = null;

        try
        {

            w = IOUtils.getFile (userDict);

        } catch (Exception e)
        {

            Environment.logError ("Unable to get user dictionary file: " +
                                  userDict,
                                  e);

        }

        StringTokenizer tt = new StringTokenizer (w,
                                                  String.valueOf ('\n'));

        java.util.List<String> wwords = new ArrayList ();

        while (tt.hasMoreTokens ())
        {

            wwords.add (tt.nextToken ());

        }

        Collections.sort (wwords);

        for (String i : wwords)
        {

            Vector v = new Vector ();
            v.add (i);
            words.add (v);

        }

        Vector<String> cols = new Vector ();
        cols.add ("Word");

        final JTable wordTable = new JTable ();

        wordTable.setModel (new DefaultTableModel (words,
                                                   cols)
            {

                public boolean isCellEditable (int row,
                                               int col)
                {

                    return false;

                }

            });

        Box fb = new Box (BoxLayout.X_AXIS);
        fb.setAlignmentX (Component.LEFT_ALIGNMENT);
        fb.add (newWords);

        fb.setBorder (new EmptyBorder (5,
                                       5,
                                       5,
                                       5));
        b.add (fb);

        Box buts = new Box (BoxLayout.X_AXIS);

        final JButton add = new JButton ("Add");

        buts.add (add);

        final ActionAdapter aa = new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {

                String n = newWords.getText ();

                StringTokenizer t = new StringTokenizer (n,
                                                         ",;");

                while (t.hasMoreTokens ())
                {

                    String w = t.nextToken ().trim ();

                    _this.projectViewer.addWordToDictionary (w,
                                                             "user");

                    _this.projectViewer.fireProjectEvent (ProjectEvent.PERSONAL_DICTIONARY,
                                                          ProjectEvent.ADD_WORD,
                                                          w);

                    DefaultTableModel m = (DefaultTableModel) wordTable.getModel ();

                    Vector r = new Vector ();
                    r.add (w);
                    m.insertRow (0,
                                 r);

                }

                newWords.setText ("");

            }

        };

        add.addActionListener (aa);

        newWords.addKeyListener (new KeyAdapter ()
            {

                public void keyPressed (KeyEvent ev)
                {

                    if (ev.getKeyCode () == KeyEvent.VK_ENTER)
                    {

                        aa.actionPerformed (null);

                    }

                }

            });

        buts.setAlignmentX (Component.LEFT_ALIGNMENT);
        buts.setBorder (new EmptyBorder (0,
                                         5,
                                         20,
                                         5));

        b.add (buts);

        b.add (UIUtils.createBoldSubHeader ("Words in Dictionary",
                                            null));
        b.add (Box.createVerticalStrut (5));

        wordTable.setAlignmentX (Component.LEFT_ALIGNMENT);
        wordTable.setOpaque (false);
        wordTable.setFillsViewportHeight (true);
        wordTable.setBorder (null);
        wordTable.setTableHeader (null);

        final JScrollPane ppsp = new JScrollPane (wordTable);

        // ppsp.setBorder (null);
        ppsp.setOpaque (false);
        ppsp.setAlignmentX (Component.LEFT_ALIGNMENT);
        ppsp.setBorder (new CompoundBorder (new EmptyBorder (5,
                                                             5,
                                                             5,
                                                             5),
                                            ppsp.getBorder ()));

        ppsp.setPreferredSize (new Dimension (500,
                                              200));
        ppsp.getViewport ().setOpaque (false);

        b.add (ppsp);

/*
        ppsp.setMinimumSize (new Dimension (500,
                                            t.getRowHeight () * 10));
 */
/*
        t.setPreferredScrollableViewportSize (new Dimension (-1,
                                                             t.getRowHeight () * 10));
 */


        buts = new Box (BoxLayout.X_AXIS);

        final JButton remove = new JButton ("Remove Selected");

        buts.add (remove);

        remove.addActionListener (new ActionAdapter ()
            {

                public void actionPerformed (ActionEvent ev)
                {

                    DefaultTableModel m = (DefaultTableModel) wordTable.getModel ();

                    int[] selection = wordTable.getSelectedRows ();

                    for (int i = selection.length - 1; i > -1; i--)
                    {

                        _this.projectViewer.removeWordFromDictionary (m.getValueAt (selection[i],
                                                                                    0).toString (),
                                                                      "user");

                        // Remove the row.
                        m.removeRow (selection[i]);

                    }

                    // Clear the selection.
                    // ((DefaultListSelectionModel) wordTable.getSelectionModel ()).clearSelection ();

                }

            });

        buts.setAlignmentX (Component.LEFT_ALIGNMENT);
        buts.setBorder (new EmptyBorder (0,
                                         5,
                                         20,
                                         5));

        b.add (buts);

        return b;

    }

    public JButton[] getButtons ()
    {

        final DictionaryManager _this = this;

        JButton b = new JButton ("Finish");

        b.addActionListener (new ActionAdapter ()
            {

                public void actionPerformed (ActionEvent ev)
                {

                    _this.close ();

                }

            });

        JButton[] buts = new JButton[1];
        buts[0] = b;

        return buts;

    }

}