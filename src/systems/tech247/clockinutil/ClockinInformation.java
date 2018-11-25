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
public class ClockinInformation {
    
    private String info;
    private ClockinMachine machine;
    public ClockinInformation(String info,ClockinMachine machine){
        this.info = info;
        this.machine =  machine;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @return the machine
     */
    public ClockinMachine getMachine() {
        return machine;
    }
    
}
