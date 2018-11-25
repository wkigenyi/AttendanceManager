/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Checkinout;
import systems.tech247.hr.Employees;
import systems.tech247.hr.OrganizationUnits;

/**
 *
 * @author Admin
 */
public class FactoryStoredEmployeeClockin extends ChildFactory<EmployeeStoredClockin> {
    
    OrganizationUnits department;
    Date selectedDate;
    
    
    @Override
    protected boolean createKeys(List<EmployeeStoredClockin> list) {
        
        Date checkinTime=null;
        Date checkOutTime=null;
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        List<Employees> l;
        if(department!=null){
        l = DataAccess.searchEmployeesByDepartment(department.getOrganizationUnitID());
        }else{
        l = DataAccess.searchEmployees("SELECT e FROM Employees e WHERE e.isDisengaged=0");
        }
        for(Employees e: l){
            String name = e.getSurName()+" "+e.getOtherNames();
            String code = e.getEmpCode();
            String checkin = "No Checkin";
            String checkout = "No Checkout";
            String hoursWorked = "Unknown";
            String shiftName = DataAccess.getEmployeeShift(selectedDate, e);
            List<Checkinout> clockins = DataAccess.getCheckinOut(e, selectedDate);
            for(int i=0;i<clockins.size();i++){
                if(clockins.get(i).getChecktype().equalsIgnoreCase("I")){
                    checkinTime = clockins.get(i).getCheckinoutPK().getChecktime();
                    checkin = sdf.format(checkinTime);
                }else{
                    checkOutTime = clockins.get(i).getCheckinoutPK().getChecktime();
                    checkout = sdf.format(checkOutTime);
                }
                
                try{
                    
                    if(checkinTime==null || checkOutTime==null){
                        
                    }else{
                        Calendar calout = Calendar.getInstance();
                        calout.setTime(checkOutTime);
                        Calendar calin = Calendar.getInstance();
                        calin.setTime(checkinTime);
                        Long t = calout.getTimeInMillis()-calin.getTimeInMillis();
                        BigDecimal hours = new BigDecimal(t/(60*60*1000)).setScale(3);
                        System.out.println(hours);
                        hoursWorked = hours.doubleValue()+" ";
                    }
                    
                }catch(NullPointerException ex){
                    
                }
            
            }
            list.add(new EmployeeStoredClockin(name, code,checkin, checkout, shiftName,hoursWorked));
        }
        return true;
    }
    
    public FactoryStoredEmployeeClockin(OrganizationUnits department, Date date){
        this.department = department;
        this.selectedDate = date;
    }
    
    @Override
    protected Node createNodeForKey(EmployeeStoredClockin key){
        Node node = null;
        try{
            node = new NodeStoredEmployeeClockin(key);
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }    
        return node;
    }
    
}
