/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import systems.tech247.util.SetupItem;
import systems.tech247.util.NodeSetupItem;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author WKigenyi
 */
public class FactoryTASetup extends ChildFactory<SetupItem> {
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Shifts",Children.create(new FactoryShifts("SELECT s FROM PtmShifts s", true), true)));
        toPopulate.add(new SetupItem("Reports", Children.create(new FactoryTAReports(), true), "systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Overtime Authorization", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Authorization Procedure For 
            }
        }));
        toPopulate.add(new SetupItem("Process Time & Attendance Data", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Determine If the guy was absent or not
            }
        }));
        toPopulate.add(new SetupItem("Attendance Register", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Attendance Analysis, Employee, Date, Day, Status(Absent ?), Hours Worked,Shift
            }
        }));
        

        
        toPopulate.add(new SetupItem("Pass Data To Payroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Confirm Absenteeism and charge the brother
            }
        }));
        
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(SetupItem key) {
        
        Node node =  null;
        try {
            
            node = new NodeSetupItem(key);
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}
