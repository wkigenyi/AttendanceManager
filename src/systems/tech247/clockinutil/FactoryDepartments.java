/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.clockinutil;

import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.util.AddTool;
import systems.tech247.util.NodeAddTool;



/**
 *
 * @author Wilfred
 */
public class FactoryDepartments extends ChildFactory<Object>{

    
    
    String sql;
    boolean add;
    public FactoryDepartments(String sql){
        this(sql, false);
    }
    
    public FactoryDepartments(String sql, boolean add){
        this.add = add;
        this.sql = sql;
    }
    
    @Override
    protected boolean createKeys(List<Object> list) {
        
        //Populate the list of child entries
        list.addAll(DataAccess.searchDepartments(sql));
        
        //return true since we are set
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Object key){
        Node node = null;
        if(key instanceof OrganizationUnits){
            node = new DepartmentNode((OrganizationUnits)key);
        }else if(key instanceof AddTool){
            node = new NodeAddTool((AddTool)key);
        }
        
        return node;
    }
    
    private class DepartmentNode extends AbstractNode{
        
        private final InstanceContent instanceContent;
        OrganizationUnits unit;
        
        public DepartmentNode(OrganizationUnits emp){
            this(new InstanceContent(),emp);
        }
        
        private DepartmentNode (InstanceContent ic, OrganizationUnits unit){
            super(Children.create(new FactoryDepartmentEmployees(unit), true), new AbstractLookup(ic));
            instanceContent = ic;
            instanceContent.add(unit);
            this.unit = unit;
            setIconBaseWithExtension("systems/tech247/util/icons/department.png");
            setDisplayName(unit.getOrganizationUnitName());
        }

        
        

        
        
        
    
}
    
}
