/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.ErrorManager;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import systems.tech247.dbaccess.BooleanEditor;
import systems.tech247.dbaccess.DataAccess;

/**
 *
 * @author Admin
 */
public class NodeSchedule extends AbstractNode implements PropertyChangeListener{
    
    
    EntityManager entityManager = DataAccess.entityManager;
    ShiftSchedule machine;
    public NodeSchedule(ShiftSchedule machine) throws IntrospectionException{
        super(Children.LEAF, Lookups.singleton(machine));
        this.machine = machine;
        setDisplayName(machine.getDate());
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        addPropertyChangeListener(WeakListeners.propertyChange(this, machine));
        
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        final ShiftSchedule schedule = getLookup().lookup(ShiftSchedule.class);
        
        
        
        try{
            Property weekDay = new PropertySupport.Reflection(schedule, String.class, "getWeekDay", null);
            weekDay.setName("weekDay");
            set.put(weekDay);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }
        try{
            Property sCode = new PropertySupport.Reflection(schedule,String.class,"getsCode",null);
            sCode.setName("sCode");
            set.put(sCode);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }    
        
        try{
            Property isHoliday = new PropertySupport.Reflection(schedule,Boolean.class,"getIsHoliday",null);
            isHoliday.setName("isHoliday");
            set.put(isHoliday);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }
        
        
      //Weekly Day Off Can be Changed
        try {
            
            final PropertySupport.Reflection testValue;
            Property isWeekOffP;
            
            testValue = new PropertySupport.Reflection(schedule, Boolean.class, "weeklyOff");
            testValue.setPropertyEditorClass(BooleanEditor.class);
            set.put(testValue);
            
            
            
            
            isWeekOffP = new PropertySupport(
                    "isWeeklyOff", 
                    Boolean.class, 
                    "Weekly Off", 
                    "Weekly Employee Off?", true, true) {
                @Override
                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                    return schedule.getIsWeekOff();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    
                        //Confirm we are editing
                        StatusDisplayer.getDefault().setStatusText("Weekly Off Updated");
                        String updateQuery = "UPDATE [dbo].[PtmShiftSchedule]\n" +
"   SET [IsWeekOff] = '"+val+"' WHERE ShiftScheduleId = "+schedule.getShiftScheduleId()+"";
                        Query query = entityManager.createNativeQuery(updateQuery);
                        entityManager.getTransaction().begin();
                        query.executeUpdate();
                        entityManager.getTransaction().commit();
                        
                        
                        testValue.setValue(val);
                        
                        
                       
                        
                        
                    
                }
            };
            set.put(isWeekOffP);
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        
        
        //Compensatory Day Off Can be Changed
        try {
            
            final PropertySupport.Reflection testValue;
            Property isCompOffP;
            
            testValue = new PropertySupport.Reflection(schedule, Boolean.class, "compOff");
            testValue.setPropertyEditorClass(BooleanEditor.class);
            set.put(testValue);
            
            
            
            
            isCompOffP = new PropertySupport(
                    "isCOff", 
                    Boolean.class, 
                    "Comp Day Off", 
                    "Comp Day Off?", true, true) {
                @Override
                public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                    return schedule.getIsCOff();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    
                        //Confirm we are editing
                        StatusDisplayer.getDefault().setStatusText("Compensatory Day Off Assigned");
                        //Show the change in the Property
                        testValue.setValue(val);
                        
                        
                       
                        
                        
                    
                }
            };
            set.put(isCompOffP);
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        
        
        
        
        
        
        
        
        /*
        try{
            Property isCOff = new PropertySupport.Reflection(schedule,Boolean.class,"getIsCOff",null);
            isCOff.setName("isCOff");
            set.put(isCOff);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }*/
        
        try{
            Property isLeave = new PropertySupport.Reflection(schedule,Boolean.class,"getIsLeave",null);
            isLeave.setName("isLeave");
            set.put(isLeave);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }
        
        try{
            Property isWeekOff = new PropertySupport.Reflection(schedule,Boolean.class,"getIsWeekOff",null);
            isWeekOff.setName("isWeekOff");
            set.put(isWeekOff);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }
        
        
        
        sheet.put(set);
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("isWeekOff".equals(evt.getPropertyName())){
            this.fireDisplayNameChange("null", getDisplayName());
        }
    }
    
    
    

    
    
    
    
}
