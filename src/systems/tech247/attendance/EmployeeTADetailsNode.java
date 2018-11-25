/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import systems.tech247.hr.Employees;

/**
 *
 * @author Admin
 */
public class EmployeeTADetailsNode extends AbstractNode{
    
    Employees emp;
    public EmployeeTADetailsNode(Employees emp){
        super(Children.create(new FactoryEmployeeTADetails(emp), true));
        this.emp = emp;
        setIconBaseWithExtension("systems/tech247/util/icons/person.png");
            setDisplayName(emp.getSurName()+" "+emp.getOtherNames());
    }


    
    
    
}
