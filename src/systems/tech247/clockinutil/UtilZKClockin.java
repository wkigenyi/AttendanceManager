/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.openide.awt.StatusDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.util.NbPreferences;
import systems.tech247.endeavorclockin.ZKOptions;
import systems.tech247.endeavorclockin.ZKSoapLib;
import systems.tech247.hr.OrganizationUnits;

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
    public static void loadTASetup(){
        
        emTAsetup.setRootContext(new NodeTASetup());
    }
    
    public static ExplorerManager departmentAttendance = new  ExplorerManager();
    public static void loadDepartmentAttendance(OrganizationUnits department,Date day){
        //load the clockins
        //StatusDisplayer.getDefault().setStatusText("We are going to load the clockins for the selected department and todays date");
        departmentAttendance.setRootContext(new AbstractNode(Children.create(new FactoryStoredEmployeeClockin(department, day), true)));
    }
    
    
    
}
