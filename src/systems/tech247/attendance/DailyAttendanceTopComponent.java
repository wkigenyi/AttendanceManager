/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.StatusDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
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
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.hr.VwPtmAttendanceWithComment;
import systems.tech247.shiftschedule.CustomOutlineCellRenderer;
import systems.tech247.tareports.ReportAttendanceWithComment;
import systems.tech247.util.CapDownloadable;
import systems.tech247.util.CapPreview;
import systems.tech247.util.CapPrint;
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
    "CTL_DailyAttendanceAction=Daily Attendance",
    "CTL_DailyAttendanceTopComponent=Daily Attendance Register",
    "HINT_DailyAttendanceTopComponent="
})
public final class DailyAttendanceTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {
    OrganizationUnits unit;
    TopComponent tc = WindowManager.getDefault().findTopComponent("DepartmentsTopComponent");
    Lookup.Result<OrganizationUnits> rslt = tc.getLookup().lookupResult(OrganizationUnits.class);
    Calendar calendar = Calendar.getInstance();
    ExplorerManager em = new ExplorerManager();
    InstanceContent content = new InstanceContent();
    Lookup lkp = new AbstractLookup(content);
    //JSpinner spinner = Calendar.getInstance();
    Calendar cfrom = Calendar.getInstance();
    Calendar cto = Calendar.getInstance();
    Date from;
    Date to;
    QueryAttendance query;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    JRDataSource data;
    Boolean quer;
    public DailyAttendanceTopComponent(){
        this(true);
    }
    
    public DailyAttendanceTopComponent(final Boolean all) {
        initComponents();
        setName(Bundle.CTL_DailyAttendanceTopComponent());
        setToolTipText(Bundle.HINT_DailyAttendanceTopComponent());
        this.quer = all;
        if(all){
            setName("Attendance Report");
        }else{
            setName("Absentism Report");
        }
        
        content.add(new CapDownloadable() {
            @Override
            public void download() {
                // TODO: Download the data and show in the view
                load();
            }
        });
        
        
        content.add((CapPrint) () -> {
            Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tc, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
            if(result == NotifyDescriptor.OK_OPTION){
                ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
            ph.start();
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        ph.progress("Getting The Data..");
                        
                            data = UtilZKClockin.generateAttendanceData(unit, from, to,quer);
                        
                        
                        ph.progress("Compiling The Report..");
                        JasperReportBuilder report = new ReportAttendanceWithComment(data, unit, from, to,quer).getReport();
                try {
                    report.print();
                    ph.progress("Done.");
                    ph.finish();

                } catch (DRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                    }
                });
                
            }
            
        });
        content.add((CapPreview) () -> {
            Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tc, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
            if(result == NotifyDescriptor.OK_OPTION){
                ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
            ph.start();
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        ph.progress("Getting The Data..");
                        
                            data = UtilZKClockin.generateAttendanceData(unit, from, to,quer);
                        
                        
                        ph.progress("Compiling The Report..");
                        JasperReportBuilder report = new ReportAttendanceWithComment(data, unit, from, to,quer).getReport();
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
        });
        associateLookup(lkp);
        OutlineView ov = new OutlineView("Month Day");
        ov.getOutline().setRootVisible(false);
        ov.addPropertyColumn("empname", "Employee Name");
        ov.addPropertyColumn("dept", "DEPARTMENT");
        ov.addPropertyColumn("clockin", "Time IN");
        ov.addPropertyColumn("clockout", "Time OUT");
        ov.addPropertyColumn("shift", "Shift");
        ov.addPropertyColumn("hours", "Hours Worked");
        ov.addPropertyColumn("comment", "Comment");
        //ov.addPropertyColumn("isHoliday", "Holiday");
        //ov.addPropertyColumn("isWeeklyOff", "Weekly Off Day");
        //ov.addPropertyColumn("isCOff", "Compsn Day Off");
        //ov.addPropertyColumn("isLeave", "On Leave");
        
        jtLeave.setBackground(Color.GREEN);
        jtCompensation.setBackground(Color.YELLOW);
        jtHoliday.setBackground(Color.red);
        jtWeeklyOff.setBackground(Color.PINK);
        jpView.setLayout(new BorderLayout());
        jpView.add(ov);
        //The editor
        ov.getOutline().setDefaultRenderer(Node.Property.class, 
                new CustomOutlineCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int modelRow  = table.convertRowIndexToModel(row);
                Node node = em.getRootContext().getChildren().getNodeAt(modelRow);
                if(node != null){
                    VwPtmAttendanceWithComment att = node.getLookup().lookup(VwPtmAttendanceWithComment.class);
                   
                        
                        decorateShift(att, cell);
                        
                    
                }
                
                
                return cell;
            }
                    
                });
        
        
        
                //Set the start date, a month behind
        
        cfrom.add(Calendar.MONTH, -1);
        cfrom.set(Calendar.HOUR, 12);
        cfrom.set(Calendar.MINUTE, 0);
        cfrom.set(Calendar.SECOND, 0);
        cfrom.set(Calendar.MILLISECOND, 0);
        cfrom.add(Calendar.DAY_OF_MONTH, -1);
        from = cfrom.getTime();
        
        jdcFrom.setDate(from);
        jdcTo.setMinSelectableDate(from);
        
        cto.set(Calendar.HOUR, 23);
        cto.set(Calendar.MINUTE, 59);
        cto.set(Calendar.SECOND, 59);
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
        jLabel1 = new javax.swing.JLabel();
        jtLeave = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtCompensation = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtHoliday = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtWeeklyOff = new javax.swing.JTextField();
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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel1.text")); // NOI18N

        jtLeave.setEditable(false);
        jtLeave.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtLeave.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel2.text")); // NOI18N

        jtCompensation.setEditable(false);
        jtCompensation.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtCompensation.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel3.text")); // NOI18N

        jtHoliday.setEditable(false);
        jtHoliday.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtHoliday.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel4.text")); // NOI18N

        jtWeeklyOff.setEditable(false);
        jtWeeklyOff.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtWeeklyOff.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtCompensation, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtHoliday, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtWeeklyOff, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jtLeave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jtCompensation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jtHoliday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jtWeeklyOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JPanel jpView;
    private javax.swing.JTextField jtCompensation;
    private javax.swing.JTextField jtHoliday;
    private javax.swing.JTextField jtLeave;
    private javax.swing.JTextField jtWeeklyOff;
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
    

    
    static protected JSpinner addLabeledSpinner(Container c,
                                                
                                                SpinnerModel model) {

 
        JSpinner spinner = new JSpinner(model);
   
        c.add(spinner);
 
        return spinner;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    void decorateShift(VwPtmAttendanceWithComment att,Component cell){
        
        if(att != null){
            if(att.getOnLeave()){
                cell.setBackground(Color.GREEN);
            }else if(att.getIsCOff()){
                cell.setBackground(Color.yellow);
            }else if(att.getIsHoliday()){
                cell.setBackground(Color.RED);
            }else if(att.getIsWeekOff()){
                cell.setBackground(Color.PINK);
            }
        }
        
        
    }
    
    void load(){
        String datefrom = sdf.format(from);
        String dateto = sdf.format(to);
        String sql;
        if(quer){
            sql = "select * FROM vwPtmAttendanceWithComment where ShiftDate>='"+datefrom+"' AND ShiftDate<='"+dateto+"' ORDER BY ShiftDate DESC";
        }else{
            sql = "select * FROM vwPtmAttendanceWithComment where ShiftDate>='"+datefrom+"' AND ShiftDate<='"+dateto+"' AND Comment LIKE 'ABSENT' ORDER BY ShiftDate DESC";
        }
        query = new QueryAttendance(sql);
        
        RequestProcessor.getDefault().post(() -> {
            makeBusy(true);
            em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceList(query), true)));
            makeBusy(false);
        });
        
        //StatusDisplayer.getDefault().setStatusText(sdf.format(to));
                
        
        
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result<OrganizationUnits> rslt = (Lookup.Result<OrganizationUnits>)ev.getSource();
        rslt.allInstances().stream().map((o) -> {
            this.unit = o;
            return o;
        }).forEachOrdered((_item) -> {
            StatusDisplayer.getDefault().setStatusText(unit.getOrganizationUnitName());
        });
    }
    
    
    
}
