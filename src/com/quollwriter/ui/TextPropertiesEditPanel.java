package com.quollwriter.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.*;

import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import com.quollwriter.*;

import com.gentlyweb.properties.*;

import com.quollwriter.events.*;
import com.quollwriter.ui.components.ActionAdapter;
import com.quollwriter.ui.components.QPopup;
import com.quollwriter.ui.components.ChangeAdapter;
import com.quollwriter.ui.components.TextProperties;

public class TextPropertiesEditPanel extends Box implements UserPropertyListener
{
    
    public static int MAX_TEXT_BORDER_WIDTH = 150;
    
    private AbstractViewer viewer = null;
    private TextProperties textProps = null;
    private String eventType = null;
    private boolean showColorSelectors = false;
    //private PopupsSupported popupParent = null;

    private JComboBox            fonts = null;
    private JComboBox            sizes = null;
    private JComboBox            align = null;
    private JComboBox            line = null;
    private JCheckBox            indent = null;
    private JCheckBox            highlightWritingLine = null;
    private JCheckBox            typewriterScrolling = null;
    private JPanel               textcolorSwatch = null;
    private JPanel               bgcolorSwatch = null;
    private JPanel               writingLineHighlightColorSwatch = null;
    private JSlider    textBorder = null;

    private Map<String, QPopup>  popups = new HashMap ();
    
    public TextPropertiesEditPanel (AbstractViewer  pv,
                                    TextProperties  props,
                                    String          eventType,
                                    boolean         showColorSelectors,
                                    PopupsSupported popupParent)
    {
        
        super (BoxLayout.Y_AXIS);
        
        this.textProps = props;
        this.eventType = eventType;
        this.viewer = pv;
        this.showColorSelectors = showColorSelectors;
        //this.popupParent = popupParent;
                
    }
        
    @Override
    public void propertyChanged (UserPropertyEvent ev)
    {

        UserPropertySetter set = null;
    
        if (this.textProps instanceof UserPropertySetter)
        {
            
            set = (UserPropertySetter) this.textProps;
            set.stopSetting ();
            
        }
        
        try
        {
    
            this.fonts.setSelectedItem (this.textProps.getFontFamily ());
            this.sizes.setSelectedItem (this.textProps.getFontSize ());
            this.align.setSelectedItem (this.textProps.getAlignment ());
            this.line.setSelectedItem (this.textProps.getLineSpacing ());
            this.textcolorSwatch.setBackground (this.textProps.getTextColor ());
            this.bgcolorSwatch.setBackground (this.textProps.getBackgroundColor ());        
            this.highlightWritingLine.setSelected (this.textProps.isHighlightWritingLine ());
            this.indent.setSelected (this.textProps.getFirstLineIndent ());
            this.writingLineHighlightColorSwatch.setBackground (this.textProps.getWritingLineColor ());
            this.textBorder.setValue (this.textProps.getTextBorder ());
            
        } finally {
            
            if (set != null)
            {
                
                set.startSetting ();
                
            }
            
        }

    }
    
    public Dimension getMinimumSize ()
    {
        
        return this.getPreferredSize ();        
        
    }
    
    public void setTextProperties (TextProperties props)
    {
    
        this.textProps = props;
    
        this.fonts.setSelectedItem (props.getFontFamily ());
        this.sizes.setSelectedItem (props.getFontSize ());
        this.align.setSelectedItem (props.getAlignment ());
        this.line.setSelectedItem (props.getLineSpacing ());
        this.textcolorSwatch.setBackground (props.getTextColor ());
        this.bgcolorSwatch.setBackground (props.getBackgroundColor ());        
        this.highlightWritingLine.setSelected (props.isHighlightWritingLine ());
        this.indent.setSelected (props.getFirstLineIndent ());
        this.textBorder.setValue (props.getTextBorder ());
            
    }
        
    private void addItem (String     label,
                          JComponent comp,
                          JComponent addTo)
    {
        
        if (label != null)
        {
            
            addTo.add (UIUtils.createLabel (label));
            
        }
        
        if (comp != null)
        {
            
            Box b = new Box (BoxLayout.X_AXIS);

            b.setAlignmentX (JComponent.LEFT_ALIGNMENT);
            
            b.setBorder (UIUtils.createPadding ((label != null ? 5 : 0), 10, 10, 0));
            
            comp.setAlignmentX (JComponent.LEFT_ALIGNMENT);
            
            b.add (comp);
           
            addTo.add (b);
            
        }
        
    }
    
    public void init ()
    {

        final TextPropertiesEditPanel _this = this;
    
        Box layout = new Box (BoxLayout.Y_AXIS);
    
        layout.setBorder (UIUtils.createPadding (5, 5, 5, 5));
    
        this.fonts = UIUtils.getFontsComboBox (this.textProps.getFontFamily ());

        this.fonts.addActionListener (new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {            

                _this.fonts.setFont (new Font ((String) _this.fonts.getSelectedItem (),
                                         Font.PLAIN,
                                         12));

                _this.textProps.setFontFamily ((String) _this.fonts.getSelectedItem ());
            
                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_FONT);

            }

        });

                this.fonts.setFont (new Font ((String) this.fonts.getSelectedItem (),
                                         Font.PLAIN,
                                         12));

        this.addItem ("Font",
                      this.fonts,
                      layout);

        this.sizes = UIUtils.getFontSizesComboBox (this.textProps.getFontSize ());

        this.sizes.addActionListener (new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {

                try
                {

                    _this.textProps.setFontSize (Integer.parseInt (_this.sizes.getSelectedItem ().toString ()));
                    
                } catch (Exception e)
                {

                    // Ignore.

                }
            
                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_FONT_SIZE);

            }

        });

        this.addItem ("Size",
                      this.sizes,
                      layout);
    
        this.align = UIUtils.getAlignmentComboBox (this.textProps.getAlignment ());

        this.align.addActionListener (new ActionAdapter ()
        {
    
            public void actionPerformed (ActionEvent ev)
            {

                _this.textProps.setAlignment ((String) _this.align.getSelectedItem ());            
            
                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_ALIGNMENT);

            }

        });

        this.addItem ("Alignment",
                      this.align,
                      layout);

        this.line = UIUtils.getLineSpacingComboBox (this.textProps.getLineSpacing ());
                                                    
        this.line.addActionListener (new ActionAdapter ()
        {
    
            public void actionPerformed (ActionEvent ev)
            {

                try
                {

                    _this.textProps.setLineSpacing (Float.parseFloat (_this.line.getSelectedItem ().toString ()));

                } catch (Exception e)
                {

                    // Ignore.

                }

                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_LINE_SPACING);

            }

        });

        this.addItem ("Line Spacing",
                      this.line,
                      layout);

        this.textBorder = new JSlider (SwingConstants.HORIZONTAL,
                                       0,
                                       TextPropertiesEditPanel.MAX_TEXT_BORDER_WIDTH,
                                       this.textProps.getTextBorder ());
        this.textBorder.setToolTipText ("Drag to change the size of the border between the edge of the writing area and the text");
        this.textBorder.addChangeListener (new ChangeAdapter ()
        {

            public void stateChanged (ChangeEvent ev)
            {

                _this.textProps.setTextBorder (_this.textBorder.getValue ());
            
                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_TEXT_BORDER);                
                

            }

        });

        textBorder.setOpaque (false);
        textBorder.setMaximumSize (new Dimension (150, 20));
                      
        this.addItem ("Text Border Width",
                      textBorder,
                      layout);
                      
        this.indent = new JCheckBox ("<html>Indent the first line of each paragraph</html>");
        this.indent.setVerticalTextPosition (SwingConstants.TOP);
        this.indent.setOpaque (false);
        this.indent.setSelected (this.textProps.getFirstLineIndent ());

        this.indent.addActionListener (new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {

                _this.textProps.setFirstLineIndent (_this.indent.isSelected ());
            
                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_LINE_INDENT);                
                
            }

        });

        this.addItem (null,
                      this.indent,
                      layout);

        this.highlightWritingLine = new JCheckBox ("Highlight the writing line");
        this.highlightWritingLine.setOpaque (false);
        this.highlightWritingLine.setSelected (this.textProps.isHighlightWritingLine ());

        this.highlightWritingLine.addActionListener (new ActionAdapter ()
        {

            public void actionPerformed (ActionEvent ev)
            {

                _this.textProps.setHighlightWritingLine (_this.highlightWritingLine.isSelected ());

                _this.viewer.fireProjectEvent (_this.eventType,
                                               ProjectEvent.CHANGE_HIGHLIGHT_WRITING_LINE);                
                
            }

        });

        this.addItem (null,
                      this.highlightWritingLine,
                      layout);
    
        this.writingLineHighlightColorSwatch = QColorChooser.getSwatch (_this.textProps.getWritingLineColor ());
        
        UIUtils.setAsButton (this.writingLineHighlightColorSwatch);
                            
        this.writingLineHighlightColorSwatch.addMouseListener (new MouseAdapter ()
        {

            public void mouseReleased (MouseEvent ev)
            {
            
                Color writingLineColor = _this.textProps.getWritingLineColor ();
  
                QPopup popup = QColorChooser.getColorChooserPopup ("Select the highlight line color",
                                                                writingLineColor,
                                                                new ChangeAdapter ()
                                                                {
                         
                                                                    public void stateChanged (ChangeEvent ev)
                                                                    {
                        
                                                                        Color c = (Color) ev.getSource ();
                                                                        
                                                                        _this.writingLineHighlightColorSwatch.setBackground (c);
                                                                       
                                                                        _this.textProps.setWritingLineColor (c);

                                                                        _this.viewer.fireProjectEvent (_this.eventType,
                                                                                                       ProjectEvent.CHANGE_HIGHLIGHT_WRITING_LINE);
                                                                        
                                                                    }

                                                                },
                                                                null);                
                            
                popup.setDraggable (_this.viewer);
                
                _this.viewer.showPopupAt (popup,
                                               _this.writingLineHighlightColorSwatch,
                                               true);

            }
            
        });
        
        this.addItem ("Highlight line",
                      this.writingLineHighlightColorSwatch,
                      layout);
        
        if (this.showColorSelectors)
        {

            this.textcolorSwatch = QColorChooser.getSwatch (this.textProps.getTextColor ());
            UIUtils.setAsButton (this.textcolorSwatch);
            this.bgcolorSwatch = QColorChooser.getSwatch (this.textProps.getBackgroundColor ());
            UIUtils.setAsButton (this.bgcolorSwatch);
    
            this.textcolorSwatch.addMouseListener (new MouseAdapter ()
            {
    
                public void mouseReleased (MouseEvent ev)
                {
    
                    Color textcolor = _this.textProps.getTextColor ();
                
                    QPopup popup = QColorChooser.getColorChooserPopup ("Select the text color",
                                                                    textcolor,
                                                                    new ChangeAdapter ()
                                                                    {
                             
                                                                         public void stateChanged (ChangeEvent ev)
                                                                         {
                             
                                                                             Color c = (Color) ev.getSource ();
                             
                                                                             _this.textProps.setTextColor (c);
                             
                                                                             textcolorSwatch.setBackground (c);
                                                                             
                                                                             bgcolorSwatch.setBackground (_this.textProps.getBackgroundColor ());
    
                                                                             _this.viewer.fireProjectEvent (_this.eventType,
                                                                                                            ProjectEvent.CHANGE_FONT_COLOR);
                             
                                                                         }
    
                                                                     },
                                                                    null);                
                
                    popup.setDraggable (_this.viewer);
    
                    _this.viewer.showPopupAt (popup,
                                              _this.textcolorSwatch,
                                              true);
                
                }
    
            });
    
            this.addItem ("Text",
                          this.textcolorSwatch,
                          layout);
    
            bgcolorSwatch.addMouseListener (new MouseAdapter ()
            {
    
                public void mouseReleased (MouseEvent ev)
                {
    
                    Color bgcolor = _this.textProps.getBackgroundColor ();   
    
                    QPopup popup = QColorChooser.getColorChooserPopup ("Select the background color",
                                                                    bgcolor,
                                                                    new ChangeAdapter ()
                                                                    {
     
                                                                        public void stateChanged (ChangeEvent ev)
                                                                        {
     
                                                                            Color c = (Color) ev.getSource ();
     
                                                                            _this.textProps.setBackgroundColor (c);
                                                                                 
                                                                            bgcolorSwatch.setBackground (c);
                                                                            textcolorSwatch.setBackground (_this.textProps.getTextColor ());
                                                                              
                                                                            _this.viewer.fireProjectEvent (_this.eventType,
                                                                                                           ProjectEvent.CHANGE_BG_COLOR);
     
                                                                        }
     
                                                                    },
                                                                    null);    
                                          
                    popup.setDraggable (_this.viewer);
    
                    _this.viewer.showPopupAt (popup,
                                              _this.bgcolorSwatch,
                                              true);
                    
                }
    
            });
        
            this.addItem ("Background",
                          this.bgcolorSwatch,
                          layout);
            
            final JLabel reset = UIUtils.createClickableLabel ("Reset to defaults",
                                                               null);
        
            reset.setToolTipText ("Click to reset the text properties to their default values");
            
            reset.addMouseListener (new MouseAdapter ()
            {
        
                public void mouseReleased (MouseEvent ev)
                {
                    
                    _this.textProps.resetToDefaults ();
                                
                }
    
            });
        
            this.addItem (null,
                          reset,
                          layout);

        }
    
        this.add (layout);
                
    }
    
}
