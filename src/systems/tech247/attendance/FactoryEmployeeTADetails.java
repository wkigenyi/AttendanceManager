/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import systems.tech247.util.SetupItem;
import systems.tech247.util.NodeSetupItem;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import systems.tech247.clockinutil.FactoryOutOfStation;
import systems.tech247.hr.Employees;
import systems.tech247.shiftschedule.ShiftScheduleTopComponent;


/**
 *
 * @author WKigenyi
 */
public class FactoryEmployeeTADetails extends ChildFactory<SetupItem> {
    
    Employees emp;
    
    public FactoryEmployeeTADetails(Employees emp){
        this.emp = emp;
    }
    
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Shift Schedule",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new ShiftScheduleTopComponent(emp);
                tc.open();
                tc.requestActive();
            }
        }));
        toPopulate.add(new SetupItem("Attendance Register", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new AttendanceRegisterTopComponent(emp);
                tc.open();
                tc.requestActive();
            }
        }));
        toPopulate.add(new SetupItem("Transactions / Clockin Logs", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new ClockinTopComponent(emp);
                tc.open();
                tc.requestActive();
            }
        }));
        
//        toPopulate.add(new SetupItem("Pending Compensation Days", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                TopComponent tc = new CompDaysTopComponent(emp);
//                tc.open();
//                tc.requestActive();
//            }
//        }));
        
        toPopulate.add(new SetupItem("Out Of Station Visits",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new OutOfStationVisitsTopComponent(emp);
                tc.open();
                tc.requestActive();
            }
        }));
        
//        toPopulate.add(new SetupItem("Absent Days", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                TopComponent tc = new AbsentDaysTopComponent(emp);
//                tc.open();
//                tc.requestActive();
//            }
//        }));
        
        
        
        
        
        
        
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
