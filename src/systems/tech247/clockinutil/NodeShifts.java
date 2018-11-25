/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import systems.tech247.hr.PtmShifts;


/**
 *
 * @author Admin
 */
public class NodeShifts extends BeanNode<PtmShifts>{
    
    PtmShifts shift;
    
    public NodeShifts(PtmShifts bean) throws IntrospectionException{
        super(bean,Children.LEAF,Lookups.singleton(bean));
        
        this.shift = bean;
        setDisplayName(bean.getShiftName());
        setIconBaseWithExtension("systems/tech247/endeavoricons/settings.png");
    }

    @Override
    public Action getPreferredAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = new ShiftEditorTopComponent(shift);
                tc.open();
                tc.requestActive();
            }
        };
    }
    
    
}
