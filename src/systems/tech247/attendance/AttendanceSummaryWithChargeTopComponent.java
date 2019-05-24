/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.StatusDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;
import systems.tech247.clockinutil.UtilZKClockin;
import systems.tech247.dbaccess.AttendanceSummary;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Currencies;
import systems.tech247.hr.Employees;
import systems.tech247.hr.TblPayrollCode;
import systems.tech247.hr.TblPeriods;
import systems.tech247.tareports.ReportAttendanceSummary;
import systems.tech247.util.CapDownloadable;
import systems.tech247.util.CapPreview;
import systems.tech247.util.CapUploadable;
import systems.tech247.util.NotifyUtil;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.attendance//DailyAttendance//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "DailyAttendance",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.attendance.DailyAttendanceTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_AttendanceRegisterAction",
//        preferredID = "AttendanceRegisterTopComponent"
//)
@Messages({
    "CTL_AttendanceSummaryWithChargesAction=Attendance Summary With Charges",
    "CTL_AttendanceSummaryWithChargesTopComponent=Attendance Summary With Charges",
    "HINT_AttendanceSummaryWithChargesTopComponent="
})
public final class AttendanceSummaryWithChargeTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {
    
    TblPayrollCode code;
    TopComponent tc = WindowManager.getDefault().findTopComponent("PayrollCodesTopComponent");
    Lookup.Result<TblPayrollCode> rslt = tc.getLookup().lookupResult(TblPayrollCode.class);
    Calendar calendar = Calendar.getInstance();
    ExplorerManager em = new ExplorerManager();
    InstanceContent content = new InstanceContent();
    Lookup lkp = new AbstractLookup(content);
    //JSpinner spinner = Calendar.getInstance();
    Calendar cfrom = Calendar.getInstance();
    Calendar cto = Calendar.getInstance();
    Date from;
    Date to;
    QueryAttendanceSummary query;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    JRDataSource data;
    Boolean quer;
    CapPreview enablePreview;
    CapDownloadable enableDownload;
    CapUploadable enableUpload; 
    List<AttendanceSummary> list = new ArrayList();
    public AttendanceSummaryWithChargeTopComponent() {
        initComponents();
        setName(Bundle.CTL_AttendanceSummaryWithChargesTopComponent());
        setToolTipText(Bundle.CTL_AttendanceSummaryWithChargesTopComponent());
        
        enableDownload = this::load;
        enableUpload = this::uploadCharges;
        enablePreview = this::preview;
        content.add(enableDownload);
        
        
//        content.add((CapPrint) () -> {
//            Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tc, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
//            if(result == NotifyDescriptor.OK_OPTION){
//                ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
//                ph.start();
//                RequestProcessor.getDefault().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ph.progress("Getting The Data..");
//                        
//                            data = UtilZKClockin.generateAttendanceData(unit, from, to,quer);
//                        
//                        
//                        ph.progress("Compiling The Report..");
//                        JasperReportBuilder report = new ReportAttendanceWithComment(data, unit, from, to,quer).getReport();
//                try {
//                    report.print();
//                    ph.progress("Done.");
//                    ph.finish();
//
//                } catch (DRException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//                    }
//                });
//                
//            }
//            
//        });
//        content.add((CapPreview) () -> {
//            Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tc, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
//            if(result == NotifyDescriptor.OK_OPTION){
//                ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
//            ph.start();
//                RequestProcessor.getDefault().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ph.progress("Getting The Data..");
//                        
//                            data = UtilZKClockin.generateAttendanceData(unit, from, to,quer);
//                        
//                        
//                        ph.progress("Compiling The Report..");
//                        JasperReportBuilder report = new ReportAttendanceWithComment(data, unit, from, to,quer).getReport();
//                try {
//                    report.show(false);
//                    ph.progress("Done.");
//                    ph.finish();
//
//                } catch (DRException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//                    }
//                });
//                
//            }
//        });
        associateLookup(lkp);
        OutlineView ov = new OutlineView("Summary");
        ov.getOutline().setRootVisible(false);
        //ov.addPropertyColumn("empcode", "PR Number");
        ov.addPropertyColumn("name", "Employee Name");
        ov.addPropertyColumn("dept", "Department");
        ov.addPropertyColumn("position", "Position");
        ov.addPropertyColumn("joined", "Joined");
        ov.addPropertyColumn("days", "Period Days");
        ov.addPropertyColumn("attend", "Attended Days");
        ov.addPropertyColumn("absent", "Absent Days");
        //ov.addPropertyColumn("basic", "Basic Pay");
        //ov.addPropertyColumn("charge", "Absent Amount");
        //ov.addPropertyColumn("isCOff", "Compsn Day Off");
        //ov.addPropertyColumn("isLeave", "On Leave");
        

        jpView.setLayout(new BorderLayout());
        jpView.add(ov);
        //The editor
        
        
        
        
                //Set the start date, a month behind
        
        cfrom.add(Calendar.MONTH, -1);
        cfrom.set(Calendar.HOUR, 12);
        cfrom.set(Calendar.MINUTE, 0);
        cfrom.set(Calendar.SECOND, 0);
        cfrom.set(Calendar.MILLISECOND, 0);
        //cfrom.add(Calendar.DAY_OF_MONTH, -1);
        from = cfrom.getTime();
        
        jdcFrom.setDate(from);
        jdcTo.setMinSelectableDate(from);
        
        cto.set(Calendar.HOUR, 23);
        cto.set(Calendar.MINUTE, 59);
        cto.set(Calendar.SECOND, 59);
        cto.add(Calendar.DAY_OF_MONTH, -1);
        cto.set(Calendar.MILLISECOND, 0);
        to = cto.getTime();
        
        jdcTo.setDate(to);
        jdcFrom.setMaxSelectableDate(to);
        
        jdcTo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcTo && evt.getPropertyName()=="date"){
                    
                    cto.setTime(jdcTo.getDate());
                    cto.set(Calendar.HOUR, 23);
                    cto.set(Calendar.MINUTE, 59);
                    cto.set(Calendar.SECOND, 59);
                    cto.set(Calendar.MILLISECOND, 999);
                    to = cto.getTime();
                    jdcFrom.setMaxSelectableDate(to);
                    if(cto.getTimeInMillis()<cto.getTimeInMillis()){
                        NotifyUtil.error("Date Range Error", "The From Date must be before the To Date", false);
                    }else{
                        //load();
                    }
                }
            }
        });
        
        
        
        jdcFrom.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcFrom && evt.getPropertyName()=="date"){
                    
                    cfrom.setTime(jdcFrom.getDate());
                    cfrom.set(Calendar.HOUR, 0);
                    cfrom.set(Calendar.MINUTE, 0);
                    cfrom.set(Calendar.SECOND, 0);
                    cfrom.set(Calendar.MILLISECOND, 1);
                    from = cfrom.getTime();
                    
                    jdcTo.setMinSelectableDate(from);
                    
                    if(cto.getTimeInMillis()<cfrom.getTimeInMillis()){
                        NotifyUtil.error("Date Range Error", "The From Date must be before the To Date", false);
                    }else{
                        //load();
                    }
                }
            }
        });
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpView = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jdcTo = new com.toedter.calendar.JDateChooser();

        jpView.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpViewLayout = new javax.swing.GroupLayout(jpView);
        jpView.setLayout(jpViewLayout);
        jpViewLayout.setHorizontalGroup(
            jpViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpViewLayout.setVerticalGroup(
            jpViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(AttendanceSummaryWithChargeTopComponent.class, "AttendanceSummaryWithChargeTopComponent.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(AttendanceSummaryWithChargeTopComponent.class, "AttendanceSummaryWithChargeTopComponent.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JPanel jpView;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
            //load();
            rslt.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    

    


    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    void preview(){
        content.remove(enablePreview);
        
        ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
        ph.start();
        RequestProcessor.getDefault().post(new Runnable() {
        @Override
        public void run() {
            ph.progress("Getting The Data..");
            data = UtilZKClockin.generateAttendanceSummary(list);
            ph.progress("Compiling The Report..");
            JasperReportBuilder report = new ReportAttendanceSummary(data, from, to).getReport();
            try {
                report.show(false);
                ph.progress("Done.");
                ph.finish();

            } catch (DRException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        });
                
            }
            
        
        
        
        
        
        
        
        
        
        
        
        
    
    
    void load(){
        content.remove(enableDownload);
        list.clear();
        String datefrom = sdf.format(from);
        String dateto = sdf.format(to);
        String sql;
        
            sql = "select \n" +
"employeeID,\n" +
"empCode,\n" +
"surName,\n" +
"otherNames as otherName,\n" +
"dept,\n" +
"positionName as position,\n" +
"CategoryDetails as category,\n" +
"DateOfEmployment as joined,\n" +
"COUNT(*) AS daysInPeriod,\n" +
"SUM(CASE WHEN COMMENT LIKE '%ABSENT%' THEN 1 ELSE 0 END) AS absentDays,\n" +
"dbo.prlfnGetBasicPay(EmployeeID) AS basicPay\n" +
"FROM vwPtmAttendanceWithComment \n" +
"WHERE SHiftdate >='"+datefrom+"' AND SHiftdate <='"+dateto+"'\n" +
"GROUP BY EmployeeID,EmpCode,SurName,OtherNames,Dept,PositionName,CategoryDetails,DateOfEmployment";
        
        ProgressHandle ph = ProgressHandleFactory.createHandle("Summarising Attendance Details, Please wait");
            ph.start();
            RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                
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
                ph.progress("Added "+list.size()+" "+ surName+" "+otherName);
            }
            
            if(list.size()>0){
                ph.progress("Presenting Summary");
                em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceSummary(list), true)));
                content.add(enableUpload);
                content.add(enablePreview);
            }else{
                ph.progress("No Results");
            }
            
            
            ph.finish();
            content.add(enableDownload);
                
            }
        });
            
            
            
        
//        RequestProcessor.getDefault().post(() -> {
//            makeBusy(true);
//            try{
//            em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceSummary(sql), true)));
//            }catch(Exception ex){
//                
//            }
//                makeBusy(false);
//        });
        
        //StatusDisplayer.getDefault().setStatusText(sdf.format(to));
                
        
        
    }
    
    void uploadCharges(){
        
        TblPeriods period = DataAccess.getCurrentMonth();
        Currencies currency = DataAccess.getBaseCurrency();
        DialogDisplayer.getDefault().notify(new DialogDescriptor(tc,"Select A Payroll Code"));
        
        if(code==null){ //User Did not specify the code
            StatusDisplayer.getDefault().setStatusText("To Proceed, A Code Must Be  Selected");
        }else{
            Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation(code.getPayrollCodeName()+ " was selected.\n\nProceed ?", "Confirm The Code"));
            if(result == NotifyDescriptor.YES_OPTION){
                
                //Check if there are transactions for the specified code in the current period
                if( 1==1 ){ //If The transactions exist
                    Object result2 = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation("The Amount on the specified code will be updated to:\n\n Amount * Attended Days / Period Days\n\n Proceed ?", "Confirm"));
                    if(result2 == NotifyDescriptor.YES_OPTION){
                        
                            content.remove(enableUpload);
                            ProgressHandle ph = ProgressHandleFactory.createHandle("Updating Amounts on Code: "+ code.getPayrollCodeName());
                            ph.start();
                            RequestProcessor.getDefault().post(new Runnable() {
                            @Override
                            public void run() {
                
                                for(int i = 0; i<list.size(); i++){
                                    AttendanceSummary sum = list.get(i);
                                    Employees emp = DataAccess.getEmployeeByID(sum.getEmployeeID());
                                    BigDecimal amount = new BigDecimal(sum.getBasicPay()*sum.getAbsentDays()/sum.getDaysinPeriods()).round(MathContext.DECIMAL32);
                                    ph.progress(i+" / "+list.size()+ " "+emp.getSurName()+" "+emp.getOtherNames());
                                    //DataAccess.saveEmployeeTransaction(emp, code, amount, currency, TOOL_TIP_TEXT_KEY, period);
                                    DataAccess.updateEmployeeTransaction(emp, code, period, sum.getDaysinPeriods(), sum.getDaysinPeriods()-sum.getAbsentDays());
                    
                                }
                            ph.finish();
                
                            }
                            });
                        
                    }
                    
                }else{
                    StatusDisplayer.getDefault().setStatusText("No Transactions where found For "+code.getPayrollCodeName());
                    NotifyUtil.warn("No Transaction Found","No Transactions on Code: "+code.getPayrollCodeName() , false);
                }
                
            }
        }
        
        
        
        
        
        
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result<TblPayrollCode> rslt = (Lookup.Result<TblPayrollCode>)ev.getSource();
        rslt.allInstances().stream().map((o) -> {
            this.code = o;
            return o;
        }).forEachOrdered((_item) -> {
            StatusDisplayer.getDefault().setStatusText(code.getPayrollCodeName());
        });
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("");

    }
    
    
    
    
    
}
