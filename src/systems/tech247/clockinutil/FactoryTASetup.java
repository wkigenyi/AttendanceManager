/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.util.Date;
import systems.tech247.util.SetupItem;
import systems.tech247.util.NodeSetupItem;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import systems.tech247.attendance.AttendanceSummaryWithChargeTopComponent;

/**
 *
 * @author WKigenyi
 */
public class FactoryTASetup extends ChildFactory<SetupItem> {
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Shifts",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new ShiftsTopComponent("view");
                tc.open();
                tc.requestActive();
                        
            }
        }));
        
        toPopulate.add(new SetupItem("Holidays",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("HolidaysTopComponent");
                tc.open();
                tc.requestActive();
                        
            }
        }));
        
        
        toPopulate.add(new SetupItem("Assign Shift Schedules", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UtilZKClockin.duplicateShiftSchedule(new Date());
            }
        }));
        
        
        

        
        toPopulate.add(new SetupItem("Pass Data To Payroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Post absentism charges to employees
                //Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation("This will post absentism charges on the employees.\nEnsure that Off Days and Leave Days have been assigned before proceeding.\nProceed ?","Process Absentism"));
                TopComponent tc = new AttendanceSummaryWithChargeTopComponent();
                tc.open();
                tc.requestActive();
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
