/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockin;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 *
 * @author Admin
 * 
 * The class that allows interaction with ZK machines using UDP Protocol
 */
public class ZKUDPLib {
    static int USHRT_MAX = 65535;

    static int CMD_CONNECT = 1000;

    static int CMD_EXIT = 1001;

    static int CMD_ENABLEDEVICE = 1002;

    static int CMD_DISABLEDEVICE = 1003;

    static int CMD_RESTART = 1004;

    static int CMD_POWEROFF = 1005;

    static int CMD_ACK_OK = 2000;

    static int CMD_ACK_ERROR = 2001;

    static int CMD_ACK_DATA = 2002;

    static int CMD_PREPARE_DATA = 1500;

    static int CMD_DATA = 1501;

    static int CMD_USERTEMP_RRQ = 9;

    static int CMD_ATTLOG_RRQ = 13;

    static int CMD_CLEAR_DATA = 14;

    static int CMD_CLEAR_ATTLOG = 15;

    static int CMD_WRITE_LCD = 66;

    static int CMD_GET_TIME = 201;

    static int CMD_SET_TIME = 202;

    static int CMD_VERSION = 1100;

    static int CMD_AUTH = 1102;

    static int CMD_DEVICE = 11;

    static int CMD_CLEAR_ADMIN = 20;

    static int CMD_SET_USER = 8;

    static int CMD_GET_FREE_SIZES = 50;

    static String EMPTY_STRING = "";

    static String CUSTOMIZED_COMMAND_STRING = null;

    static int DEVICE_GENERAL_INFO_STRING_LENGTH = 184;

    static String XML_FAIL_RESPONSE = "Fail";

    static String XML_SUCCESS_RESPONSE = "Success!";
    
    
   //Class Instance
    ZKUDPLib instance;
    
    //Device Response
    String dataReceived;
    
    //Session ID Associated with UDP Transaction
    int sessionID =0;
    //Tells if the result was successful
    Boolean result;
    
    //Supported Commands
    static HashMap commands  = new HashMap();
    //Socket for connection
    Socket socket;
    
    HashMap options = new HashMap();
    
    public ZKUDPLib(HashMap options){
        
        //Use the provided Options
        this.options = options;
        
        commands.put("get_platform", new TADZKLibCommand(CMD_DEVICE, "~Platform", true, "~Platform="));//1
        commands.put("get_fingerprint_algorithm", new TADZKLibCommand(CMD_DEVICE, "~ZKFPVersion", true, "~ZKFPVersion="));//2
        commands.put("get_serial_number", new TADZKLibCommand(CMD_DEVICE, "~SerialNumber", true, "~SerialNumber="));//3
        commands.put("get_oem_vendor", new TADZKLibCommand(CMD_DEVICE, "~OEMVendor", true, "~OEMVendor="));//4
        commands.put("get_mac_address", new TADZKLibCommand(CMD_DEVICE, "MAC", true, "MAC="));//5
        commands.put("get_device_name", new TADZKLibCommand(CMD_DEVICE, "~DeviceName", true, "~DeviceName="));//6
        commands.put("get_manufacture_time", new TADZKLibCommand(CMD_DEVICE, "~ProductTime", true, "~ProductTime="));//7
        commands.put("get_antipassback_mode", new TADZKLibCommand(CMD_DEVICE, "~APBFO", true, "~APBFO="));//8
        commands.put("get_workcode", new TADZKLibCommand(CMD_DEVICE, "~WCFO", true, "~WCFO="));//9
        commands.put("get_ext_format_mode", new TADZKLibCommand(CMD_DEVICE, "~ExtendFmt", true, "~ExtendFmt="));//10
        commands.put("get_encrypted_mode", new TADZKLibCommand(CMD_DEVICE, "encrypt_out", true, "encrypt_out="));//11
        commands.put("get_pin2_width", new TADZKLibCommand(CMD_DEVICE, "~PIN2Width", true, "~PIN2Width="));//12
        commands.put("get_ssr_mode", new TADZKLibCommand(CMD_DEVICE, "~SSR", true, "~SSR="));//13
        commands.put("get_firmware_version", new TADZKLibCommand(CMD_VERSION, EMPTY_STRING, true, false));//14
        commands.put("get_free_sizes", new TADZKLibCommand(CMD_GET_FREE_SIZES, EMPTY_STRING, true, false));//15
        commands.put("set_date", new TADZKLibCommand(CMD_SET_TIME, CUSTOMIZED_COMMAND_STRING, true, false));//16
        commands.put("delete_admin", new TADZKLibCommand(CMD_CLEAR_ADMIN, EMPTY_STRING, true, false));//17
        commands.put("enable", new TADZKLibCommand(CMD_ENABLEDEVICE, EMPTY_STRING, true, false));//18
        commands.put("disable", new TADZKLibCommand(CMD_DISABLEDEVICE, EMPTY_STRING, true, false));//19
        commands.put("restart", new TADZKLibCommand(CMD_RESTART, EMPTY_STRING, true, false));//20
        commands.put("poweroff", new TADZKLibCommand(CMD_POWEROFF, EMPTY_STRING, true, false));//21
        
        
    }
    
    public void executeCommand(String command){
        Boolean shouldDisconnect =  true;
        
    }
    //Establis a connection To The Device
    public void connect(){
        
        int command = CMD_CONNECT;
        String command_string  = EMPTY_STRING;
        int checksum = 0;
        sessionID = sessionID+1;
        int replyID = -1 + USHRT_MAX;
        //
    }
    
    
    
    
    
	


    

}
