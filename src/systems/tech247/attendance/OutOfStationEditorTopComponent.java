/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.attendance;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.dbaccess.DataAccess;

import systems.tech247.hr.Employees;
import systems.tech247.hr.PtmOutstationVisits;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.attendance//OutOfStationEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "OutOfStationEditorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.attendance.OutOfStationEditorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_OutOfStationEditorAction",
        preferredID = "OutOfStationEditorTopComponent"
)
@Messages({
    "CTL_OutOfStationEditorAction=Out Of Station Visit Editor",
    "CTL_OutOfStationEditorTopComponent=Out Of Station",
    "HINT_OutOfStationEditorTopComponent=This is a OutOfStationEditor window"
})
public final class OutOfStationEditorTopComponent extends TopComponent {
    
    Date dateFrom;
    Calendar from = Calendar.getInstance();
    Date dateTo;
    Calendar to = Calendar.getInstance();
    PtmOutstationVisits visit;
    Employees emp;
    String remarks;
    InstanceContent ic = new InstanceContent();
    PtmOutstationVisits updateable;
    EntityManager entityManager = DataAccess.entityManager;
    
    public OutOfStationEditorTopComponent(){
        this(null,null);
    }
    
    public OutOfStationEditorTopComponent(Employees emp){
        this(null, emp);
    }
    

    
    public OutOfStationEditorTopComponent(PtmOutstationVisits visit,Employees emp) {
        initComponents();
        setName(Bundle.CTL_OutOfStationEditorTopComponent());
        setToolTipText(Bundle.HINT_OutOfStationEditorTopComponent());
        
        this.visit = visit;
        this.emp = emp;
        try{
            updateable = entityManager.find(PtmOutstationVisits.class, visit.getOutstationID());
            
        }catch(NullPointerException ex){
            
        }
        if(visit!=null){
            jdcFrom.setDate(visit.getFromDate());
            jdcTo.setDate(visit.getToDate());
            jtRemarks.setText(visit.getRemarks());
            dateFrom = visit.getFromDate();
            dateTo = visit.getToDate();
            remarks = visit.getRemarks();
            this.emp = visit.getEmployeeID();
        }
        
        
        
        jdcFrom.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcFrom && evt.getPropertyName()=="date"){
                    dateFrom = jdcFrom.getDate();
                    from.setTime(dateFrom);
                    from.set(Calendar.HOUR_OF_DAY, 0);
                    from.set(Calendar.MINUTE, 0);
                    from.set(Calendar.SECOND, 0);
                    from.set(Calendar.MILLISECOND, 0);
                    dateFrom = from.getTime();
                    try{
                        updateable.setFromDate(dateFrom);
                    }catch(NullPointerException ex){
                        
                    }
                    modify();
                }
            }
        });
        
        jdcTo.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getSource()==jdcTo && evt.getPropertyName()=="date"){
                    dateTo = jdcTo.getDate();
                    to.setTime(dateTo);
                    to.set(Calendar.HOUR_OF_DAY, 0);
                    to.set(Calendar.MINUTE, 0);
                    to.set(Calendar.SECOND, 0);
                    to.set(Calendar.MILLISECOND, 0);
                    dateTo = to.getTime();
                    
                    try{
                        updateable.setToDate(dateTo);
                    }catch(NullPointerException ex){
                        
                    }
                    modify();
                }
            }
        });
        
        jtRemarks.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                remarks = jtRemarks.getText();
                try{
                    updateable.setRemarks(remarks);
                }catch(NullPointerException ex){
                    
                }
                modify();
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

        jLabel1 = new javax.swing.JLabel();
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdcTo = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtRemarks = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(OutOfStationEditorTopComponent.class, "OutOfStationEditorTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(OutOfStationEditorTopComponent.class, "OutOfStationEditorTopComponent.jLabel2.text")); // NOI18N

        jtRemarks.setColumns(20);
        jtRemarks.setRows(5);
        jScrollPane1.setViewportView(jtRemarks);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(OutOfStationEditorTopComponent.class, "OutOfStationEditorTopComponent.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcTo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JTextArea jtRemarks;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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
    
    private class OSVSavable extends AbstractSavable{
        
        OSVSavable(){
            register();
        }

        @Override
        protected String findDisplayName() {
            if(null==visit){
                return "New Out Of Station Visit";
            }else{
                return "OSV For" + emp.getSurName()+" "+emp.getOtherNames();
            }
        }
        
        OutOfStationEditorTopComponent tc(){
            return OutOfStationEditorTopComponent.this;
        }

        @Override
        protected void handleSave() throws IOException {
            
            tc().ic.remove(this);
            unregister();
            //New Visit
            if(null==visit){
                
                
                        String insertSQL = "INSERT INTO [dbo].[PtmOutstationVisits]\n" +
"           ([CoCode]\n" +
"           ,[EmployeeID]\n" +
"           ,[YearID]\n" +
"           ,[MonthId]\n" +
"           ,[FromDate]\n" +
"           ,[ToDate]\n" +
"           ,[UserId]\n" +
"           ,[OutstationEntryDate]\n" +
"           ,[Remarks])\n" +
"     VALUES\n" +
"           (?,?,?,?,?,?,?,?,?)";
            Query query = entityManager.createNativeQuery(insertSQL);
            query.setParameter(1, 1);
            query.setParameter(2, emp.getEmployeeID());
            
           
            query.setParameter(5, dateFrom);
            query.setParameter(6, dateTo);
            
            query.setParameter(8, new Date());
            query.setParameter(9, remarks);
            
           
            
            
            

            
            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();
            }else{
                //Updating the visit
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                
                    
                
            }
            
            
            
            this.tc().close();
            
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof OSVSavable){
                OSVSavable e = (OSVSavable)o;
                return tc() == e.tc();
            }
            return false;        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }
        
    }
    
    public void modify(){
        
        
    
            if(dateFrom== null || dateTo ==null){
                //Do nothing, let the person figure it out
            
            }else{
                if(getLookup().lookup(OSVSavable.class)==null){
                            ic.add(new OSVSavable());
                }
            }
        
        
            
        
    }
}
