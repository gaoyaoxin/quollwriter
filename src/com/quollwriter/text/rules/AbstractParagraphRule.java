package com.quollwriter.text.rules;

import java.util.*;

import com.quollwriter.text.*;

public abstract class AbstractParagraphRule extends AbstractRule<Paragraph> implements ParagraphRule
{

    public AbstractParagraphRule (boolean userRule)
    {

        super (userRule);

    }

    public abstract List<Issue> getIssues (Paragraph paragraph);
    
    public String getEditSummary ()
    {

        return this.summary;

    }

    public String getEditDescription ()
    {

        return this.desc;

    }
    
    @Override
    public String getEditFormTitle (boolean add)
    {
        
        return (add ? "Add new Paragraph Structure rule" : null);
        
    }    

}
