/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Admin
 */
public class FactoryEmployeeClockins extends ChildFactory<EmployeeClockin> {
    
    List<EmployeeClockin> l;
    
    @Override
    protected boolean createKeys(List<EmployeeClockin> list) {
        
        //Flip the list
        for(int i=this.l.size()-1;i>=0;i--){
            list.add(l.get(i));
        }
        
        return true;
    }
    
    public FactoryEmployeeClockins(List<EmployeeClockin> l){
        this.l = l;
    }
    
    @Override
    protected Node createNodeForKey(EmployeeClockin key){
        Node node = null;
        try{
            node = new NodeEmployeeClockin(key);
        }catch(Exception ex){
            
        }    
        return node;
    }
    
}
