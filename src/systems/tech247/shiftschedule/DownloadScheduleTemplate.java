/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.shiftschedule;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import systems.tech247.hr.OrganizationUnits;

/**
 *
 * @author Wilfred
 */
public class DownloadScheduleTemplate extends javax.swing.JPanel implements LookupListener{

    /**
     * Creates new form DownloadScheduleTemplate
     */
    TopComponent tc = WindowManager.getDefault().findTopComponent("DepartmentsTopComponent");
    Lookup.Result<OrganizationUnits> result = tc.getLookup().lookupResult(OrganizationUnits.class);
    private OrganizationUnits selectedDept;
    private Date from;
    private Date to;
    
    public DownloadScheduleTemplate(){
        this(null,null,null);
    }
    
    public DownloadScheduleTemplate(OrganizationUnits unit, Date from, Date to) {
        initComponents();
        
        this.selectedDept = unit;
        this.from = from;
        this.to = to;
        
        fillTheFrom();
        
        jdcFrom.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if("date".equals(evt.getPropertyName()) && evt.getSource()==jdcFrom){
                this.from = jdcFrom.getDate();
                StatusDisplayer.getDefault().setStatusText("From Date Set");
                jdcTo.setMinSelectableDate(this.from);
            }
        });
        
        jdcTo.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if("date".equals(evt.getPropertyName()) && evt.getSource()==jdcTo){
                this.to = jdcTo.getDate();
                StatusDisplayer.getDefault().setStatusText("To Date Set");
                jdcFrom.setMaxSelectableDate(this.to);
            }
        });
        
        
        
        jtDepartment.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                DialogDisplayer.getDefault().notify(new DialogDescriptor(tc, "Select A Department"));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        jtDepartment.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DialogDisplayer.getDefault().notify(new DialogDescriptor(tc, "Select A Department"));
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
        
        result.addLookupListener(this);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result<OrganizationUnits> rslt = (Lookup.Result<OrganizationUnits>)ev.getSource();
        for(OrganizationUnits org : rslt.allInstances()){
            selectedDept = org;
            StatusDisplayer.getDefault().setStatusText(getSelectedDept().getOrganizationUnitName());
            jtDepartment.setText(selectedDept.getOrganizationUnitName());
        }
                
    }
    
    void fillTheFrom(){
        if(to != null){
            jdcTo.setDate(to);
        }
        if(from != null){
            jdcFrom.setDate(from);
        }
        
        if(selectedDept != null){
            jtDepartment.setText(selectedDept.getOrganizationUnitName());
        }
    }
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtDepartment = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jdcTo = new com.toedter.calendar.JDateChooser();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DownloadScheduleTemplate.class, "DownloadScheduleTemplate.jLabel1.text")); // NOI18N

        jtDepartment.setText(org.openide.util.NbBundle.getMessage(DownloadScheduleTemplate.class, "DownloadScheduleTemplate.jtDepartment.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DownloadScheduleTemplate.class, "DownloadScheduleTemplate.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(DownloadScheduleTemplate.class, "DownloadScheduleTemplate.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtDepartment)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(jdcTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JTextField jtDepartment;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the selectedDept
     */
    public OrganizationUnits getSelectedDept() {
        return selectedDept;
    }

    /**
     * @return the from
     */
    public Date getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public Date getTo() {
        return to;
    }
}
