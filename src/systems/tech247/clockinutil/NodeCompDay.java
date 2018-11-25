/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;


/**
 *
 * @author Admin
 */
public class NodeCompDay extends AbstractNode{
    
    Date clockin;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy");
    
    public NodeCompDay(Date clockin) throws IntrospectionException{
        
        super(Children.LEAF,Lookups.singleton(clockin));
        
        this.clockin = clockin;
        setDisplayName(sdf.format(clockin));
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        
    }

    
    
    

    

    
    
    
    
    
}
