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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
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
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.EmployeeCategories;
import systems.tech247.hr.Employees;
import systems.tech247.hr.JobPositions;
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
    
    String sqlString ;
    OrganizationUnits unit;
    EmployeeCategories cat;
    JobPositions position;
    TopComponent tcDepartments = WindowManager.getDefault().findTopComponent("DepartmentsTopComponent");
    TopComponent tcCategories = WindowManager.getDefault().findTopComponent("CategoriesTopComponent");
    TopComponent tcPositions = WindowManager.getDefault().findTopComponent("PositionsTopComponent");
    TopComponent tcEmployees = WindowManager.getDefault().findTopComponent("EmployeesTopComponent");
    Lookup.Result<OrganizationUnits> rsltDepartment = tcDepartments.getLookup().lookupResult(OrganizationUnits.class);
    Lookup.Result<Employees> rsltEmployee = tcEmployees.getLookup().lookupResult(Employees.class);
    Lookup.Result<JobPositions> rsltPosition = tcPositions.getLookup().lookupResult(JobPositions.class);
    Lookup.Result<EmployeeCategories> rsltCategory = tcCategories.getLookup().lookupResult(EmployeeCategories.class);
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
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    JRDataSource data;
    Boolean quer;
    List<Employees> list = new ArrayList();
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
        
        if(quer){
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment WHERE shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                }else{
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment WHERE shiftdate >= ? AND ShiftDate <=? AND Comment LIKE '%ABSENT%' ORDER BY EmployeeID,ShiftDate DESC";
                }
        
        
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
                        
                            data = UtilZKClockin.generateAttendanceData(sqlString, from, to);
                        
                        
                        ph.progress("Compiling The Report..");
                        JasperReportBuilder report = new ReportAttendanceWithComment(data, from, to,quer).getReport();
                try {
                    report.print();
                    ph.progress("Done.");
                    ph.finish();

                } catch (DRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                    }
                });
                
            //}
            
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
                        
                            data = UtilZKClockin.generateAttendanceData(sqlString, from, to);
                        
                        
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
                
            //}
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
                    
                    
                    from = DataAccess.cleanDate(jdcFrom.getDate());
                    
                    jdcTo.setMinSelectableDate(from);
                    
                    if(cto.getTimeInMillis()<cfrom.getTimeInMillis()){
                        NotifyUtil.error("Date Range Error", "The From Date must be before the To Date", false);
                    }else{
                        //load();
                    }
                }
            }
        });
        
        jtDepartmentFilter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWindow(tcDepartments, "Select A Department");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        jtCategoryFilter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWindow(tcCategories, "Select A Category");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        jtPositionFilter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWindow(tcPositions, "Select A Position");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        jtEmployeeFilter.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showWindow(tcEmployees, "Select An Employee");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        jLabel7 = new javax.swing.JLabel();
        jtEmployeeFilter = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtPositionFilter = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtDepartmentFilter = new javax.swing.JTextField();
        jtCategoryFilter = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel7.text")); // NOI18N

        jtEmployeeFilter.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtEmployeeFilter.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel8.text")); // NOI18N

        jtPositionFilter.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtPositionFilter.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel9.text")); // NOI18N

        jtDepartmentFilter.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtDepartmentFilter.text")); // NOI18N

        jtCategoryFilter.setText(org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jtCategoryFilter.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jLabel10.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(DailyAttendanceTopComponent.class, "DailyAttendanceTopComponent.jButton1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 89, Short.MAX_VALUE))
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jtCategoryFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                        .addComponent(jtDepartmentFilter, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jtPositionFilter, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jtEmployeeFilter, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtEmployeeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtPositionFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtDepartmentFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtCategoryFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JPanel jpView;
    private javax.swing.JTextField jtCategoryFilter;
    private javax.swing.JTextField jtCompensation;
    private javax.swing.JTextField jtDepartmentFilter;
    private javax.swing.JTextField jtEmployeeFilter;
    private javax.swing.JTextField jtHoliday;
    private javax.swing.JTextField jtLeave;
    private javax.swing.JTextField jtPositionFilter;
    private javax.swing.JTextField jtWeeklyOff;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
            //load();
            rsltDepartment.addLookupListener(this);
            rsltEmployee.addLookupListener(this);
            rsltCategory.addLookupListener(this);
            rsltPosition.addLookupListener(this);
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

        
        
        
        RequestProcessor.getDefault().post(() -> {
            query = new QueryAttendance(sqlString, from, to);
            makeBusy(true);
            em.setRootContext(new AbstractNode(Children.create(new FactoryAttendanceList(query,from,to), true)));
            makeBusy(false);
        });
        
        //StatusDisplayer.getDefault().setStatusText(sdf.format(to));
                
        
        
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result rslt = (Lookup.Result)ev.getSource();
        for(Object o: rslt.allInstances()){
            if(o instanceof Employees){
                Employees e = (Employees)o;
                
                list.clear();
                //list.add(e);
                
                if(quer){
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where EmployeeID = "+e.getEmployeeID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                }else{
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where EmployeeID = "+e.getEmployeeID()+" AND shiftdate >= ? AND ShiftDate <=? AND Comment LIKE '%ABSENT%' ORDER BY EmployeeID,ShiftDate DESC";
                }
                //query = new QueryAttendance(sqlString);
                clearFilters();
                jtEmployeeFilter.setText(e.getSurName()+e.getOtherNames());
                
            }else if(o instanceof JobPositions){
                JobPositions j = (JobPositions)o;
                if(quer){
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where PositionID = "+j.getPositionID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                }else{
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where PositionID = "+j.getPositionID()+" AND shiftdate >= ? AND ShiftDate <=? AND Comment LIKE '%ABSENT%' ORDER BY EmployeeID,ShiftDate DESC";
                }
                //query = new QueryAttendance(sqlString);
                clearFilters();
                jtPositionFilter.setText(j.getPositionName());
                
                
                
            }else if(o instanceof EmployeeCategories){
                EmployeeCategories cat = (EmployeeCategories)o;
                if(quer){
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where CategoryID = "+cat.getCategoryID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                }else{
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where CategoryID = "+cat.getCategoryID()+" AND shiftdate >= ? AND ShiftDate <=? AND Comment LIKE '%ABSENT%' ORDER BY EmployeeID,ShiftDate DESC";
                }
                //query = new QueryAttendance(sqlString);
                clearFilters();
                jtCategoryFilter.setText(cat.getCategoryName());
                
            }else if(o instanceof OrganizationUnits){
                unit = (OrganizationUnits)o;
                if(quer){
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where deptID = "+unit.getOrganizationUnitID()+" AND shiftdate >= ? AND ShiftDate <=? ORDER BY ShiftDate DESC";
                }else{
                    sqlString = "SELECT * FROM vwPtmAttendanceWithComment where deptID = "+unit.getOrganizationUnitID()+" AND shiftdate >= ? AND ShiftDate <=? AND Comment LIKE '%ABSENT%' ORDER BY EmployeeID,ShiftDate DESC";
                }
                //query = new QueryAttendance(sqlString);
                clearFilters();
                jtDepartmentFilter.setText(unit.getOrganizationUnitName());
            }
        }
    }
    
    void showWindow(TopComponent tc,String message){
        DialogDisplayer.getDefault().notify(new DialogDescriptor(tc,message));
    }
    
    void clearFilters(){
        jtEmployeeFilter.setText("");
        jtPositionFilter.setText("");
        jtCategoryFilter.setText("");
        jtDepartmentFilter.setText("");
    }
    
    
    
}
