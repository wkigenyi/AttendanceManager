/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.StatusDisplayer;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.PtmShifts;
import systems.tech247.util.SpringUtilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.clockinutil//ShiftEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ShiftEditorTopComponent",
        iconBase="systems/tech247/util/icons/settings.png", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)



@Messages({
    
    "CTL_ShiftEditorTopComponent=New Shift",
    "HINT_ShiftEditorTopComponent=Shift Editor"
})
public final class ShiftEditorTopComponent extends TopComponent implements ChangeListener{
    
    PtmShifts emp;
    
    DataAccess da = new DataAccess();
    InstanceContent ic = new InstanceContent();
    
    
    //Updatables
    String pname = "";
    String code = "";
    Calendar date = new GregorianCalendar();
    
    
    Date startTime;
    Date lateTime;
    Date endTime;
    
    
    String defa = "";
    String casual ="";
    JFormattedTextField ftf = null;
    
    JSpinner jspStarter=null;
    JSpinner jspLate = null;
    JSpinner jspEnd = null;
    SpinnerDateModel sModel;
    SpinnerDateModel lModel;
    SpinnerDateModel eModel;
    
    
    
    String[] labels = {"Shift Name:","Shift Code:","Start Time:","Late Begins:","Shift Ends:"};
    int numPairs = labels.length;
    EntityManager entityManager = DataAccess.getEntityManager();
    
    PtmShifts updatable;
    
    public ShiftEditorTopComponent(){
        this(null);
    }

    public ShiftEditorTopComponent(PtmShifts e) {
        initComponents();
        setName(Bundle.CTL_ShiftEditorTopComponent());
        setToolTipText(Bundle.HINT_ShiftEditorTopComponent());
        viewPanel.setLayout(new SpringLayout());
        //Set the default date
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        startTime = date.getTime();
        lateTime = date.getTime();
        endTime = date.getTime();
        
        //jspStart.setEditor(new JSpinner.DateEditor(jspStart, "HH:mm"));
        //jspLate.setEditor(new JSpinner.DateEditor(jspLate, "HH:mm"));
        //jspEnd.setEditor(new JSpinner.DateEditor(jspEnd, "HH:mm"));
        
        emp = e;
        
        if(null!=e){
            updatable = entityManager.find(PtmShifts.class, e.getShiftID());
        }
        
        
        //Fill in the employee details
        
            fillShift(e);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
       
                
        
        
        

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource()==jspStarter){
            SpinnerDateModel model = (SpinnerDateModel)jspStarter.getModel();
            startTime = model.getDate();
            try{
                updatable.setStartTime(startTime);
            }catch(Exception ex){
                
            }
            modify();
        }
        if(e.getSource()==jspLate){
            SpinnerDateModel model = (SpinnerDateModel)jspLate.getModel();
            lateTime = model.getDate();
            try{
                updatable.setLateLimit(lateTime);
            }catch(Exception ex){
                
            }
            modify();
        }
        if(e.getSource()==jspEnd){
            SpinnerDateModel model = (SpinnerDateModel)jspEnd.getModel();
            endTime = model.getDate();
            try{
                updatable.setEndTime(endTime);
            }catch(Exception ex){
                
            }
            modify();
        }
    }


    
    private class ShiftSavable extends AbstractSavable{
        
        ShiftSavable(){
            register();
        }

        @Override
        protected String findDisplayName() {
            if(null==emp){
                return "New Shift";
            }else{
                return emp.getShiftName();
            }
        }
        
        ShiftEditorTopComponent tc(){
            return ShiftEditorTopComponent.this;
        }

        @Override
        protected void handleSave() throws IOException {
            
            tc().ic.remove(this);
            unregister();
            //New Employee
            if(null==emp){
                
                        String insertSQL = "INSERT INTO [dbo].[PtmShifts]\n" +
"           ([ShiftName]\n" +
"           ,[ShiftCode]\n" +
"           ,[StartTime]\n" +
"           ,[EndTime]\n" +
"           ,[CalculationMethod]\n" +
"           ,[RoundOffCriteria]\n" +
"           ,[ApplicationValidity]\n" + 
"           ,[CategoryID]\n" +                                
"           ,[LateLimit])\n" +                                
"     VALUES\n" +
"           (?,?,?,?,1,2,5,1,?)";
            Query query = entityManager.createNativeQuery(insertSQL);
            query.setParameter(1, pname);
            query.setParameter(2, code);
            
            query.setParameter(3, startTime);
            query.setParameter(4, endTime);
            query.setParameter(5, lateTime);
            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();
            }else{
                //Creating a new Employee
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
        
        
    
            if(pname.length()<1){
                StatusDisplayer.getDefault().setStatusText("Proper Shift Name is Required");
            }else if(code.length()<1){
                StatusDisplayer.getDefault().setStatusText("Proper Code is Required");
            }else{
                if(getLookup().lookup(ShiftSavable.class)==null){
                            ic.add(new ShiftSavable());
                }
            }
        
        
            
        
    }
    
    void fillShift(PtmShifts e){
        
        
                try{
                setName("Shift > " + e.getShiftName());
                }catch(NullPointerException ex){
                    // We already have a name..
                }
                
                try{
                
                startTime = e.getStartTime();
                lateTime = e.getLateLimit();
                endTime = e.getEndTime();
                pname = e.getShiftName();
                code = e.getShiftCode();
                }catch(NullPointerException ex){
                    //Use the default values
                }
                
                    
                    sModel = new SpinnerDateModel(startTime, null, null, Calendar.MINUTE);
                    
           
                    JTextField jftShiftName = addLabelTextField(viewPanel, labels[0]);
                    jftShiftName.setText(pname);
                    jftShiftName.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            pname = jftShiftName.getText();
                            try{
                                updatable.setShiftName(pname);
                                
                            }catch(Exception ex){
                                
                            }
                            modify();
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            pname = jftShiftName.getText();
                            try{
                                updatable.setShiftName(pname);
                                
                            }catch(Exception ex){
                                
                            }
                            modify(); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            pname = jftShiftName.getText();
                            try{
                                updatable.setShiftName(pname);
                                
                            }catch(Exception ex){
                                
                            }
                            modify(); //To change body of generated methods, choose Tools | Templates.
                        }
                    });
                    
                    
                    JTextField jftShiftCode = addLabelTextField(viewPanel, labels[1]);
                    jftShiftCode.setText(code);
                    jftShiftCode.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            code = jftShiftCode.getText();
                            try{
                                updatable.setShiftCode(code);
                                
                            }catch(Exception ex){
                                
                            }
                            modify();
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            code = jftShiftCode.getText();
                            try{
                                updatable.setShiftCode(code);
                                
                            }catch(Exception ex){
                                
                            }
                            modify(); //To change body of generated methods, choose Tools | Templates.
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            code = jftShiftCode.getText();
                            try{
                                updatable.setShiftCode(code);
                                
                            }catch(Exception ex){
                                
                            }
                            modify(); //To change body of generated methods, choose Tools | Templates.
                        }
                    });
        
                    
                    
                    
                    jspStarter = addLabelSpinner(viewPanel, labels[2], sModel);
                    jspStarter.setEditor(new JSpinner.DateEditor(jspStarter, "HH:mm"));
                    jspStarter.addChangeListener(this);
                    //jspStart.setValue(e.getStartTime());
                    //DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Start Date Model Set"));
                    
                    
                    ftf = getTextField(jspStarter);
        if (ftf != null ) {
            ftf.setColumns(8); //specify more width than we need
            ftf.setHorizontalAlignment(JTextField.RIGHT);
        }
                    
                    lModel = new SpinnerDateModel(lateTime, null, null, Calendar.MINUTE);
                    
                    
                    //jspLate.setModel(lModel);
                    //DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Late Date Model Set"));
                    jspLate = addLabelSpinner(viewPanel, labels[3], lModel);
                    jspLate.setEditor(new JSpinner.DateEditor(jspLate, "HH:mm"));
                    jspLate.addChangeListener(this);
                    
                    ftf = getTextField(jspLate);
        if (ftf != null ) {
            ftf.setColumns(8); //specify more width than we need
            ftf.setHorizontalAlignment(JTextField.RIGHT);
        }
                    
                    
                    
                    eModel = new SpinnerDateModel(endTime, null, null, Calendar.MINUTE);
                    jspEnd = addLabelSpinner(viewPanel, labels[4], eModel);
                    jspEnd.setEditor(new JSpinner.DateEditor(jspEnd, "HH:mm"));
                    
                    jspEnd.addChangeListener(this);
                    
        ftf = getTextField(jspEnd);
        if (ftf != null ) {
            ftf.setColumns(8); //specify more width than we need
            ftf.setMaximumSize(new Dimension(8, 2));
            ftf.setHorizontalAlignment(JTextField.RIGHT);
        }            
                    
                    
                    SpringUtilities.makeCompactGrid(viewPanel, 
                            numPairs, 2,//rows and columns 
                            10, 10,//initX,initY
                            6, 10 //xPad,yPad
                    );
                    
                    //jspEnd.setModel(eModel);
                    //DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("End Date Model Set"));
                    
                    
                
                
                
              
                
                
                
                
                
                
                
                
                
                
        
                
    }
    
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                               + spinner.getEditor().getClass()
                               + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 187, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(viewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 320, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(viewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 93, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel viewPanel;
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
    
    static protected JSpinner addLabelSpinner(Container c,String label,SpinnerModel model){
        JLabel l = new JLabel(label);
        c.add(l);
        JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        
        return spinner;
        
        
    }
    
    static protected JTextField addLabelTextField(Container c,String label){
        JLabel l = new JLabel(label);
        c.add(l);
        JTextField spinner = new JTextField();
        l.setLabelFor(spinner);
        c.add(spinner);
        
        return spinner;
        
        
    }

    
}
