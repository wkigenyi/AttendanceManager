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
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import systems.tech247.attendance.DailyAttendanceTopComponent;

/**
 *
 * @author WKigenyi
 */
public class FactoryTAReports extends ChildFactory<SetupItem> {
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Transactions",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Daily Total",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Time Card",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Total Time Card",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Early Out",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Late Arrival",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Absentism* - Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new DailyAttendanceTopComponent(false);
                tc.open();
                tc.requestActive();
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Employee Shift",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));

        toPopulate.add(new SetupItem("Time Card List",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Daily Attendance* - Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new DailyAttendanceTopComponent();
                tc.open();
                tc.requestActive();
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Monthly Summary",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        
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
