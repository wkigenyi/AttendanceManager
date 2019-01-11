/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import systems.tech247.attendance.QueryAttendance;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.VwPtmAttendanceWithComment;

/**
 *
 * @author Admin
 */
public class FactoryAttendanceWithComment extends ChildFactory<VwPtmAttendanceWithComment> {
    
    Employees emp;
    Date date;
    
    Date from;
    Date to;
    
    public FactoryAttendanceWithComment(Employees emp,Date date){
        this.emp = emp;
        this.date = date;
    }
    
    public FactoryAttendanceWithComment(Date from,Date to){
        this.from = from;
        this.to = to;
    }
    
    public FactoryAttendanceWithComment(QueryAttendance query){

    }

    @Override
    protected boolean createKeys(List<VwPtmAttendanceWithComment> list) {
        if(null != emp && null != date){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
        
            int yr = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;

            String sql = "select * FROM vwPtmAttendanceWithComment where EmployeeID="+emp.getEmployeeID()+" AND MONTH(ShiftDate)="+month+" and YEAR(ShiftDate)="+yr+"";
        
            List<VwPtmAttendanceWithComment> l = DataAccess.getAttendance(sql);
        
            l.forEach((s) -> {
                list.add(s);
            });
        }else if(null != from && null != to){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String datefrom = sdf.format(from);
            String dateto = sdf.format(to);
            
            String sql = "select * FROM vwPtmAttendanceWithComment where ShiftDate>='"+datefrom+"' AND ShiftDate<='"+dateto+"'";
        
            List<VwPtmAttendanceWithComment> l = DataAccess.getAttendance(sql);
        
            l.forEach((s) -> {
                list.add(s);
            });
        }
        
        
        
        
        
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected Node createNodeForKey(VwPtmAttendanceWithComment key){
        Node node = null;
        
            try {
                node = new NodeAttendanceWithComment(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        return node;
    }
    
}
