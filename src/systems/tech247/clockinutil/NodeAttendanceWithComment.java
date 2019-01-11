/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;
import systems.tech247.hr.VwPtmAttendanceWithComment;

/**
 *
 * @author Admin
 */
public class NodeAttendanceWithComment extends AbstractNode{
    
    
    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy,EEE");
    VwPtmAttendanceWithComment att;
    public NodeAttendanceWithComment(VwPtmAttendanceWithComment att) throws IntrospectionException{
        super(Children.LEAF, Lookups.singleton(att));
        setDisplayName(sdf.format(att.getShiftDate()));
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        
        
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final VwPtmAttendanceWithComment att = getLookup().lookup(VwPtmAttendanceWithComment.class);
        
        
        Property clockin = new PropertySupport("clockin", String.class, "IN", "IN", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                try{
                return sdf.format(att.getCheckin());
                }catch(NullPointerException ex){
                return "No Checkin";    
                }
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Property clockout = new PropertySupport("clockout", String.class, "OUT", "OUT", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                try{
                return sdf.format(att.getCheckOut());
                }catch(NullPointerException ex){
                    return "No Checkout";
                }
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Property hours = new PropertySupport("hours", String.class, "Hours Worked", "Hours Worked", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getHoursWorked();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    
        Property comment = new PropertySupport("comment", String.class, "COMMENT", "COMMENT", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getComment();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Property shift = new PropertySupport("shift", String.class, "SHIFT", "SHIFT", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getShiftName();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
   
        Property name = new PropertySupport("empname", String.class, "NAME", "NAME", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getSurName()+" "+att.getOtherNames();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Property dept = new PropertySupport("dept", String.class, "DEPT", "DEPT", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getDept();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        

        
        
        
        
        
        
        
        
   
        set.put(name);
        set.put(dept);
        set.put(shift);
        set.put(comment);
        set.put(hours);
        set.put(clockout);
        set.put(clockin);
        sheet.put(set);
        return sheet;
    }

   
    
    
    

    
    
    
    
}
