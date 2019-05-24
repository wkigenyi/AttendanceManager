/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import systems.tech247.dbaccess.AttendanceSummary;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Wilfred
 */
public class NodeAttendanceSummary extends AbstractNode {
    
    
    NodeAttendanceSummary(AttendanceSummary att){
        super(Children.LEAF,Lookups.singleton(att));
        setDisplayName(att.getEmpCode());
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = Sheet.createPropertiesSet();
        final AttendanceSummary att = getLookup().lookup(AttendanceSummary.class);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        final NumberFormat nf = new DecimalFormat("#,###.00");
        Property empcode = new PropertySupport("empcode", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getEmpCode();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(empcode);
        
        Property name = new PropertySupport("name", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getSurName()+ " " +att.getOtherName();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(name);
        
        Property dept = new PropertySupport("dept", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getDept();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(dept);
        
        
        Property position = new PropertySupport("position", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getPosition();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(position);
        
        
        Property joined = new PropertySupport("joined", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(att.getJoined());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(joined);
        
        Property days = new PropertySupport("days", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getDaysinPeriods();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(days);
        
        Property cat = new PropertySupport("cat", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getCategory();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(cat);
        
        Property absentDays = new PropertySupport("absent", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return att.getAbsentDays();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(absentDays);
        
        Property attended = new PropertySupport("attend", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return   att.getDaysinPeriods() - att.getAbsentDays();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(attended);
        
        Property basic = new PropertySupport("basic", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return nf.format(att.getBasicPay());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(basic);
        
        Property charge = new PropertySupport("charge", String.class, PROP_DISPLAY_NAME, PROP_SHORT_DESCRIPTION, true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return nf.format(att.getBasicPay()*att.getAbsentDays()/att.getDaysinPeriods()) ;
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        set.put(charge);
        
        
        
        
        
        sheet.put(set);
        return sheet; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
