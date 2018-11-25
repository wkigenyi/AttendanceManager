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
public class EmployeeClockin {
    
    private String clockinID;
    
    private String clockinTime;
    
    private String clockinType;

    public EmployeeClockin(String clockinID, String clockinTime, String clockinType) {
        this.clockinID = clockinID;
        this.clockinTime = clockinTime;
        this.clockinType = clockinType;
    }

    /**
     * @return the clockinID
     */
    public String getClockinID() {
        return clockinID;
    }

//    /**
//     * @param clockinID the clockinID to set
//     */
//    public void setClockinID(String clockinID) {
//        this.clockinID = clockinID;
//    }

    /**
     * @return the clockinTime
     */
    public String getClockinTime() {
        return clockinTime;
        
    }

//    /**
//     * @param clockinTime the clockinTime to set
//     */
//    public void setClockinTime(String clockinTime) {
//        this.clockinTime = clockinTime;
//    }

    /**
     * @return the clockinType
     */
    public String getClockinType() {
        if("0".equals(clockinType)){
            return "IN";
        }else{
            return "OUT";
        }
    }

//    /**
//     * @param clockinType the clockinType to set
//     */
//    public void setClockinType(String clockinType) {
//        this.clockinType = clockinType;
//    }

    
    
 
    

    
}
