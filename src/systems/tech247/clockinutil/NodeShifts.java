/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import systems.tech247.hr.PtmShifts;
import systems.tech247.util.CapEditable;


/**
 *
 * @author Admin
 */
public class NodeShifts extends AbstractNode{
    
    PtmShifts shift;
    
    public NodeShifts(PtmShifts bean) throws IntrospectionException{
        this(bean,new InstanceContent());
    }
    
    public NodeShifts(final PtmShifts bean, InstanceContent ic) throws IntrospectionException{
        super(Children.LEAF,new AbstractLookup(ic));
        
        ic.add(bean);
        ic.add(new CapEditable() {
            @Override
            public void edit() {
                TopComponent tc = new ShiftEditorTopComponent(bean);
                tc.open();
                tc.requestActive();
            }
        });
        setDisplayName(bean.getShiftName());
        setIconBaseWithExtension("systems/tech247/endeavoricons/settings.png");
    }
    
    

//    @Override
//    public Action getPreferredAction() {
//        return new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                TopComponent tc = new ShiftEditorTopComponent(shift);
//                tc.open();
//                tc.requestActive();
//            }
//        };
//    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = Sheet.createPropertiesSet();
        final PtmShifts shift = getLookup().lookup(PtmShifts.class);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        Property code = new PropertySupport("code", String.class, "CODE", "CODE", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return shift.getShiftCode();
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Property start = new PropertySupport("start", String.class, "START", "START", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(shift.getStartTime());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Property late = new PropertySupport("late", String.class, "LATE", "LATE", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(shift.getLateLimit());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Property end = new PropertySupport("end", String.class, "END", "END", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(shift.getEndTime());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(end);
        set.put(late);
        set.put(start);
        set.put(code);
        sheet.put(set);
        return sheet;
    }
    
    
    
    
}
