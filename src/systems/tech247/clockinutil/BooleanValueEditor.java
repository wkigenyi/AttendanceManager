/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import javax.swing.JLabel;

/**
 *
 * @author Admin
 */
public class BooleanValueEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    @Override
    public String getAsText() {
        return super.getAsText(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component getCustomEditor() {
        return new JLabel("Are you sure?");
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }
    
    
    
    
    
}
