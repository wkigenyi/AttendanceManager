/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockin;

/**
 *
 * @author Admin
 */
public class Tad {
    private static final String[]  PARSEABLE_ARGS= {
        "com_key", 
	"pin", 
	"time", 
	"template",
        
	"name", 
	"password", 
	"group", 
	"privilege",
        
	"card", 
	"pin2", 
	"tz1", 
	"tz2", 
	"tz3",
        
	"finger_id", 
	"option_name", 
	"date",
        
	"size", 
	"valid", 
	"value"
   
    };
    
    private String ip;
    
    private Object internalID;
    
    private String description;
    
    private String comKey;
    
    private String connectionTimeOut;
    
    private String encoding;
    
    private int udpPORT;
    
    private Object tadSOAP;
    
    private Object zkLib;
}
