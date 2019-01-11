/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.math.BigDecimal;
import java.util.Date;
import systems.tech247.dbaccess.AttendanceSummary;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

import systems.tech247.api.ReloadableQueryCapability;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.util.NotifyUtil;

/**
 *
 * @author Wilfred
 */
public class FactoryAttendanceSummary extends ChildFactory<AttendanceSummary>{

    QueryAttendanceSummary query;
    String sql;
    Date from;
    Date to;
    List<AttendanceSummary> lists;
    
    
    public FactoryAttendanceSummary(QueryAttendanceSummary query){
        this.query = query;
    }
    
    public FactoryAttendanceSummary(String sql){
        this.sql = sql;
    }
    
    public FactoryAttendanceSummary(Date from, Date to){
        this.from = from;
        this.to = to;
    }
    
    public FactoryAttendanceSummary(List<AttendanceSummary> list){
        this.lists = list;
    }
    
    @Override
    protected boolean createKeys(List<AttendanceSummary> list) {
        //get this ability from the look
        if(null!=query){
            
        
        ReloadableQueryCapability r = query.getLookup().lookup(ReloadableQueryCapability.class);
        //Use the ability
        if(r != null){
            try{
                r.reload();
            }catch(Exception ex){
                
            }
        }
        //Populate the list of child entries
        /*list.add(new AddTool(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeePersonalInfoEditorTopComponent newEditor = new EmployeePersonalInfoEditorTopComponent();
                newEditor.open();
                newEditor.requestActive();
            }
        }));*/
        list.addAll(query.getList());
        }
        if(null!=sql){
            ProgressHandle ph = ProgressHandleFactory.createHandle("Summarising Attendance Details, Please wait");
            ph.start();
            List<Object[]> resultList =  DataAccess.getAttendanceSummary(sql);
            
            for(Object[] o : resultList){
                int id = (int)o[0];
                String code = (String)o[1];
                String surName = (String)o[2];
                String otherName = (String)o[3];
                String dept = (String)o[4];
                String pos = (String)o[5];
                String cat = (String)o[6];
                Date date = (Date)o[7];
                int days = (int)o[8];
                int absent = (int)o[9];
                BigDecimal amount = (BigDecimal)o[10];
                Double amt = amount.doubleValue();
                AttendanceSummary sum = new AttendanceSummary(id, date, code, surName, otherName, dept, cat, pos, days, absent, amt);
                
                list.add(sum);
            }
            
            
            //list.addAll(DataAccess.getAttendanceSummary(sql));
            ph.finish();
        }
        if(null != to && null != from){
            ProgressHandle ph = ProgressHandleFactory.createHandle("Summarising Attendance Details, Please wait");
            ph.start();
            list.addAll(DataAccess.getAttendanceSummary(from,to));
            ph.finish();
        }
        
        if(null != lists){
            list.addAll(lists);
        }
        
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(AttendanceSummary key){
        Node node = null;
        try{
            node = new NodeAttendanceSummary(key);
        }catch(Exception ex){
            NotifyUtil.error("Error", "Error", ex, false);
        }
        
        return node;
    }
    
    
    
}
