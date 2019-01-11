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
import org.openide.nodes.Sheet.Set;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import systems.tech247.attendance.OutOfStationEditorTopComponent;
import systems.tech247.hr.PtmOutstationVisits;
import systems.tech247.util.CapEditable;


/**
 *
 * @author Admin
 */
public class NodeOSV extends AbstractNode{
    
    PtmOutstationVisits visit;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy");
    
    public NodeOSV(PtmOutstationVisits visit) throws IntrospectionException{
        this(visit,new InstanceContent());
    }
    
    public NodeOSV(final PtmOutstationVisits visit,InstanceContent ic) throws IntrospectionException{
        
        super(Children.LEAF,new AbstractLookup(ic));
        
        ic.add(visit);
        ic.add(new CapEditable() {
            @Override
            public void edit() {
                TopComponent tc = new OutOfStationEditorTopComponent(visit,visit.getEmployeeID());
                tc.open();
                tc.requestActive();
            }
        });
        setDisplayName(visit.getRemarks());
        setIconBaseWithExtension("systems/tech247/util/icons/capex.png");
        //setShortDescription(visit.getRemarks());
        
    }

//    @Override
//    public Action getPreferredAction() {
//        return new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                TopComponent tc = new OutOfStationEditorTopComponent(visit, null);
//                tc.open();
//                tc.requestActive();
//            }
//        };
//    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = Sheet.createPropertiesSet();
        
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        final PtmOutstationVisits visit = getLookup().lookup(PtmOutstationVisits.class);
        
        
        Property from = new PropertySupport("from", String.class, "FROM", "FROM", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(visit.getFromDate());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        Property to = new PropertySupport("to", String.class, "FROM", "FROM", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(visit.getToDate());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(to);
        set.put(from);
        sheet.put(set);
        return sheet; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    

    
    
    

    

    
    
    
    
    
}
