/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmShifts;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;

/**
 *
 * @author Admin
 */
public class FactoryShifts extends ChildFactory<Object> {
    
    String sql = "SELECT s FROM PtmShifts s";
    
    @Override
    protected boolean createKeys(List<Object> list) {
        
        
        list.addAll(DataAccess.getShifts(sql));
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof PtmShifts){
            try {
                node = new NodeShifts((PtmShifts)key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else{
            node = new NodeAddTool((AddTool)key);
        }    
        return node;
    }
    
}
