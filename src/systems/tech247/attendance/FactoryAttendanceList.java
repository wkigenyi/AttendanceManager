/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.beans.IntrospectionException;
import java.util.Date;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

import systems.tech247.api.ReloadableQueryCapability;
import systems.tech247.clockinutil.NodeAttendanceWithComment;
import systems.tech247.hr.VwPtmAttendanceWithComment;

/**
 *
 * @author Wilfred
 */
public class FactoryAttendanceList extends ChildFactory<VwPtmAttendanceWithComment>{

    QueryAttendance query;
    Date from;
    Date to;
    public FactoryAttendanceList(QueryAttendance query,Date from,Date to){
        this.query = query;
        this.to = to;
        this.from = from;
    }
    
    @Override
    protected boolean createKeys(List<VwPtmAttendanceWithComment> list) {
        //get this ability from the look
        ReloadableQueryCapability r = query.getLookup().lookup(ReloadableQueryCapability.class);
        //Use the ability
        if(r != null){
            try{
                r.reload();
            }catch(Exception ex){
                
            }
        }
        //Populate the list of child entries
        /*list.add(new AddTool(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeePersonalInfoEditorTopComponent newEditor = new EmployeePersonalInfoEditorTopComponent();
                newEditor.open();
                newEditor.requestActive();
            }
        }));*/
        list.addAll(query.getList());
        
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(VwPtmAttendanceWithComment key){
        Node node = null;
        try{
            node = new NodeAttendanceWithComment(key);
        }catch(IntrospectionException ex){
            
        }
        
        return node;
    }
    
    
    
}
