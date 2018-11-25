/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import systems.tech247.hr.PtmShiftSchedule;

/**
 *
 * @author Admin
 */
public class FactoryShiftSchudules extends ChildFactory<PtmShiftSchedule> {
    
    List<PtmShiftSchedule> l;
    
    public FactoryShiftSchudules(List l){
        this.l = l;
    }

    @Override
    protected boolean createKeys(List<PtmShiftSchedule> list) {
        
        
        for(PtmShiftSchedule s : l){
            list.add(new ShiftSchedule(s));
        }
        
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(PtmShiftSchedule key){
        Node node = null;
        if(key instanceof ShiftSchedule){
            try {
                node = new NodeSchedule((ShiftSchedule)key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else{
            //node = new NodeAddTool((AddTool)key);
        }    
        return node;
    }
    
}
