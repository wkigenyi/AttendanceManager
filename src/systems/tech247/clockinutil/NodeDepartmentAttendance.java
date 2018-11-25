/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import systems.tech247.hr.OrganizationUnits;

/**
 *
 * @author Admin
 */
public class NodeDepartmentAttendance extends AbstractNode {
    OrganizationUnits department;
    public NodeDepartmentAttendance(OrganizationUnits unit){
        
        super(Children.LEAF);
        this.department = unit;
        setIconBaseWithExtension("systems/tech247/util/icons/department.png");
        setDisplayName(department.getOrganizationUnitName());
    }
}
