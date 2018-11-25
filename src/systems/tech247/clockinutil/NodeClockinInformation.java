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
import systems.tech247.hr.Checkinout;


/**
 *
 * @author Admin
 */
public class NodeClockinInformation extends AbstractNode{
    
    Checkinout clockin;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy");
    
    public NodeClockinInformation(Checkinout clockin) throws IntrospectionException{
        
        super(Children.LEAF,Lookups.singleton(clockin));
        
        this.clockin = clockin;
        setDisplayName(sdf.format(clockin.getCheckinoutPK().getChecktime()));
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //final Checkinout clockin = getLookup().lookup(Checkinout.class);
        
        Property time = new PropertySupport("time", String.class, "Clocking Time", "Time when the clockin ocurred", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(clockin.getCheckinoutPK().getChecktime());
            }
            
            @Override
            public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(time);
        
        Property inout = new PropertySupport("inout", String.class, "IN/OUT", "In or Out ?", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return clockin.getChecktype();
            }
            
            @Override
            public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(inout);
        
        Property sno = new PropertySupport("sno", String.class, "Machine Serial#", "Serial Number", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return clockin.getSensorid();
            }
            
            @Override
            public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(sno);
        
        
        
        
        
        
        sheet.put(set);
        return sheet;
    }
    
    

    

    
    
    
    
    
}
