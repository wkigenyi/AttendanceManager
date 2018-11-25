/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.ErrorManager;
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
import systems.tech247.clockinutil.FactoryClockinInfo;
import systems.tech247.hr.Employees;
import systems.tech247.shiftschedule.CustomOutlineCellRenderer;
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
@ActionID(category = "Window", id = "systems.tech247.attendance.ClockinTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ClockinAction",
        preferredID = "ClockinTopComponent"
)
@Messages({
    "CTL_ClockinAction=Clockin Logs",
    "CTL_ClockinTopComponent=Clockin Logs",
    "HINT_ClockinTopComponent="
})
public final class ClockinTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    Calendar cFrom = Calendar.getInstance();
    Calendar cTo = Calendar.getInstance();
    ExplorerManager em = new ExplorerManager();
    Employees emp;
    Date from;
    Date to;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    
    public ClockinTopComponent(){
        this(null);
    }
    
    public ClockinTopComponent(final Employees emp) {
        initComponents();
        setName(Bundle.CTL_ClockinTopComponent()+" ->"+emp.getSurName()+" "+emp.getOtherNames());
        setToolTipText(Bundle.HINT_ClockinTopComponent());
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        this.emp = emp;
        OutlineView ov = new OutlineView("Month Day");
        ov.getOutline().setRootVisible(false);
        
        ov.addPropertyColumn("time", "TIME");
        ov.addPropertyColumn("inout", "IN/OUT");
        //ov.addPropertyColumn("isHoliday", "Holiday");
        //ov.addPropertyColumn("isWeeklyOff", "Weekly Off Day");
        //ov.addPropertyColumn("isCOff", "Compsn Day Off");
        //ov.addPropertyColumn("isLeave", "On Leave");
        ov.addPropertyColumn("sno", "MACHINE Number");

        jpView.setLayout(new BorderLayout());
        jpView.add(ov);
        //The editor
        ov.getOutline().setDefaultRenderer(Node.Property.class, 
                new CustomOutlineCellRenderer(){
            
                    
                });
        
        
        
        
        //Set the start date, a month behind
        
        cFrom.add(Calendar.MONTH, -1);
        cFrom.set(Calendar.HOUR, 12);
        cFrom.set(Calendar.MINUTE, 0);
        cFrom.set(Calendar.SECOND, 0);
        cFrom.set(Calendar.MILLISECOND, 0);
        cFrom.add(Calendar.DAY_OF_MONTH, -1);
        from = cFrom.getTime();
        
        jdcFrom.setDate(from);
        
        cTo.set(Calendar.HOUR, 23);
        cTo.set(Calendar.MINUTE, 59);
        cTo.set(Calendar.SECOND, 59);
        cTo.set(Calendar.MILLISECOND, 0);
        to = cTo.getTime();
        
        jdcTo.setDate(to);
        
        jdcTo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcTo && evt.getPropertyName()=="date"){
                    
                    cTo.setTime(jdcTo.getDate());
                    cTo.set(Calendar.HOUR, 23);
                    cTo.set(Calendar.MINUTE, 59);
                    cTo.set(Calendar.SECOND, 59);
                    cTo.set(Calendar.MILLISECOND, 999);
                    to = cTo.getTime();
                    
                    if(cTo.getTimeInMillis()<cFrom.getTimeInMillis()){
                        NotifyUtil.error("Date Range Error", "The From Date must be before the To Date", false);
                    }else{
                        em.setRootContext(new AbstractNode(Children.create(new FactoryClockinInfo(emp, from, to), true)));
                    }
                }
            }
        });
        
        jdcFrom.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcFrom && evt.getPropertyName()=="date"){
                    
                    cFrom.setTime(jdcFrom.getDate());
                    cFrom.set(Calendar.HOUR, 0);
                    cFrom.set(Calendar.MINUTE, 0);
                    cFrom.set(Calendar.SECOND, 0);
                    cFrom.set(Calendar.MILLISECOND, 1);
                    from = cFrom.getTime();
                    
                    
                    
                    if(cTo.getTimeInMillis()<cFrom.getTimeInMillis()){
                        NotifyUtil.error("Date Range Error", "The From Date must be before the To Date", false);
                    }else{
                        em.setRootContext(new AbstractNode(Children.create(new FactoryClockinInfo(emp, from, to), true)));
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
        jLabel2 = new javax.swing.JLabel();
        jdcFrom = new com.toedter.calendar.JDateChooser();
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
            .addGap(0, 246, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ClockinTopComponent.class, "ClockinTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ClockinTopComponent.class, "ClockinTopComponent.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 61, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JPanel jpView;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        em.setRootContext(new AbstractNode(Children.create(new FactoryClockinInfo(emp, from, to), true)));
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
    
    
    
    
    
}
