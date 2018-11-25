/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmHolidays;
import systems.tech247.hr.PtmShiftSchedule;

/**
 *
 * @author Admin
 */
public class ShiftSchedule extends PtmShiftSchedule{
    PtmShiftSchedule bean;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dayF = new SimpleDateFormat("EEEEE");
    
    List<PtmHolidays> holidays = DataAccess.getHolidays();
    List<Date> dates = new ArrayList<>();
    
    private String date;
    private String weekDay;
    private String sCode;
    private Boolean weeklyOff;
    private Boolean compOff;
    
    public ShiftSchedule(PtmShiftSchedule bean){
        this.bean = bean;
        for(PtmHolidays h: holidays){
            dates.add(h.getDateOf());
        }
    }

    public void setCompOff(Boolean compOff) {
        bean.setIsCOff(compOff);
    }

    public Boolean getCompOff() {
        return bean.getIsCOff();
    }
    
    
    
    

    public Boolean getWeeklyOff() {
        return bean.getIsWeekOff();
    }

    public void setWeeklyOff(Boolean weeklyOff) {
        bean.setIsWeekOff(weeklyOff);
    }
    
    
    
    

    /**
     * @return the date
     */
    public String getDate() {
        return sdf.format(bean.getShiftDate());
    }

    @Override
    public Boolean getIsWeekOff() {
        return bean.getIsWeekOff();
    }

    @Override
    public Boolean getIsHoliday() {
        
        
        return dates.contains(bean.getShiftDate());
     
    }

    @Override
    public Boolean getIsLeave() {
        return bean.getIsLeave();
    }

    public String getWeekDay() {
        return dayF.format(bean.getShiftDate());
    }

    public String getsCode() {
        try{
            return DataAccess.getShiftByID(bean.getShiftCode()).getShiftName();
        }catch(NullPointerException ex){
            if(bean.getIsWeekOff()){
                return "Weekly Off";
            }else{
                return "Unknown Shift";
            }
        }
    }

    @Override
    public Boolean getIsCOff() {
        return bean.getIsCOff();
    }

    @Override
    public void setIsWeekOff(Boolean isWeekOff) {
        super.setIsWeekOff(isWeekOff); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    

    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    

    
    
    
}
