/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.shiftschedule.ShiftScheduleTopComponent;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;



/**
 *
 * @author Wilfred
 */
public class FactoryDepartmentEmployees extends ChildFactory<Object>{

    
    
    OrganizationUnits department;
    
    public FactoryDepartmentEmployees(OrganizationUnits department){
        this.department = department;
    }
    
    
    
    @Override
    protected boolean createKeys(List<Object> list) {
        
        //Populate the list of child entries
        list.addAll(DataAccess.searchEmployeesByDepartment(department.getOrganizationUnitID()));
        
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof Employees){
            node = new EmployeeNode((Employees)key);
        }else if(key instanceof AddTool){
            node = new NodeAddTool((AddTool)key);
        }
        
        return node;
    }
    
    private class EmployeeNode extends AbstractNode{
        
        private final InstanceContent instanceContent;
        Employees unit;
        
        public EmployeeNode(Employees emp){
            this(new InstanceContent(),emp);
        }
        
        private EmployeeNode (InstanceContent ic, Employees unit){
            super(Children.LEAF, new AbstractLookup(ic));
            instanceContent = ic;
            instanceContent.add(unit);
            this.unit = unit;
            setIconBaseWithExtension("systems/tech247/util/icons/person.png");
            setDisplayName(unit.getSurName()+" "+unit.getOtherNames());
        }

        @Override
        public Action[] getActions(boolean context) {
            Action[] actions = new Action[]{
                new AbstractAction("Show Shift Schedule") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TopComponent tc = new ShiftScheduleTopComponent(unit);
                        tc.open();
                        tc.requestActive();
                    }
                },
                new AbstractAction("Show Compesation Days"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Here we shall show the compesation days for the brother and the their status
                    //Indicate the holidays worked and the right user can add a compensation day approved by the right people
                }
                    
                }
            };
            
            return actions;
        }
        

        
        
        
    
}
    
}
