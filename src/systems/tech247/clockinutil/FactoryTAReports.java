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

/**
 *
 * @author WKigenyi
 */
public class FactoryTAReports extends ChildFactory<SetupItem> {
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Absent Days Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Absentism Memos Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Absentism Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Analysis Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        toPopulate.add(new SetupItem("Attendance Register Modification Report",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Run the report
            }
        },"systems/tech247/util/icons/capex.png"));
        
        toPopulate.add(new SetupItem("Attendance Status Report",new AbstractAction() {
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
