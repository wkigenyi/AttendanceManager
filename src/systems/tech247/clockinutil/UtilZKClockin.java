/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import javax.swing.JButton;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import systems.tech247.clockin.ZKOptions;
import systems.tech247.clockin.ZKSoapLib;
import systems.tech247.dbaccess.AttendanceSummary;
import systems.tech247.dbaccess.DataAccess;
import static systems.tech247.dbaccess.DataAccess.entityManager;
import systems.tech247.hr.Employees;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.hr.VwPtmAttendanceWithComment;

/**
 *
 * @author Admin
 */
public class UtilZKClockin {
    
    public static ExplorerManager attendanceEM = new ExplorerManager();
    
    public static ExplorerManager clockinMachines = new ExplorerManager();
    public static void loadClockin(List<EmployeeClockin> list){
        attendanceEM.setRootContext(new AbstractNode(Children.create(new FactoryEmployeeClockins(list), true)));
    }
    public static ExplorerManager employeesEM = new ExplorerManager();
    public static void loadEmployees(List<ClockinEmployee> list){
        employeesEM.setRootContext(new AbstractNode(Children.create(new FactoryClockinEmployee(list), true)));
    }
    public static void loadClockinMachine(){
        
        HashMap details = new  HashMap();
        
        details.put("ip", "1.1.1.1");
        details.put("soap", 80);
        details.put("udp", 4370);
        details.put("name", "No Configured Machine");
        try{
            String ip = NbPreferences.forModule(ClockinMachinesPanel.class).get("ZKIPAddress", "");
            if(ip.length()>2){
                details.put("ip", ip);
            }
            details.put("ip", NbPreferences.forModule(ClockinMachinesPanel.class).get("ZKIPAddress", ""));
            String name =  NbPreferences.forModule(ClockinMachinesPanel.class).get("MachineName", "");
            if(name.length()>2){
                details.put("name", name);
            }
            
        }catch(Exception ex){
            StatusDisplayer.getDefault().setStatusText("No Clockin Machine Configured");
        }
        String ip = details.get("ip").toString();
        String name = details.get("name").toString();
        
        ClockinMachine machine = new ClockinMachine(ip, 0, name);
        
        //clockinMachines.setRootContext(new NodeClockinMachine(machine));
    }
    
//    public static void loadClockinInfo(ClockinMachine machine, int whatdoyouwant){
//        clockinMachines.setRootContext(new NodeClockinMachine(machine, whatdoyouwant));
//    }
    
    public static ChildFactory chooseClockinFactory(ClockinMachine machine,int whatdoyouwant){
       
        HashMap map = new HashMap();
        map.put("ip", machine.getIp());
        ZKSoapLib stub = new ZKSoapLib(new ZKOptions(map));
        switch(whatdoyouwant){
            case 1:
                List<EmployeeClockin> l = stub.getAttendanceLogs();
                //get the clockins
                
                
                return new FactoryEmployeeClockins(l);
                
            case 2:
                List<ClockinEmployee> list = stub.getRegisteredEmployees();
                return new FactoryClockinEmployee(list);
            default:  
                return null;
        }
    }
    /* Payroll Code Groups Here */
    public static ExplorerManager emTAsetup = new ExplorerManager();
    
    
    public static ExplorerManager departmentAttendance = new  ExplorerManager();
    public static void loadDepartmentAttendance(OrganizationUnits department,Date day){
        //load the clockins
        //StatusDisplayer.getDefault().setStatusText("We are going to load the clockins for the selected department and todays date");
        departmentAttendance.setRootContext(new AbstractNode(Children.create(new FactoryStoredEmployeeClockin(department, day), true)));
    }
    
    public static JRDataSource generateAttendanceData(OrganizationUnits dept,Date from, Date to, Boolean quer){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        String sql;
        DRDataSource data = new DRDataSource("name","date","timein","timeout","hours","comment");
        if(quer){
            sql = "SELECT * FROM vwPtmAttendanceWithComment WHERE deptID = "+dept.getOrganizationUnitID()+" AND ShiftDate>='"+df.format(from)+"' AND ShiftDate<='"+df.format(to)+"'";
        }else{
            sql = "SELECT * FROM vwPtmAttendanceWithComment WHERE deptID = "+dept.getOrganizationUnitID()+" AND ShiftDate>='"+df.format(from)+"' AND ShiftDate<='"+df.format(to)+"' AND Comment LIKE 'ABSENT'";
        }
        
        List<VwPtmAttendanceWithComment> list = DataAccess.getAttendance(sql);
        String checkin = "";
        String checkout = "";
        Double hours = 0.0;
        //Collections.sort(list, new PayrollCodeGroupComparator());
        for(VwPtmAttendanceWithComment att : list) {
            
            try{
                checkin = stf.format(att.getCheckin());
                
            }catch(NullPointerException ex){
                checkin = "";
            }
            try{
                //checkin = stf.format(att.getCheckin());
                checkout = stf.format(att.getCheckOut());
                //hours = att.getHoursWorked();
            }catch(NullPointerException ex){
                checkout = "";
            }
            try{
                //checkin = stf.format(att.getCheckin());
                //checkout = stf.format(att.getCheckOut());
                hours = att.getHoursWorked();
            }catch(NullPointerException ex){
                
            }
            
            data.add(att.getSurName()+" "+att.getOtherNames()+"("+att.getClockinID()+")",sdf.format(att.getShiftDate()),checkin,checkout,hours,att.getComment());
        }    
        //StatusDisplayer.getDefault().setStatusText("Data is ready");
        return data;
    }
    
    public static JRDataSource generateAttendanceSummary(List<AttendanceSummary> list){
        
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        DRDataSource data = new DRDataSource(
                "empcode",
                "empname",
                "dept",
                "job",
                "join",
                "days",
                "absent",
                "cat",
                "basic",
                "amount"
        );
        
        
        
        //Collections.sort(list, new PayrollCodeGroupComparator());
        for(AttendanceSummary att : list) {
            
            data.add(
                    att.getEmpCode(),
                    att.getSurName()+" "+att.getOtherName(),
                    att.getDept(),
                    att.getCategory(),
                    sdf.format(att.getJoined()),
                    att.getDaysinPeriods(),
                    att.getAbsentDays(),
                    att.getPosition(),
                    att.getBasicPay(),
                    att.getBasicPay()*att.getAbsentDays()/att.getDaysinPeriods());
        }    
        //StatusDisplayer.getDefault().setStatusText("Data is ready");
        return data;
    }
    
    
    
    
    
    
    public static void duplicateShiftSchedule(Date date){
        NotifyDescriptor nd = new NotifyDescriptor(
                "Duplicate Previous Shift Schedules?", //Message or component 
                "Shift Schedules", //Title
                NotifyDescriptor.YES_NO_OPTION,//Options 
                NotifyDescriptor.QUESTION_MESSAGE,//Symbol 
                new Object[]{
                    
                    new JButton("Yes, All Employees"){
            @Override
            public void addActionListener(ActionListener l) {
                super.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // We are at the beginning of the month
                Runnable task;
                task = () -> {
                    ProgressHandle ph = ProgressHandleFactory.createHandle("Filling The Schedules");
                    ph.start();
                    //Delete the existing schedules for the month
                    Calendar cal = Calendar.getInstance();
 
                    cal.setTime(date);
                    cal.set(Calendar.DATE, 0);
                    cal.set(Calendar.HOUR, 12);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND, 0);
                    
                    int currentMonthInDB = cal.get(Calendar.MONTH)+1;
                    int currentYear = cal.get(Calendar.YEAR);
                    ph.progress("Cleaning The Month,  Deleting existing schedules");
                    String sql = "DELETE FROM PtmShiftSchedule WHERE year(shiftDate) = "+currentYear+" AND month(ShiftDate)="+currentMonthInDB+"";
                    Query query = entityManager.createNativeQuery(sql);
                    entityManager.getTransaction().begin();
                    query.executeUpdate();
                    entityManager.getTransaction().commit();
                    //Get the list of employees that are to be updated
                    ph.progress("Getting the employees");
                    List<Employees> employeeList = DataAccess.searchEmployees(
                            "SELECT e FROM Employees e WHERE e.isDisengaged = 0"
                    );
                    
                    int total = employeeList.size();
                    
                    
                    for(int i = 0; i<total; i++){
                        //Reset the date for the new employee
                        cal.setTime(date);
                        cal.set(Calendar.DATE, 1);
                        cal.set(Calendar.HOUR_OF_DAY,0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND,0);
                        cal.set(Calendar.MILLISECOND, 0);
                        
                        
                        
                        //int latestShiftCode = DataAccess.getLatestShiftCode(emp.getEmployeeID());
                        int month =cal.get(Calendar.MONTH);
                        int monthV = cal.get(Calendar.MONTH);
                        int day = 1;
                        Employees emp = employeeList.get(i);
                        
                        while(month==monthV){
                            ph.progress(i+" / "+total+" Posting for "+emp.getSurName()+" "+emp.getOtherNames()+" Day "+day);
                            //String getInsertID = "SELECT MAX(shiftscheduleid) from PtmShiftSchedule";
                            //Query queryID = entityManager.createNativeQuery(getInsertID);
                            //int id = ((BigDecimal)queryID.getSingleResult()).intValue();
                            //int newID = id+1;
                            String insertQuery = 
                                    "INSERT INTO [dbo].[PtmShiftSchedule]\n" +
                                    "           ([CoCode]\n" +//1
                                    "           ,[EmployeeID]\n" +//2
                                    "           ,[ShiftDate]\n" +//3
                                    "           ,[ShiftCode]\n" +//4
                                    "           ,[DayType]\n" +//5
                                    "           ,[IsWeekOff]\n" +//6,7
                                    "           ,[IsCOff]\n" +//8
                                    "           ,[IsLeave]\n" +//9
                                    "           ,[IsOT2Shift]\n" +//10
                                    "           ,[UserID]\n" +//11
                                    "           ,[EntryDate]\n" +//12
                                    "           ,[DepartmentCode]\n" +//13
                                    "           ,[MasterTwoCode])\n" +//null 14
                                    "     VALUES\n" +
                                    "           (?,?,?,dbo.ptmfnGetLatestShiftCode(?),?,dbo.ptmfnIsWeekOff(?,?),?,?,?,?,?,?,?)";
                            
                            Query queryInsert = entityManager.createNativeQuery(insertQuery);
                            //queryInsert.setParameter(1, newID);
                            queryInsert.setParameter(1, 1);
                            
                            queryInsert.setParameter(2, emp.getEmployeeID());
                            
                            queryInsert.setParameter(3, cal.getTime());
                            
                            queryInsert.setParameter(4, emp.getEmployeeID());
                            
                            queryInsert.setParameter(6, cal.getTime());
                            
                            queryInsert.setParameter(7, emp.getEmployeeID());
                            
                            queryInsert.setParameter(8,0);
                            
                            //queryInsert.setParameter(10, 0);
                            queryInsert.setParameter(12, new Date());
                            
                            
                            entityManager.getTransaction().begin();
                            queryInsert.executeUpdate();
                            entityManager.getTransaction().commit();
                            //Now go to the next date
                            cal.add(Calendar.DATE, 1);
                            monthV= cal.get(Calendar.MONTH);
                            day = day+1;
                            
                        }
                    }
                    ph.finish();
                };
                
                RequestProcessor.getDefault().post(task);
                    }
                });
                
                
                
                
                

                
            }
                        
                        
                    },
                    NotifyDescriptor.CANCEL_OPTION
                }, 
                null);
        
        DialogDisplayer.getDefault().notify(nd);
        
        
    }
    
    
    
}
