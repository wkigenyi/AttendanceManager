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
public class FactoryClockinEmployee extends ChildFactory<ClockinEmployee> {
    
    List<ClockinEmployee> l;
    
    @Override
    protected boolean createKeys(List<ClockinEmployee> list) {
        l.forEach((cl) -> {
            list.add(cl);
            
        });
        return true;
    }
    
    public FactoryClockinEmployee(List<ClockinEmployee> l){
        this.l = l;
    }
    
    @Override
    protected Node createNodeForKey(ClockinEmployee key){
        Node node = null;
        try{
            node = new NodeRegisteredEmployee(key);
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }    
        return node;
    }
    
}
