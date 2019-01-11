/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.shiftschedule;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;



import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmShiftSchedule;
import systems.tech247.hr.PtmShifts;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.shiftschedule//ShiftScheduleEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ShiftScheduleEditorTopComponent",
        iconBase="systems/tech247/util/icons/settings.png", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)



@Messages({
    
    "CTL_ShiftScheduleEditor=Shift Schedule Editor",
    "HINT_ShiftScheduleEditor=Shift Schedule Editor"
})
public final class ShiftScheduleEditorTopComponent extends TopComponent implements LookupListener{
    
    PtmShiftSchedule emp;
    
    DataAccess da = new DataAccess();
    InstanceContent ic = new InstanceContent();
    TopComponent tc = WindowManager.getDefault().findTopComponent("ShiftsTopComponent");
    Lookup.Result<PtmShifts> rslt = tc.getLookup().lookupResult(PtmShifts.class);
    
    

    EntityManager entityManager = DataAccess.getEntityManager();
    
    PtmShiftSchedule updatable;
    
    public ShiftScheduleEditorTopComponent(){
        this(null);
    }

    public ShiftScheduleEditorTopComponent(PtmShiftSchedule e) {
        initComponents();
        setName(Bundle.CTL_ShiftScheduleEditor());
        setToolTipText(Bundle.HINT_ShiftScheduleEditor());
        

        

        
        emp = e;
        
        if(null!=e){
            updatable = entityManager.find(PtmShiftSchedule.class, e.getShiftScheduleId());
        }
 
        //Fill in the employee details
        
            fillShift(e);
            
            jtShift.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DialogDisplayer.getDefault().notify(new DialogDescriptor(tc, "Select A Shift"));
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




    
    private class ShiftSavable extends AbstractSavable{
        
        ShiftSavable(){
            register();
        }

        @Override
        protected String findDisplayName() {
            return "Shift Schedule";
        }
        
        ShiftScheduleEditorTopComponent tc(){
            return ShiftScheduleEditorTopComponent.this;
        }

        @Override
        protected void handleSave() throws IOException {
            
            tc().ic.remove(this);
            unregister();
            //New Employee
            if(null==emp){
                //We shall only deal with existing schedules for now
//                
//                        String insertSQL = "INSERT INTO [dbo].[PtmShifts]\n" +
//"           ([ShiftName]\n" +
//"           ,[ShiftCode]\n" +
//"           ,[StartTime]\n" +
//"           ,[EndTime]\n" +
//"           ,[CalculationMethod]\n" +
//"           ,[RoundOffCriteria]\n" +
//"           ,[ApplicationValidity]\n" + 
//"           ,[CategoryID]\n" +                                
//"           ,[LateLimit])\n" +                                
//"     VALUES\n" +
//"           (?,?,?,?,1,2,5,1,?)";
//            Query query = entityManager.createNativeQuery(insertSQL);
//            query.setParameter(1, pname);
//            query.setParameter(2, code);
//            
//            query.setParameter(3, startTime);
//            query.setParameter(4, endTime);
//            query.setParameter(5, lateTime);
//            entityManager.getTransaction().begin();
//            query.executeUpdate();
//            entityManager.getTransaction().commit();
            }else{
                //Updates Only
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                
                    
                
            }
            
            //Reload to show the changes / new data
            //UtilZKClockin.loadTASetup();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof ShiftSavable){
                ShiftSavable e = (ShiftSavable)o;
                return tc() == e.tc();
            }
            return false;        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }
        
    }
    
    public void modify(){
                if(getLookup().lookup(ShiftSavable.class)==null){
                            ic.add(new ShiftSavable());
                }
    }
    
    void fillShift(PtmShiftSchedule e){
        jtShift.setText(DataAccess.getShiftByID(e.getShiftCode()).getShiftName());
        jdcShiftDate.setDate(e.getShiftDate());
        jcbComp.setSelected(e.getIsCOff());
        jcbWeekly.setSelected(e.getIsWeekOff());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdcShiftDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtShift = new javax.swing.JTextField();
        jcbWeekly = new javax.swing.JCheckBox();
        jcbComp = new javax.swing.JCheckBox();

        jdcShiftDate.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ShiftScheduleEditorTopComponent.class, "ShiftScheduleEditorTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ShiftScheduleEditorTopComponent.class, "ShiftScheduleEditorTopComponent.jLabel2.text")); // NOI18N

        jtShift.setText(org.openide.util.NbBundle.getMessage(ShiftScheduleEditorTopComponent.class, "ShiftScheduleEditorTopComponent.jtShift.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jcbWeekly, org.openide.util.NbBundle.getMessage(ShiftScheduleEditorTopComponent.class, "ShiftScheduleEditorTopComponent.jcbWeekly.text")); // NOI18N
        jcbWeekly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbWeeklyActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jcbComp, org.openide.util.NbBundle.getMessage(ShiftScheduleEditorTopComponent.class, "ShiftScheduleEditorTopComponent.jcbComp.text")); // NOI18N
        jcbComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCompActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcShiftDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtShift, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jcbComp))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jcbWeekly)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jdcShiftDate, jtShift});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jdcShiftDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtShift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbWeekly)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbComp)
                .addContainerGap(164, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jcbWeeklyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbWeeklyActionPerformed
        updatable.setIsWeekOff(jcbWeekly.isSelected());
        modify();
    }//GEN-LAST:event_jcbWeeklyActionPerformed

    private void jcbCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCompActionPerformed
        updatable.setIsCOff(jcbComp.isSelected());
        modify();
    }//GEN-LAST:event_jcbCompActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox jcbComp;
    private javax.swing.JCheckBox jcbWeekly;
    private com.toedter.calendar.JDateChooser jdcShiftDate;
    private javax.swing.JTextField jtShift;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        rslt.addLookupListener(this);
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

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result<PtmShifts> rslt = (Lookup.Result<PtmShifts>)ev.getSource();
        for(PtmShifts p: rslt.allInstances()){
            updatable.setShiftCode(p.getShiftID());
            modify();
        }
            
    }
    
    

    
}
