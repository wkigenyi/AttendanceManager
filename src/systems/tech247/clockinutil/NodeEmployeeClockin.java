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
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;


/**
 *
 * @author Admin
 */
public class NodeEmployeeClockin extends BeanNode<EmployeeClockin>{
    
    public NodeEmployeeClockin(EmployeeClockin bean) throws IntrospectionException{
        super(bean,Children.LEAF,Lookups.singleton(bean));
        
        try{
            Employees employee = DataAccess.getEmployeeByClockinID(padClockin(bean.getClockinID(), 10));
            setDisplayName(employee.getSurName()+" "+employee.getOtherNames());
        }catch(NullPointerException ex){
            setDisplayName(bean.getClockinID()+"-Not in Database");
        }
        setIconBaseWithExtension("systems/tech247/endeavoricons/person.png");
    }
    
    private String padClockin(String clockinID,int length){
        while(clockinID.length()<length){
            clockinID = "0"+clockinID;
        }
        return clockinID;
    }
}
