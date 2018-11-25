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
public class ClockinMachine {
    
    private int soapPort;
    private String ip;
    private int UDPPort;
    private String name;
    
    public ClockinMachine(String ip, int soapPort,String name){
        this(ip, soapPort, 0,name);
    }
    
    public ClockinMachine(String ip, int soapPort, int UDPPort,String name){
        this.UDPPort =UDPPort;
        this.ip = ip;
        this.soapPort = soapPort;
        this.name = name;
    }

    /**
     * @return the soapPort
     */
    public int getSoapPort() {
        return soapPort;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the UDPPort
     */
    public int getUDPPort() {
        return UDPPort;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
