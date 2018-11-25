/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import systems.tech247.attendance.Attendance;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.PtmShiftSchedule;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;

/**
 *
 * @author Admin
 */
public class FactoryAttendance extends ChildFactory<Object> {
    
    Employees emp;
    Date date;
    
    public FactoryAttendance(Employees emp,Date date){
        this.emp = emp;
        this.date = date;
    }

    @Override
    protected boolean createKeys(List<Object> list) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int yr = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        String sql = "select * FROM PtmShiftSchedule where EmployeeID="+emp.getEmployeeID()+" AND MONTH(ShiftDate)="+month+" and YEAR(ShiftDate)="+yr+"";
        
        List<PtmShiftSchedule> l = DataAccess.getShiftSchedule(sql);
        
        for(PtmShiftSchedule s : l){
            list.add(new Attendance(s,s));
        }
        
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof Attendance){
            try {
                node = new NodeAttendance((Attendance)key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else{
            node = new NodeAddTool((AddTool)key);
        }    
        return node;
    }
    
}
