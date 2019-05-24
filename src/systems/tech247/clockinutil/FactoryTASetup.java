/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import systems.tech247.util.SetupItem;
import systems.tech247.util.NodeSetupItem;
import java.util.List;
import javax.swing.AbstractAction;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import systems.tech247.attendance.AttendanceSummaryWithChargeTopComponent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.hr.TblPeriods;
import systems.tech247.shiftschedule.DownloadScheduleTemplate;
import systems.tech247.tareports.ReportScheduleTemplate;

/**
 *
 * @author WKigenyi
 */
public class FactoryTASetup extends ChildFactory<SetupItem> {
    
    @Override
    protected boolean createKeys(List<SetupItem> toPopulate) {
        toPopulate.add(new SetupItem("Shifts",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new ShiftsTopComponent("view");
                tc.open();
                tc.requestActive();
                        
            }
        }));
        
        toPopulate.add(new SetupItem("Holidays",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("HolidaysTopComponent");
                tc.open();
                tc.requestActive();
                        
            }
        }));
        
        
        toPopulate.add(new SetupItem("Assign Shift Schedules", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TblPeriods period = DataAccess.getCurrentMonth();
                List<Employees> listEmp = DataAccess.searchEmployees("SELECT e FROM Employees e WHERE e.isDisengaged= 0");
                UtilZKClockin.duplicateShiftSchedule(period,listEmp);
            }
        }));
        
        
        
        toPopulate.add(new SetupItem("Import Attendance Schedules", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("ImportScheduleTopComponent");
                tc.open();
                tc.requestActive();
            }
        }));
        
        toPopulate.add(new SetupItem("Import Clockin Data", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("ImportClockinsTopComponent");
                tc.open();
                tc.requestActive();
            }
        }));
        
        
        
        
        toPopulate.add(new SetupItem("Download Schedule Template", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TblPeriods currentPeriod = DataAccess.getCurrentMonth();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                Calendar calfrom = Calendar.getInstance();
                calfrom.set(Calendar.YEAR, currentPeriod.getPeriodYear());
                calfrom.set(Calendar.MONTH, DataAccess.covertMonthsToInt(currentPeriod.getPeriodMonth()));
                calfrom.set(Calendar.DAY_OF_MONTH,26);
                Date from = calfrom.getTime();
                Calendar calto = Calendar.getInstance();
                calto.set(Calendar.YEAR, currentPeriod.getPeriodYear());
                calto.set(Calendar.MONTH, (DataAccess.covertMonthsToInt(currentPeriod.getPeriodMonth())+1));
                calto.set(Calendar.DAY_OF_MONTH,25);
                Date to = calto.getTime();
                OrganizationUnits unit;
                
                
                DownloadScheduleTemplate dls = new DownloadScheduleTemplate(null, from, to);
                Object result = DialogDisplayer.getDefault().notify(new DialogDescriptor(dls,"Specify Dept and Dates"));
                if(result == NotifyDescriptor.OK_OPTION){
                    unit = dls.getSelectedDept();
                    Date froms = dls.getFrom();
                    Date tos = dls.getTo();
                    if(unit != null){
                        StatusDisplayer.getDefault().setStatusText("Start Procesing the template");
                        ProgressHandle ph = ProgressHandleFactory.createHandle("Putting your template together");
                        RequestProcessor.getDefault().post(new Runnable() {
                            @Override
                            public void run() {
                                ph.start();
                                ph.progress("Getting Employee Code");
                                
                                List<Employees> list = DataAccess.searchEmployeesByDepartment(unit.getOrganizationUnitID());
                                //Make the list of dates
                                Calendar calMoving = Calendar.getInstance();
                                calMoving.setTime(froms);
                                calMoving.set(Calendar.HOUR,0);
                                calMoving.set(Calendar.MINUTE, 0);
                                calMoving.set(Calendar.SECOND, 0);
                                calMoving.set(Calendar.MILLISECOND, 0);
                                List<Date> dates = new ArrayList();
                                
                                Calendar calEnd = Calendar.getInstance();
                                calEnd.setTime(tos);
                               
                                
                                do {
                                    System.out.println(calMoving.getTime());
                                    dates.add(calMoving.getTime());
                                    calMoving.add(Calendar.DATE, 1);
                                } 
                                while(calMoving.compareTo(calEnd) < 1);
                                System.out.println(dates.size()+" Days");
                                
                                ph.progress("Establishing the columns");
                                ColumnBuilder[] columns = new ColumnBuilder[dates.size()+2];
                                TextColumnBuilder name = DynamicReports.col.column("Name", "empname",DynamicReports.type.stringType());
                                columns[0] = name;
                                TextColumnBuilder empcode = DynamicReports.col.column("PR #", "empcode",DynamicReports.type.stringType());
                                columns[1] = empcode;
                                for(int i=0; i<dates.size(); i++){
                                    String label = sdf.format(dates.get(i));
                                    columns[i+2] = DynamicReports.col.column(label,label,DynamicReports.type.stringType());
                                    
                                }
                                
                                
                                
                                
                                
                                ph.progress("Building The Report Data");
                                JRDataSource data = UtilZKClockin.generateScheduleData(list,dates);
                                ph.progress("Now Building..");
                                JasperReportBuilder report = new ReportScheduleTemplate(data,unit,columns).getReport();
                                try{
                                    report.show(false);
                                    ph.progress("Done.");
                                    ph.finish();
                                }catch(DRException ex){
                                    
                                }
                                        
                                        
                                        
           
                            }
                        });
                        
                        
                        
                    }else{
                        StatusDisplayer.getDefault().setStatusText("Department Was not selected, you try again");
                    }
                }
                
                
                
                
                
                
            }
        }));
        
        toPopulate.add(new SetupItem("Download Clockin Data", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("TAClockinTopComponent");
                tc.open();
                tc.requestActive();
            }
        }));
        
        

        
        toPopulate.add(new SetupItem("Pass Data To Payroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Post absentism charges to employees
                //Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation("This will post absentism charges on the employees.\nEnsure that Off Days and Leave Days have been assigned before proceeding.\nProceed ?","Process Absentism"));
                TopComponent tc = new AttendanceSummaryWithChargeTopComponent();
                tc.open();
                tc.requestActive();
            }
        }));
        
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(SetupItem key) {
        
        Node node =  null;
        try {
            
            node = new NodeSetupItem(key);
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}
