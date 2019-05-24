/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import systems.tech247.clockinutil.UtilZKClockin;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.TblPeriods;

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

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            new AbstractAction("Create Schedules For This Period") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TblPeriods p = DataAccess.getCurrentMonth();
                    List<Employees> list = new ArrayList();
                    list.add(emp);
                    UtilZKClockin.duplicateShiftSchedule(p, list);
                }
            }
        };
    }

    
    
    


    
    
    
}
