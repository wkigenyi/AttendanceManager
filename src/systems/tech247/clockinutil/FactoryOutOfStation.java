/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import systems.tech247.attendance.OutOfStationEditorTopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.PtmOutstationVisits;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;

/**
 *
 * @author Admin
 */
public class FactoryOutOfStation extends ChildFactory<Object> {
    
    Employees emp;
    
    
    public FactoryOutOfStation(Employees emp){
        this.emp = emp;
        
    }

    @Override
    protected boolean createKeys(List<Object> list) {

        list.add(new AddTool(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new OutOfStationEditorTopComponent(emp);
                tc.open();
                tc.requestActive();
            }
        }));
               
       Collection<PtmOutstationVisits> l = new DataAccess().searchOutSS(emp);
        
        list.addAll(l);
        
        
        
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof PtmOutstationVisits){
            try {
                node = new NodeOSV((PtmOutstationVisits)key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else{
            node = new NodeAddTool((AddTool)key);
        }    
        return node;
    }
    
}
