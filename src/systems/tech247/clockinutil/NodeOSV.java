/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import systems.tech247.attendance.OutOfStationEditorTopComponent;
import systems.tech247.hr.PtmOutstationVisits;


/**
 *
 * @author Admin
 */
public class NodeOSV extends AbstractNode{
    
    PtmOutstationVisits visit;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy");
    
    public NodeOSV(PtmOutstationVisits visit) throws IntrospectionException{
        
        super(Children.LEAF,Lookups.singleton(visit));
        
        this.visit = visit;
        setDisplayName(sdf.format(visit.getFromDate()) +" -  "+sdf.format(visit.getToDate()));
        setIconBaseWithExtension("systems/tech247/util/icons/capex.png");
        setShortDescription(visit.getRemarks());
        
    }

    @Override
    public Action getPreferredAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new OutOfStationEditorTopComponent(visit, null);
                tc.open();
                tc.requestActive();
            }
        };
    }
    
    

    
    
    

    

    
    
    
    
    
}
