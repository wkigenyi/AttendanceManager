/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import systems.tech247.clockinutil.UtilZKClockin;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.VwPtmAttendanceWithComment;
import systems.tech247.shiftschedule.CustomOutlineCellRenderer;
import systems.tech247.tareports.ReportAttendanceWithComment;
import systems.tech247.util.CapDownloadable;
import systems.tech247.util.CapPreview;
import systems.tech247.util.CapPrint;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.attendance//AttendanceRegister//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "AttendanceRegister",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.attendance.AttendanceRegisterTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_AttendanceRegisterAction",
//        preferredID = "AttendanceRegisterTopComponent"
//)
@Messages({
    "CTL_AttendanceRegisterAction=Attendance",
    "CTL_AttendanceRegisterTopComponent=Attendance Register",
    "HINT_AttendanceRegisterTopComponent="
})
public final class AttendanceRegisterTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    Calendar calendar = Calendar.getInstance();
    ExplorerManager em = new ExplorerManager();
    Employees emp;
    //JSpinner spinner;
    InstanceContent content = new InstanceContent();
    Lookup lkp = new AbstractLookup(content);
    //JSpinner spinner = Calendar.getInstance();
    Calendar cfrom = Calendar.getInstance();
    Calendar cto = Calendar.getInstance();
    Date from;
    Date to;
    JRDataSource data;
    Boolean quer = true;
    QueryAttendance query;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    String sql;
    public AttendanceRegisterTopComponent(){
        this(null);
    }
    
    public AttendanceRegisterTopComponent(final Employees emp) {
        initComponents();
        setName(Bundle.CTL_AttendanceRegisterTopComponent()+" ->" +emp.getSurName()+" "+emp.getOtherNames());
        setToolTipText(Bundle.HINT_AttendanceRegisterTopComponent());
       
        associateLookup(new ProxyLookup(ExplorerUtils.createLookup(em, getActionMap()),lkp));
        this.emp = emp;
        
        sql = "SELECT * FROM vwPtmAttendanceWithComment where EmployeeID = "+emp.getEmployeeID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
        OutlineView ov = new OutlineView("Month Day");
        ov.getOutline().setRootVisible(false);
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
        
        Date now = new Date();
        to = new Date(sdf.format(now));
        cto.setTime(to);
        cto.add(Calendar.DAY_OF_MONTH, -1);
        cfrom.setTime(to);
        cfrom.add(Calendar.MONTH, -1);
        cfrom.add(Calendar.DAY_OF_MONTH, 1);

        from = cfrom.getTime();
        jdcFrom.setDate(from);

        to = cto.getTime();
        
        jdcTo.setDate(to);
        jdcFrom.setMaxSelectableDate(to);
        
        jdcTo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcTo && evt.getPropertyName()=="date"){
                    
                    to = DataAccess.cleanDate(jdcTo.getDate());
                    jdcFrom.setMaxSelectableDate(to);
                    
                }
            }
        });
        
        
        
        jdcFrom.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcFrom && evt.getPropertyName()=="date"){
                    
                    from = DataAccess.cleanDate(jdcFrom.getDate());
                    
                    jdcTo.setMinSelectableDate(from);
                    
                    
                }
            }
        });
        
                content.add(new CapDownloadable() {
            @Override
            public void download() {
                // TODO: Download the data and show in the view
                load();
            }
        });
        
        
        content.add((CapPrint) () -> {
            //Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tcDepartments, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
            //if(result == NotifyDescriptor.OK_OPTION){
                ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
            ph.start();
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        ph.progress("Getting The Data..");
                            //String sql = "SELECT * FROM vwPtmAttendanceWithComment where EmployeeID = "+e.getEmployeeID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                            data = UtilZKClockin.generateAttendanceData(sql, from, to);
                        
                        
                        ph.progress("Compiling The Report..");
                        JasperReportBuilder report = new ReportAttendanceWithComment(data,from, to,quer).getReport();
                try {
                    report.print();
                    ph.progress("Done.");
                    ph.finish();

                } catch (DRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                    }
                });
                
           // }
            
        });
        content.add((CapPreview) () -> {
            //Object result = DialogDisplayer.getDefault().notify(new NotifyDescriptor(tcDepartments, "Select A Department", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.PLAIN_MESSAGE, null, null));
            //if(result == NotifyDescriptor.OK_OPTION){
            ProgressHandle ph = ProgressHandleFactory.createHandle("Generating Report, please wait..");
            ph.start();
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        ph.progress("Getting The Data..");
                        
                            data = UtilZKClockin.generateAttendanceData(sql, from, to);
                        
                        
                        ph.progress("Compiling The Report..");
                        JasperReportBuilder report = new ReportAttendanceWithComment(data, from, to,quer).getReport();
                try {
                    report.show(false);
                    ph.progress("Done.");
                    ph.finish();

                } catch (DRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                    }
                });
                
           // }
        });
        
        
        
        
        //jpMonthHolder.setLayout(new BorderLayout());
        //Add the month/year spinner
        //Date initDate = calendar.getTime();
        //calendar.add(Calendar.YEAR, -5);
        //Date earliestDate = calendar.getTime();
        //calendar.add(Calendar.YEAR, 10);
        //Date latestDate =  calendar.getTime();
        //SpinnerModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.MONTH);
        //spinner = addLabeledSpinner(jpMonthHolder, dateModel);
        //spinner.setEditor(new JSpinner.DateEditor(spinner, "MMMMM/yyyy"));
        
        /*spinner.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                
                SpinnerDateModel model = (SpinnerDateModel)spinner.getModel();
                Date date = model.getDate();
                
                if(evt.getSource()==spinner){
                    Date newDate = model.getDate();
                    if(date!=newDate){
                        date = newDate;
                        makeBusy(true);
                        em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceWithComment(emp, date), true)));
                        makeBusy(false);
                    }
                    
                    
                }
                
                
            }
        });*/
        

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
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel1.text")); // NOI18N

        jtLeave.setEditable(false);
        jtLeave.setText(org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jtLeave.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel2.text")); // NOI18N

        jtCompensation.setEditable(false);
        jtCompensation.setText(org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jtCompensation.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel3.text")); // NOI18N

        jtHoliday.setEditable(false);
        jtHoliday.setText(org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jtHoliday.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel4.text")); // NOI18N

        jtWeeklyOff.setEditable(false);
        jtWeeklyOff.setText(org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jtWeeklyOff.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel5.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(AttendanceRegisterTopComponent.class, "AttendanceRegisterTopComponent.jLabel6.text_1")); // NOI18N

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
                        .addGap(57, 57, 57)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
//        setName("Shift Schedule -> "+emp.getSurName()+" "+emp.getOtherNames());
//        SpinnerDateModel model = (SpinnerDateModel)spinner.getModel();
//        makeBusy(true);
//        em.setRootContext(new AbstractNode(Children.create(new FactoryShiftSchudules(emp, model.getDate()), true)));
//        makeBusy(false);
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

        
        String sql  = "SELECT * FROM vwPtmAttendanceWithComment where EmployeeID = "+emp.getEmployeeID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
        
        RequestProcessor.getDefault().post(() -> {
            query = new QueryAttendance(sql, from, to);
            makeBusy(true);
            em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceList(query,from,to), true)));
            makeBusy(false);
        });
        
        //StatusDisplayer.getDefault().setStatusText(sdf.format(to));
                
        
        
    }
    
    
    
}
