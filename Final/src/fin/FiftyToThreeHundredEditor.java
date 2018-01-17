//Assignment #: Final
//Student Name: Mihir Jetly
//Class: COMSC-256
//Section: 8352

package fin;

import java.beans.PropertyEditorSupport;

public class FiftyToThreeHundredEditor extends PropertyEditorSupport {
	
    private int propertyValue=0;

    public FiftyToThreeHundredEditor () {
    }

    public String[] getTags() {
       return null;
    }   

    public void setAsText(String value) {              
       int val=new Integer(value).intValue();
       if(val < 50 || val > 300) {
           throw new IllegalArgumentException(value);
       }
       propertyValue = val;
    }

	public String getAsText() {
       return new Integer(propertyValue).toString();
    }
}