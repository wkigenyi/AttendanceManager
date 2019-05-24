/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.BorderLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import systems.tech247.util.CapCreatable;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//systems.tech247.clockinutil//Shifts//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ShiftsTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "systems.tech247.clockinutil.ShiftsTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_ShiftsAction",
//        preferredID = "ShiftsTopComponent"
//)
@Messages({
    "CTL_ShiftsAction=Shifts",
    "CTL_ShiftsTopComponent=Shifts Window",
    "HINT_ShiftsTopComponent=This is a Shifts window"
})
public final class ShiftsTopComponent extends TopComponent implements ExplorerManager.Provider {
    ExplorerManager em = new ExplorerManager();
    InstanceContent content = new InstanceContent();
    Lookup lkp = new AbstractLookup(content);
    
    public ShiftsTopComponent(){
        this("");
    }
    
    public ShiftsTopComponent(String view) {
        initComponents();
        setName(Bundle.CTL_ShiftsTopComponent());
        setToolTipText(Bundle.HINT_ShiftsTopComponent());
        content.add((CapCreatable) () -> {
            TopComponent tc = new ShiftEditorTopComponent();
            tc.open();
            tc.requestActive();
        });
        setLayout(new BorderLayout());
        OutlineView ov = new OutlineView("Shifts");
        
        add(ov);
        
        if(!view.equals("")){
            ov.addPropertyColumn("code", "CODE");
            ov.addPropertyColumn("start", "Starts");
            ov.addPropertyColumn("late", "Late Limit");
            ov.addPropertyColumn("end", "Ends");
            ov.addPropertyColumn("default", "DEFAULT SHIFT");
            ov.addPropertyColumn("night", "NIGHT EXISTS");
        }
        
        em.setRootContext(new AbstractNode(Children.create(new FactoryShifts(), true)));
        
        ov.getOutline().setRootVisible(false);
        
        associateLookup(new ProxyLookup(ExplorerUtils.createLookup(em, getActionMap()),lkp));
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
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    
}
