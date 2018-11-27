/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockin;

import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class ZKOptions {
    
    //Default Options
    private static final String IP_ADDRESS ="169.254.0.1";
    private static final int INTERNAL_ID = 1;
    private static final int COM_KEY = 0;
    private static final String DESCRIPTION ="N/A";
    private static final int CONNECTION_TIME_OUT = 5;
    private static final int SOAP_PORT = 80;
    private static final int UDP_PORT = 4370;
    private static final String ENCODING = "iso8859-1";
    private static final String URI = "http://www.zksoftware/Service/message/";
    private static final Boolean EXCEPTIONS = Boolean.FALSE;
    private static final Boolean TRACE = Boolean.TRUE;
    
    
    private static HashMap options = new HashMap();
    
    public ZKOptions(HashMap options){
        ZKOptions.options = options;
        
    }
    
    public static ZKOptions getInstance(){
        return new ZKOptions();
    }
    
    private ZKOptions(){
        
    }
    
    
    
    public HashMap getOptions(){
        HashMap map = new  HashMap();
        
        //Default Options
        map.put("ip", IP_ADDRESS);
        map.put("internal_id", INTERNAL_ID);
        map.put("com_key", COM_KEY);
        map.put("description", DESCRIPTION);
        map.put("conn_timeout", CONNECTION_TIME_OUT);
        map.put("soap_port", SOAP_PORT);
        map.put("udp_port", UDP_PORT);
        map.put("encoding", ENCODING);
        map.put("uri", URI);
        map.put("trace", TRACE);
        map.put("exception", EXCEPTIONS);
        //Merge with specified options
        map.putAll(options);
        
        
        
        return map;
    }

    
}
