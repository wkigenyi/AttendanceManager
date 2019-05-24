/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockin;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import systems.tech247.clockinutil.ClockinEmployee;
import systems.tech247.clockinutil.EmployeeClockin;

/**
 *
 * @author Admin
 */
public class ZKSoapLib {
    
    
    static Map<String,String> availableCommands = new HashMap<>();
    ZKOptions options;
    int com_key;
    public ZKSoapLib(ZKOptions options){
        this.options = options;
        com_key = (int)options.getOptions().get("com_key");
        availableCommands.put("get_date", "<GetDate><ArgComKey>%com_key%</ArgComKey></GetDate>");
        availableCommands.put("get_att_log", "<GetAttLog><ArgComKey>%com_key%</ArgComKey><Arg><PIN>%pin%</PIN></Arg></GetAttLog>");
        availableCommands.put("get_user_info", "<GetUserInfo><ArgComKey>%com_key%</ArgComKey><Arg><PIN>%pin%</PIN></Arg></GetUserInfo>");
        availableCommands.put("get_all_user_info", "<GetAllUserInfo><ArgComKey>%com_key%</ArgComKey></GetAllUserInfo>");
        availableCommands.put("get_user_template", "<GetUserTemplate><ArgComKey>0</ArgComKey><Arg><PIN>%pin%</PIN><FingerID>%finger_id%</FingerID></Arg></GetUserTemplate>");
        availableCommands.put("get_combination", "<GetCombination><ArgComKey>%com_key%</ArgComKey></GetCombination>");
        availableCommands.put("get_option", "<GetOption><ArgComKey>%com_key%</ArgComKey><Arg><Name>%option_name%</Name></Arg></GetOption>");
    }
   
    public List<EmployeeClockin> getAttendanceLogs(){
        int comKeyValue = (int)options.getOptions().get("com_key");
        ArrayList<EmployeeClockin> list = new ArrayList<>();
       
        int i = 0;
        try{
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMessage =  factory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelop = soapPart.getEnvelope();
            
            SOAPBody soapBody = soapEnvelop.getBody();
            QName bodyName = new QName("http://www.zksoftware/Service/message/", "GetAttLog");
            
            SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
            //Adding the com Key
            QName comArg = new  QName("ArgComKey");
            SOAPElement comKey = bodyElement.addChildElement(comArg);
            comKey.addTextNode(""+comKeyValue+"");
            QName arg = new QName("Arg");
            SOAPElement argKey = comKey.addChildElement(arg);
            QName pin = new QName("PIN");
            SOAPElement pinKey = argKey.addChildElement(pin);
            pinKey.addTextNode("1");
                
            System.out.println();
            soapMessage.writeTo(System.out);
            
            //Getting Soap Connection
            SOAPConnectionFactory connFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = connFactory.createConnection();
            //URL endPoint = new URL("http://"+options.getOptions().get("ip")+"/iWsService");
            
            /*URL deviceURL = new URL(null, "http://"+options.getOptions().get("ip")+"/iWsService", new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                    URL target =  new URL(u.toString());
                    URLConnection connection = target.openConnection();
                    connection.setReadTimeout(10000);
                    return connection;
                }
            });*/
            //Sending the message
            
            URL endPoint = new URL("http://"+options.getOptions().get("ip")+"/iWsService");
            
            
            
            SOAPMessage response = conn.call(soapMessage, endPoint);
            
            conn.close();
            SOAPBody responseBody = response.getSOAPBody();
            response.writeTo(System.out);
            //System.out.println("Fault Code: " +response.getSOAPPart().getEnvelope().getBody().getFault().getFaultCode());
            QName responseBodyName = new QName( "GetAttLogResponse");
            Iterator dateResponseIterator = responseBody.getChildElements(responseBodyName);
            SOAPBodyElement getDateElement = (SOAPBodyElement)dateResponseIterator.next();
            QName row = new QName("Row");
            Iterator rowResponseIterator = getDateElement.getChildElements(row);
             
            QName time = new  QName("DateTime");
            QName status = new  QName("Status");
            
            while(rowResponseIterator.hasNext()){
            
            i = i+1;
            SOAPBodyElement rowElement =  (SOAPBodyElement)rowResponseIterator.next();
            Iterator pinIterator = rowElement.getChildElements(pin);
            Iterator dateIterator = rowElement.getChildElements(time);
            Iterator statusIterator = rowElement.getChildElements(status);
            SOAPBodyElement pinElement =  (SOAPBodyElement)pinIterator.next();
            SOAPBodyElement dateElement =  (SOAPBodyElement)dateIterator.next();
            SOAPBodyElement statusElement =  (SOAPBodyElement)statusIterator.next();
            //SOAPBodyElement pinElement =  (SOAPBodyElement)rowResponseIterator.next();
            
            EmployeeClockin clockin = new EmployeeClockin(pinElement.getValue(),dateElement.getValue(),statusElement.getValue());
            list.add(clockin);
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
        }catch(IOException | UnsupportedOperationException | SOAPException | NullPointerException ex){
            ex.printStackTrace();
        }
        
        
        return list;
    }
    
    public List<ClockinEmployee> getRegisteredEmployees(){
        int comKeyValue = (int)options.getOptions().get("com_key");
        ArrayList<ClockinEmployee> list = new ArrayList<>();
        
        
        int i = 0;
        try{
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMessage =  factory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelop = soapPart.getEnvelope();
            
            SOAPBody soapBody = soapEnvelop.getBody();
            QName bodyName = new QName("http://www.zksoftware/Service/message/", "GetAllUserInfo");
            
            SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
            QName comArg = new  QName("ArgComKey");
            SOAPElement comKey = bodyElement.addChildElement(comArg);
            comKey.addTextNode(""+comKeyValue+"");
            
            
            
            
            
            
            
            soapMessage.writeTo(System.out);
            
            //Getting Soap Connection
            SOAPConnectionFactory connFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = connFactory.createConnection();
            //Sending the message
            URL endPoint = new URL("http://"+options.getOptions().get("ip")+"/iWsService");
            SOAPMessage response = conn.call(soapMessage, endPoint);
            conn.close();
            SOAPBody responseBody = response.getSOAPBody();
            response.writeTo(System.out);
            QName responseBodyName = new QName( "GetAllUserInfoResponse");
            Iterator dateResponseIterator = responseBody.getChildElements(responseBodyName);
            SOAPBodyElement getDateElement = (SOAPBodyElement)dateResponseIterator.next();
            QName row = new QName("Row");
            Iterator rowResponseIterator = getDateElement.getChildElements(row);
            
            QName pin = new  QName("PIN");
            
            while(rowResponseIterator.hasNext()){
            
            i = i+1;
            SOAPBodyElement rowElement =  (SOAPBodyElement)rowResponseIterator.next();
            Iterator pinIterator = rowElement.getChildElements(pin);
            SOAPBodyElement pinElement =  (SOAPBodyElement)pinIterator.next();        
            //SOAPBodyElement pinElement =  (SOAPBodyElement)rowResponseIterator.next();
            
            ClockinEmployee employee = new ClockinEmployee(pinElement.getValue());
            list.add(employee);
            
           
            }
            
            
            
            
            
            
            
            
            
        }catch(IOException | SOAPException ex){
            Exceptions.printStackTrace(ex);
        }
        
        
        return list;
        
    }
    
    public String getDeviceTime(){
        int comKeyValue = (int)options.getOptions().get("com_key");
        String date = "We have not managed to get the date from the Device";
        String time = "Tetumanyi saawa ntuufu";
        try{
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMessage =  factory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelop = soapPart.getEnvelope();
            
            SOAPBody soapBody = soapEnvelop.getBody();
            QName bodyName = new QName("http://www.zksoftware/Service/message/", "GetDate");
            
            SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
            QName comArg = new  QName("ArgComKey");
            SOAPElement comKey = bodyElement.addChildElement(comArg);
            comKey.addTextNode(""+comKeyValue+"");
            
            
            QName argArg = new  QName("Arg");
            SOAPElement argKey = bodyElement.addChildElement(argArg);
            
            QName pinArg = new  QName("PIN");
            SOAPElement pinKey = argKey.addChildElement(pinArg);
            
            
            soapMessage.writeTo(System.out);
            
            //Getting Soap Connection
            SOAPConnectionFactory connFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = connFactory.createConnection();
            //Sending the message
            URL endPoint = new URL("http://"+options.getOptions().get("ip")+"/iWsService");
            SOAPMessage response = conn.call(soapMessage, endPoint);
            conn.close();
            SOAPBody responseBody = response.getSOAPBody();
            response.writeTo(System.out);
            QName responseBodyName = new QName( "GetDateResponse");
            Iterator dateResponseIterator = responseBody.getChildElements(responseBodyName);
            SOAPBodyElement getDateElement = (SOAPBodyElement)dateResponseIterator.next();
            QName row = new QName("Row");
            Iterator rowResponseIterator = getDateElement.getChildElements(row);
            SOAPBodyElement rowElement = (SOAPBodyElement)rowResponseIterator.next();
            QName dateQ = new QName("Date");
            Iterator dateIterator = rowElement.getChildElements(dateQ);
            SOAPBodyElement dateElement = (SOAPBodyElement)dateIterator.next();
            date = dateElement.getValue();
            
            QName timeQ = new QName("Time");
            dateIterator = rowElement.getChildElements(timeQ);
            dateElement = (SOAPBodyElement)dateIterator.next();
            time = dateElement.getValue();
            
            
            
            
            
            
            
            
            
            
        }catch(IOException | SOAPException ex){
            Exceptions.printStackTrace(ex);
        }
        
        
        
        return date+" "+time;
    }
    
    private static String getCommandString(String arg){
        return availableCommands.get(arg);
    }
    
    public ZKSoapLib getInstance(){
        return new ZKSoapLib(options);
    }
    
    
    
   
    
    
    
}
