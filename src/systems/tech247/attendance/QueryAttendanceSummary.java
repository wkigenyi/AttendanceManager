/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;


import systems.tech247.dbaccess.AttendanceSummary;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.api.ReloadableQueryCapability;
import systems.tech247.util.NotifyUtil;

/**
 *
 * @author Wilfred
 */
public class QueryAttendanceSummary implements Lookup.Provider {
    
    InstanceContent ic;
    Lookup lookup;
    
    private List list;
    
    public QueryAttendanceSummary(final String sql){
        
        list = new ArrayList<>();
        // create an InstanceContent to hold capabilities
        ic = new InstanceContent();
        // create an abstract look to expose the contents of the instance content
        lookup = new AbstractLookup(ic);
        //Add a reloadable capabiliry to the instance content
        ic.add((ReloadableQueryCapability) () -> {
            getList().removeAll(list);
            ProgressHandle ph = ProgressHandleFactory.createHandle("Organising Attendance Summary, Please be patient...");
            ph.start();
            
            List<AttendanceSummary> result = DataAccess.getAttendanceSummary(sql);
            result.forEach((record) ->{
              
                getList().add(record);
                //NotifyUtil.info("Summary Created", surname, false);
                        
            });
            
            
            ph.finish();
        });
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    /**
     * @return the sqlString
     */
    

    /**
     * @param sqlString the sqlString to set
     */
    

    /**
     * @return the list
     */
    public List getList() {
        return list;
    }
    
}
