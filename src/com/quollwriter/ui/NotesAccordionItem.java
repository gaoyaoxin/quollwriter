package com.quollwriter.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.*;
import java.awt.dnd.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.util.*;

import com.quollwriter.data.*;
import com.quollwriter.*;
import com.quollwriter.ui.actionHandlers.*;
import com.quollwriter.ui.components.ActionAdapter;
import com.quollwriter.ui.renderers.*;

public class NotesAccordionItem extends ProjectObjectsAccordionItem<ProjectViewer>
{
        
    public NotesAccordionItem (ProjectViewer pv)
    {
        
        super (Environment.getObjectTypeNamePlural (Note.OBJECT_TYPE),
               Note.OBJECT_TYPE,
               Note.OBJECT_TYPE,
               pv);
            
    }
    
    @Override
    public void reloadTree ()
    {
        
        ((DefaultTreeModel) this.tree.getModel ()).setRoot (UIUtils.createNoteTree (this.projectViewer));

    }
    
    @Override
    public void fillHeaderPopupMenu (JPopupMenu m,
                                     MouseEvent ev)
    {
                
        m.add (UIUtils.createMenuItem ("Add New Type",
                                       Constants.ADD_ICON_NAME,
                                       this.projectViewer.getAction (ProjectViewer.NEW_NOTE_TYPE_ACTION)));

        m.add (UIUtils.createMenuItem ("Manage Types",
                                       Constants.EDIT_ICON_NAME,
                                       this.projectViewer.getAction (ProjectViewer.MANAGE_NOTE_TYPES_ACTION)));

    }    
    
    @Override
    public void initTree ()
    {

        ((DefaultTreeModel) this.tree.getModel ()).setRoot (UIUtils.createNoteTree (this.projectViewer));

    }

    public boolean showItemCountOnHeader ()
    {
        
        return true;
        
    }
    
    public int getItemCount ()
    {
        
        int c = this.projectViewer.getProject ().getAllNamedChildObjects (Note.class).size ();
        
        return c;
                
    }

    @Override
    public void fillTreePopupMenu (JPopupMenu m,
                                   MouseEvent ev)
    {

        final NotesAccordionItem _this = this;

        final TreePath tp = this.tree.getPathForLocation (ev.getX (),
                                                          ev.getY ());

        JMenuItem mi = null;

        if (tp != null)
        {

            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent ();

            final NamedObject d = (NamedObject) node.getUserObject ();

            if (d instanceof TreeParentNode)
            {

                if (!d.getName ().equals (Note.EDIT_NEEDED_NOTE_TYPE))
                {
    
                    m.add (UIUtils.createMenuItem ("Rename",
                                                   Constants.EDIT_ICON_NAME,
                                                   new ActionAdapter ()
                                                   {
                                
                                                        public void actionPerformed (ActionEvent ev)
                                                        {
                                
                                                            DefaultTreeModel dtm = (DefaultTreeModel) _this.tree.getModel ();
                                
                                                            DefaultMutableTreeNode n = (DefaultMutableTreeNode) tp.getLastPathComponent ();
                                
                                                            NamedObject nt = (NamedObject) n.getUserObject ();
                                
                                                            new RenameNoteTypeActionHandler (nt.getName (),
                                                                                             _this.projectViewer).actionPerformed (ev);
                                                                
                                                        }
                                
                                                   }));

                }

                if (node.getChildCount () == 0)
                {

                    m.add (UIUtils.createMenuItem ("Delete",
                                                   Constants.DELETE_ICON_NAME,
                                                   new ActionAdapter ()
                                                   {
                                
                                                        public void actionPerformed (ActionEvent ev)
                                                        {
                                
                                                            DefaultTreeModel dtm = (DefaultTreeModel) _this.tree.getModel ();
                                
                                                            DefaultMutableTreeNode n = (DefaultMutableTreeNode) tp.getLastPathComponent ();
                                
                                                            NamedObject nt = (NamedObject) n.getUserObject ();
                                
                                                            Environment.getUserPropertyHandler (Constants.NOTE_TYPES_PROPERTY_NAME).removeType (nt.getName (),
                                                                                                                                                false);
                                
                                                            dtm.removeNodeFromParent (n);
                                
                                                        }
                                
                                                   }));

                }

            } else
            {

                m.add (UIUtils.createMenuItem ("View",
                                               Constants.VIEW_ICON_NAME,
                                               new ActionAdapter ()
                                               {
                                
                                                    public void actionPerformed (ActionEvent ev)
                                                    {
                                
                                                        _this.projectViewer.viewObject (d);
                                
                                                    }
                                
                                               }));

                m.add (UIUtils.createMenuItem ("Edit",
                                               Constants.EDIT_ICON_NAME,
                                               _this.projectViewer.getAction (ProjectViewer.EDIT_NOTE_ACTION,
                                                                              d)));

                m.add (UIUtils.createMenuItem ("Delete",
                                               Constants.DELETE_ICON_NAME,
                                               _this.projectViewer.getAction (ProjectViewer.DELETE_NOTE_ACTION,
                                                                              d)));

            }

        } 
        
    }
    
    @Override
    public TreeCellEditor getTreeCellEditor (ProjectViewer pv)
    {
        
        return new ProjectTreeCellEditor (pv,
                                          tree);
        
    }
    
    public int getViewObjectClickCount (Object d)
    {
        
        return 1;
        
    }
    
    public boolean isTreeEditable ()
    {
        
        return true;
        
    }
    
    public boolean isDragEnabled ()
    {
        
        return false;
        
    }
    
    @Override
    public DragActionHandler getTreeDragActionHandler (ProjectViewer pv)
    {
        
        return null;
        
    }
        
}