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
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
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
import systems.tech247.clockinutil.FactoryAttendance;
import systems.tech247.hr.Employees;
import systems.tech247.shiftschedule.CustomOutlineCellRenderer;

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
@ActionID(category = "Window", id = "systems.tech247.attendance.ShiftScheduleTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_AttendanceAction",
        preferredID = "AttendanceTopComponent"
)
@Messages({
    "CTL_AttendanceAction=Attendance",
    "CTL_AttendanceTopComponent=Attendance Register",
    "HINT_AttendanceTopComponent="
})
public final class AttendanceRegisterTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    Calendar calendar = Calendar.getInstance();
    ExplorerManager em = new ExplorerManager();
    Employees emp;
    JSpinner spinner;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    
    public AttendanceRegisterTopComponent(){
        this(null);
    }
    
    public AttendanceRegisterTopComponent(final Employees emp) {
        initComponents();
        setName(Bundle.CTL_AttendanceTopComponent()+" ->" +emp.getSurName()+" "+emp.getOtherNames());
        setToolTipText(Bundle.HINT_AttendanceTopComponent());
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        this.emp = emp;
        OutlineView ov = new OutlineView("Month Day");
        ov.getOutline().setRootVisible(false);
        ov.addPropertyColumn("clockin", "Time IN");
        ov.addPropertyColumn("clockout", "Time OUT");
        ov.addPropertyColumn("shift", "Shift");
        //ov.addPropertyColumn("isHoliday", "Holiday");
        //ov.addPropertyColumn("isWeeklyOff", "Weekly Off Day");
        //ov.addPropertyColumn("isCOff", "Compsn Day Off");
        //ov.addPropertyColumn("isLeave", "On Leave");
        ov.addPropertyColumn("status", "Absent?");
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
                    Attendance schedule = node.getLookup().lookup(Attendance.class);
                   
                        
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
        
        spinner.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                
                SpinnerDateModel model = (SpinnerDateModel)spinner.getModel();
                Date date = model.getDate();
                
                if(evt.getSource()==spinner){
                    Date newDate = model.getDate();
                    if(date!=newDate){
                        date = newDate;
                        makeBusy(true);
                        em.setRootContext(new AbstractNode(Children.create(new FactoryAttendance(emp, date), true)));
                        makeBusy(false);
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
    
    void decorateShift(Attendance shift,Component cell){
        
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
    
    
    
}
