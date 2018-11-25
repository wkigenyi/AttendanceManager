/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.util.Date;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Checkinout;
import systems.tech247.hr.Employees;

/**
 *
 * @author Admin
 */
public class FactoryCompDays extends ChildFactory<Date> {
    
    Employees emp;
    Date startDate;
    Date endDate;
    
    public FactoryCompDays(Employees emp,Date startDate,Date endDate){
        this.emp = emp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    protected boolean createKeys(List<Date> list) {
        list.addAll(DataAccess.getCompDays(emp, startDate, endDate));
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(Date key){
        Node node = null;
        try{
            node = new NodeCompDay(key);
        }catch(Exception ex){
            
        }    
        return node;
    }
    
}
