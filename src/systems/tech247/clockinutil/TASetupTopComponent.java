/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.BorderLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.StatusDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.clockinutil//TASetup//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "TASetupTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false/*, roles = {"ta"}*/)
@ActionID(category = "Window", id = "systems.tech247.clockinutil.TASetupTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TASetupAction",
        preferredID = "TASetupTopComponent"
)
@Messages({
    "CTL_TASetupAction=TASetup",
    "CTL_TASetupTopComponent=T & A Setup",
    "HINT_TASetupTopComponent=This is a TASetup window"
})
public final class TASetupTopComponent extends TopComponent implements ExplorerManager.Provider {

    ExplorerManager em = UtilZKClockin.emTAsetup;
    public TASetupTopComponent() {
        initComponents();
        setName(Bundle.CTL_TASetupTopComponent());
        setToolTipText(Bundle.HINT_TASetupTopComponent());
        BeanTreeView btv = new BeanTreeView();
        setLayout(new BorderLayout());
        btv.setRootVisible(false);
        add(btv);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        StatusDisplayer.getDefault().setStatusText("Opened");
    }

    @Override
    public void componentClosed() {
        
    }

    @Override
    protected void componentActivated() {
        super.componentActivated(); 
        UtilZKClockin.loadTASetup();
//To change body of generated methods, choose Tools | Templates.
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
}