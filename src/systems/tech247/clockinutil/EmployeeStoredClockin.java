/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

/**
 *
 * @author Admin
 */
public class EmployeeStoredClockin {
    
    private String empName;
    private String empCode;
    private String timeIN;
    private String timeOUT;  
    private String shift;
    private String hours;
    



    public EmployeeStoredClockin(String empName, String empCode, String timeIN, String timeOUT, String shift,String hours) {
        this.empName = empName;
        this.empCode = empCode;
        this.timeIN = timeIN;
        this.timeOUT = timeOUT;
        this.shift = shift;
        this.hours = hours;
    }

    public String getHours() {
        return hours;
    }
    
    

    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }

    

    /**
     * @return the empCode
     */
    public String getEmpCode() {
        return empCode;
    }

    

    /**
     * @return the timeIN
     */
    public String getTimeIN() {
        return timeIN;
    }

    

    /**
     * @return the timeOUT
     */
    public String getTimeOUT() {
        return timeOUT;
    }

    /**
     * @return the shift
     */
    public String getShift() {
        return shift;
    }

    
    
}
