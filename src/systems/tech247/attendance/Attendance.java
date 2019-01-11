/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import systems.tech247.clockinutil.ShiftSchedule;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmHolidays;
import systems.tech247.hr.PtmShiftSchedule;

/**
 *
 * @author Wilfred
 */
public class Attendance extends ShiftSchedule {
    PtmShiftSchedule schedule;
    String status="";
    String clockin;
    String clockout;
    String shift;
    List<PtmHolidays> holidays = DataAccess.getHolidays();
    List<Date> dates = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Attendance(PtmShiftSchedule schedule, PtmShiftSchedule bean) {
        super(bean);
        this.schedule = schedule;
        
        
        holidays.forEach((h) -> {
            dates.add(h.getDateOf());
        });
        
        
    }

    public String getClockin() {
        try{
            return sdf.format(DataAccess.getCheckIn(schedule.getEmployeeID(), schedule.getShiftDate()));
        }catch(Exception ex){
            return "No Clockin";
        }
    }

    public String getClockout() {
        try{
        return sdf.format(DataAccess.getCheckOut(schedule.getEmployeeID(), schedule.getShiftDate()));
        }catch(Exception ex){
            return "No Clock OUT";
        }
    }

    public String getStatus() {
        if((null==DataAccess.getCheckIn(schedule.getEmployeeID(), schedule.getShiftDate()) && (null==DataAccess.getCheckOut(schedule.getEmployeeID(), schedule.getShiftDate())))){
            if(schedule.getIsCOff()){
                return "C/Off";
            }else if(dates.contains(schedule.getShiftDate())){
                return "Holiday";
            }else if(schedule.getIsWeekOff()){
                return "Weekly Off";
            }else if(schedule.getIsLeave()){
                return "On Leave";
            }
            return "Absent";
        }
        return status;
    }

    public String getShift() {
        try{
       return DataAccess.getShiftByID(schedule.getShiftCode()).getShiftName();
        }catch(NullPointerException ex){
            return "ZZ Shift";
        }
    }
    
    
     
    
    
    
    

    
    
    
}
