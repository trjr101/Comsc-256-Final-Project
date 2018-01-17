//Assignment #: Final
//Student Name: Mihir Jetly
//Class: COMSC-256
//Section: 8352

package fin;

import java.beans.PropertyEditorSupport;

public class TrueFalseEditor extends PropertyEditorSupport {
	
    private boolean propertyValue;

    public TrueFalseEditor () {
    }

    public String[] getTags() {
       return null;
    }   

    public void setAsText(String value) {              
       if(value.equals("yes") || value.equals("true"))
           propertyValue = true;
       else if (value.equals("no") || value.equals("false"))
    	   propertyValue = false;
       else 
    	   throw new IllegalArgumentException(value);
    }

    public String getAsText() {
       return Boolean.toString(propertyValue);
    }
}