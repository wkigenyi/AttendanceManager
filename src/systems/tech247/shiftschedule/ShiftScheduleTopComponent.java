/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.shiftschedule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import systems.tech247.clockinutil.FactoryShiftSchudules;
import systems.tech247.clockinutil.ShiftSchedule;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.util.MessageType;
import systems.tech247.util.NotifyUtil;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.shiftschedule//ShiftSchedule//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ShiftSTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.shiftschedule.ShiftScheduleTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ShiftScheduleAction",
        preferredID = "ShiftScheduleTopComponent"
)
@Messages({
    "CTL_ShiftScheduleAction=ShiftSchedule",
    "CTL_ShiftScheduleTopComponent=Shift Scheduler",
    "HINT_ShiftScheduleTopComponent="
})
public final class ShiftScheduleTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    Calendar calendar = Calendar.getInstance();
    EntityManager entityManager = DataAccess.entityManager;
    ExplorerManager em = new ExplorerManager();
    Employees emp;
    JSpinner spinner;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SSS");
    
    public ShiftScheduleTopComponent(){
        this(null);
    }
    
    public ShiftScheduleTopComponent(final Employees emp) {
        initComponents();
        setName(Bundle.CTL_ShiftScheduleTopComponent());
        setToolTipText(Bundle.HINT_ShiftScheduleTopComponent());
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        this.emp = emp;
        OutlineView ov = new OutlineView("Month Day");
        ov.getOutline().setRootVisible(false);
        ov.addPropertyColumn("weekDay", "Week Day");
        ov.addPropertyColumn("sCode", "Shift");
        ov.addPropertyColumn("isHoliday", "Holiday");
        ov.addPropertyColumn("isWeeklyOff", "Weekly Off Day");
        ov.addPropertyColumn("isCOff", "Compsn Day Off");
        ov.addPropertyColumn("isLeave", "On Leave");
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
                    ShiftSchedule schedule = node.getLookup().lookup(ShiftSchedule.class);
                   
                        
                        decorateShift(schedule, cell);
                        
                    
                }
                
                
                return cell;
            }
                    
                });
        
        
        
        jpMonthHolder.setLayout(new BorderLayout());
        //Add the month/year spinner
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -5);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 10);
        Date latestDate =  calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.MONTH);
        spinner = addLabeledSpinner(jpMonthHolder, dateModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "MMMMM/yyyy"));
        SpinnerDateModel model = (SpinnerDateModel)spinner.getModel();
                Date date = model.getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

        
        
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SpinnerModel dateModel = spinner.getModel();
                if(dateModel instanceof SpinnerDateModel){
                    
                    Date newDate = ((SpinnerDateModel)dateModel).getDate();
                    
                    Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        
        int yr = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        String sql = "select * FROM PtmShiftSchedule where EmployeeID="+emp.getEmployeeID()+" AND MONTH(ShiftDate)="+month+" and YEAR(ShiftDate)="+yr+"";
                    
                    List list = DataAccess.getShiftSchedule(sql);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
                    if(list.size()>0){
                        
                        makeBusy(true);
                        em.setRootContext(new AbstractNode(Children.create(new FactoryShiftSchudules(list), true)));
                        makeBusy(false);
                        
                    }else{
                        NotifyUtil.show("No Shift Schedules For "+sdf.format(newDate), "Duplicate Last Month Schedule?", MessageType.QUESTION, new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                duplicateShiftSchedule(newDate);
                            }
                        }, false);
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

        jpMonthHolder = new javax.swing.JPanel();
        jpView = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtLeave = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtCompensation = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtHoliday = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtWeeklyOff = new javax.swing.JTextField();

        jpMonthHolder.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpMonthHolderLayout = new javax.swing.GroupLayout(jpMonthHolder);
        jpMonthHolder.setLayout(jpMonthHolderLayout);
        jpMonthHolderLayout.setHorizontalGroup(
            jpMonthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 152, Short.MAX_VALUE)
        );
        jpMonthHolderLayout.setVerticalGroup(
            jpMonthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jpView.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpViewLayout = new javax.swing.GroupLayout(jpView);
        jpView.setLayout(jpViewLayout);
        jpViewLayout.setHorizontalGroup(
            jpViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpViewLayout.setVerticalGroup(
            jpViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jLabel1.text")); // NOI18N

        jtLeave.setEditable(false);
        jtLeave.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jtLeave.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jLabel2.text")); // NOI18N

        jtCompensation.setEditable(false);
        jtCompensation.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jtCompensation.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jLabel3.text")); // NOI18N

        jtHoliday.setEditable(false);
        jtHoliday.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jtHoliday.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jLabel4.text")); // NOI18N

        jtWeeklyOff.setEditable(false);
        jtWeeklyOff.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jtWeeklyOff.text")); // NOI18N

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpMonthHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jtLeave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jtCompensation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jtHoliday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jtWeeklyOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpMonthHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private javax.swing.JPanel jpMonthHolder;
    private javax.swing.JPanel jpView;
    private javax.swing.JTextField jtCompensation;
    private javax.swing.JTextField jtHoliday;
    private javax.swing.JTextField jtLeave;
    private javax.swing.JTextField jtWeeklyOff;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        setName("Shift Schedule -> "+emp.getSurName()+" "+emp.getOtherNames());
        
        SpinnerModel dateModel = spinner.getModel();
                if(dateModel instanceof SpinnerDateModel){
                    
                    Date newDate = ((SpinnerDateModel)dateModel).getDate();
                    
                    Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        
        int yr = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        String sql = "select * FROM PtmShiftSchedule where EmployeeID="+emp.getEmployeeID()+" AND MONTH(ShiftDate)="+month+" and YEAR(ShiftDate)="+yr+"";
                    
                    List list = DataAccess.getShiftSchedule(sql);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
                    if(list.size()>0){
                        
                        makeBusy(true);
                        em.setRootContext(new AbstractNode(Children.create(new FactoryShiftSchudules(list), true)));
                        makeBusy(false);
                        
                    }else{
                        makeBusy(true);
                        em.setRootContext(new AbstractNode(Children.create(new FactoryShiftSchudules(list), true)));
                        makeBusy(false);
                        
                        
                        NotifyUtil.show("No Shift Schedules For "+sdf.format(newDate), "Duplicate Last Month Schedule?", MessageType.QUESTION, new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                duplicateShiftSchedule(newDate);
                            }
                        }, false);
                    }
                    
                    
                    
                }
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
    
    void decorateShift(ShiftSchedule shift,Component cell){
        
        if(shift != null){
            if(shift.getIsLeave()){
                cell.setBackground(Color.GREEN);
            }else if(shift.getIsCOff()){
                cell.setBackground(Color.yellow);
            }else if(shift.getIsHoliday()){
                cell.setBackground(Color.RED);
            }else if(shift.getIsWeekOff()){
                cell.setBackground(Color.PINK);
            }
        }
        
        
    }
    
    void duplicateShiftSchedule(Date date){
        NotifyDescriptor nd = new NotifyDescriptor(
                "Duplicate Previous Shift Schedules?", //Message or component 
                "Shift Schedules", //Title
                NotifyDescriptor.YES_NO_OPTION,//Options 
                NotifyDescriptor.QUESTION_MESSAGE,//Symbol 
                new Object[]{
                    new JButton("Yes, For Only This Employee"){
            
                        
                        
                    },
                    new JButton("Yes, All Employees"){
            @Override
            public void addActionListener(ActionListener l) {
                super.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // We are at the beginning of the month
                Runnable task;
                task = () -> {
                    ProgressHandle ph = ProgressHandleFactory.createHandle("Filling The Schedules");
                    ph.start();
                    //Delete the existing schedules for the month
                    Calendar cal = Calendar.getInstance();
 
                    cal.setTime(date);
                    cal.set(Calendar.DATE, 0);
                    cal.set(Calendar.HOUR, 12);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND, 0);
                    
                    int currentMonthInDB = cal.get(Calendar.MONTH)+1;
                    int currentYear = cal.get(Calendar.YEAR);
                    ph.progress("Cleaning The Month,  Deleting existing schedules");
                    String sql = "DELETE FROM PtmShiftSchedule WHERE year(shiftDate) = "+currentYear+" AND month(ShiftDate)="+currentMonthInDB+"";
                    Query query = entityManager.createNativeQuery(sql);
                    entityManager.getTransaction().begin();
                    query.executeUpdate();
                    entityManager.getTransaction().commit();
                    //Get the list of employees that are to be updated
                    ph.progress("Get them employees");
                    List<Employees> employeeList = DataAccess.searchEmployees(
                            "SELECT e FROM Employees e WHERE e.isDisengaged = 0"
                    );
                    
                    
                    for(Employees emp: employeeList){
                        //Reset the date for the new employee
                        cal.setTime(date);
                        cal.set(Calendar.DATE, 1);
                        cal.set(Calendar.HOUR_OF_DAY,0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND,0);
                        cal.set(Calendar.MILLISECOND, 0);
                        
                        
                        
                        int latestShiftCode = 5;
                        int month =cal.get(Calendar.MONTH);
                        int monthV = cal.get(Calendar.MONTH);
                        int day = 1;
                        //Get the latest shiftcode
                        String latestCode="SELECT TOP 1 ShiftCode FROM PtmShiftSchedule WHERE EmployeeID="+emp.getEmployeeID()+" AND  NOT(ShiftCode=0)";
                        Query queryCode = entityManager.createNativeQuery(latestCode);
                        try{
                            latestShiftCode=(int)queryCode.getSingleResult();
                        }catch(Exception ex){
                            
                        }
                        
                        while(month==monthV){
                            ph.progress("Posting for "+emp.getSurName()+" "+emp.getOtherNames()+" Day "+day);
                            String getInsertID = "SELECT MAX(shiftscheduleid) from PtmShiftSchedule";
                            Query queryID = entityManager.createNativeQuery(getInsertID);
                            int id = ((BigDecimal)queryID.getSingleResult()).intValue();
                            int newID = id+1;
                            String insertQuery = 
                                    "INSERT INTO [dbo].[PtmShiftSchedule]\n" +
                                    "           ([ShiftScheduleId]\n" +//put
                                    "           ,[CoCode]\n" +//default 1
                                    "           ,[EmployeeID]\n" +//get from e
                                    "           ,[ShiftDate]\n" +//the date
                                    "           ,[ShiftCode]\n" +//the latest one
                                    "           ,[DayType]\n" +//null
                                    "           ,[IsHoliday]\n" +//null
                                    "           ,[IsWeekOff]\n" +//0
                                    "           ,[IsCOff]\n" +//0
                                    "           ,[IsLeave]\n" +//0
                                    "           ,[IsOT2Shift]\n" +//null
                                    "           ,[UserID]\n" +//null
                                    "           ,[EntryDate]\n" +//today
                                    "           ,[DepartmentCode]\n" +//null
                                    "           ,[MasterTwoCode])\n" +//null
                                    "     VALUES\n" +
                                    "           (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            
                            Query queryInsert = entityManager.createNativeQuery(insertQuery);
                            queryInsert.setParameter(1, newID);
                            queryInsert.setParameter(2, 1);
                            queryInsert.setParameter(3, emp.getEmployeeID());
                            queryInsert.setParameter(4, cal.getTime());
                            queryInsert.setParameter(5, latestShiftCode);
                            queryInsert.setParameter(8, 0);
                            queryInsert.setParameter(9, 0);
                            queryInsert.setParameter(10,0);
                            queryInsert.setParameter(13, new Date());
                            
                            entityManager.getTransaction().begin();
                            queryInsert.executeUpdate();
                            entityManager.getTransaction().commit();
                            //Now go to the next date
                            cal.add(Calendar.DATE, 1);
                            monthV= cal.get(Calendar.MONTH);
                            day = day+1;
                            
                        }
                    }
                    ph.finish();
                };
                
                RequestProcessor.getDefault().post(task);
                    }
                });
                
                
                
                
                

                
            }
                        
                        
                    },
                    NotifyDescriptor.CANCEL_OPTION
                }, 
                null);
        
        DialogDisplayer.getDefault().notify(nd);
        
        
    }
    
    
    
    
    
}
