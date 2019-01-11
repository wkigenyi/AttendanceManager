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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.AbstractAction;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.settings.ConvertAsProperties;
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
import systems.tech247.clockinutil.FactoryShiftSchudules;
import systems.tech247.clockinutil.UtilZKClockin;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.Employees;
import systems.tech247.hr.PtmShiftSchedule;
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
        preferredID = "ShiftScheduleTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.shiftschedule.ShiftScheduleTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_ShiftScheduleAction",
//        preferredID = "ShiftScheduleTopComponent"
//)
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
    List<Date> holidays = DataAccess.getHolidayDates();
    
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
        //ov.addPropertyColumn("isHoliday", "Holiday");
        ov.addPropertyColumn("isWeeklyOff", "Weekly Off Day");
        ov.addPropertyColumn("isCOff", "Compsn Day Off");
        //ov.addPropertyColumn("isLeave", "On Leave");
        //jtLeave.setBackground(Color.GREEN);
        jtCompensation.setBackground(Color.YELLOW);
        //jtHoliday.setBackground(Color.red);
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
                    PtmShiftSchedule schedule = node.getLookup().lookup(PtmShiftSchedule.class);
                   
                        
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
                                UtilZKClockin.duplicateShiftSchedule(newDate);
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
        jLabel2 = new javax.swing.JLabel();
        jtCompensation = new javax.swing.JTextField();
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

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jLabel2.text")); // NOI18N

        jtCompensation.setEditable(false);
        jtCompensation.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleTopComponent.class, "ShiftScheduleTopComponent.jtCompensation.text")); // NOI18N

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
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtCompensation, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtWeeklyOff, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
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
                        .addComponent(jLabel2)
                        .addComponent(jtCompensation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jtWeeklyOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpMonthHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jpMonthHolder;
    private javax.swing.JPanel jpView;
    private javax.swing.JTextField jtCompensation;
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
                                UtilZKClockin.duplicateShiftSchedule(newDate);
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
    
    void decorateShift(PtmShiftSchedule shift,Component cell){
        
        if(shift != null){
            if(shift.getIsCOff()){
                cell.setBackground(Color.yellow);
//            }else if(!DataAccess.isHoliday(shift.getShiftDate())){
//                cell.setBackground(Color.RED);
            }else if(shift.getIsWeekOff()){
                cell.setBackground(Color.PINK);
            }
        }
        
        
    }
    
    
    
    
    
    
    
}
