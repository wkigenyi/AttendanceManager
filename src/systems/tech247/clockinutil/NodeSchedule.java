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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import systems.tech247.dbaccess.BooleanEditor;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmShiftSchedule;
import systems.tech247.shiftschedule.ShiftScheduleEditorTopComponent;
import systems.tech247.util.CapEditable;

/**
 *
 * @author Admin
 */
public class NodeSchedule extends AbstractNode implements PropertyChangeListener{
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    List<Date> holidays = DataAccess.getHolidayDates();
    EntityManager entityManager = DataAccess.entityManager;
    ShiftSchedule schedule;
    public NodeSchedule(PtmShiftSchedule schedule) throws IntrospectionException{
        this(schedule,new InstanceContent());
    }
            
    public NodeSchedule(final PtmShiftSchedule schedule, InstanceContent ic) throws IntrospectionException{
        super(Children.LEAF, new AbstractLookup(ic));
        
        ic.add(schedule);
        ic.add(new CapEditable() {
            @Override
            public void edit() {
                TopComponent tc = new ShiftScheduleEditorTopComponent(schedule);
                tc.open();
                tc.requestActive();
            }
        });
        setDisplayName(sdf.format(schedule.getShiftDate()));
        setIconBaseWithExtension("systems/tech247/util/icons/Calendar.png");
        addPropertyChangeListener(WeakListeners.propertyChange(this, schedule));
        
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        final SimpleDateFormat sdf = new SimpleDateFormat("EEEEE");
        final PtmShiftSchedule schedule = getLookup().lookup(PtmShiftSchedule.class);
        
        
        
        Property weekday = new PropertySupport("weekDay", String.class, "Week day", "Week day", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return sdf.format(schedule.getShiftDate());
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        set.put(weekday);
        try{
            Property sCode = new PropertySupport.Reflection(schedule,String.class,"getsCode",null);
            sCode.setName("sCode");
            set.put(sCode);
        }catch(NoSuchMethodException ex){
            ErrorManager.getDefault();
        }    
        
//        try{
//            Property isHoliday = new PropertySupport.Reflection(schedule,Boolean.class,"getIsHoliday",null);
//            isHoliday.setName("isHoliday");
//            set.put(isHoliday);
//        }catch(NoSuchMethodException ex){
//            ErrorManager.getDefault();
//        }
        
//        Property isHoliday = new PropertySupport("isHoliday", Boolean.class, "Holiday", "Holiday", true, false) {
//            @Override
//            public Object getValue() throws IllegalAccessException, InvocationTargetException {
//                return DataAccess.isHoliday(schedule.getShiftDate());
//            }
//            
//            @Override
//            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        };
//        set.put(isHoliday);
        
        
      //Weekly Day Off Can be Changed
        try {
            
            final PropertySupport.Reflection testValue;
            Property isWeekOffP;
            
            testValue = new PropertySupport.Reflection(schedule, Boolean.class, "IsWeekOff");
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
            
            testValue = new PropertySupport.Reflection(schedule, Boolean.class, "IsCOff");
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
        
        Property shift = new PropertySupport("sCode", String.class, "SHIFT", "SHIFT", true, false) {
            @Override
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                try{
                    return DataAccess.getShiftByID(schedule.getShiftCode()).getShiftName();
                }catch(NullPointerException ex){
                    return "Unknown Shift";
                }
            }
            
            @Override
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        
        set.put(shift);
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
