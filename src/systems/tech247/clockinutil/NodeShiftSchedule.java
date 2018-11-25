/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Admin
 */
public class NodeShiftSchedule extends BeanNode<ShiftSchedule>{
    
    
    ShiftSchedule machine;
    public NodeShiftSchedule(ShiftSchedule machine) throws IntrospectionException{
        super(machine,Children.LEAF,Lookups.singleton(machine));
        setDisplayName(machine.getDate());
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        
        
    }
    
    
    

    
    
    
    
}
