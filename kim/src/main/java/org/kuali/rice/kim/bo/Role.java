/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.kuali.core.bo.PersistableBusinessObjectBase;
import org.kuali.core.util.TypedArrayList;
import org.kuali.rice.kim.dto.GroupDTO;
import org.kuali.rice.kim.dto.PrincipalDTO;
import org.kuali.rice.kim.dto.RoleAttributeDTO;
import org.kuali.rice.kim.dto.RoleDTO;

/**
 * Roles represent an aggregation of permissions.  Authorization is given to either a principal or group by attributing a role
 * to them.
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class Role extends PersistableBusinessObjectBase {
	private static final long serialVersionUID = -8535955276605020423L;
	private Long id;
	private String name;
	private String description;
	private ArrayList<Permission> permissions;
	private ArrayList<Group> groups;
	private ArrayList<Principal> principals;
	private ArrayList<RoleAttribute> roleAttributes;
	
	//these lists are used for rendering the UI appropriately using the maintenance document framework
	//these can be considered essentially form objects
	private ArrayList<GroupQualifiedRole> groupQualifiedRoles;
	private ArrayList<PrincipalQualifiedRole> principalQualifiedRoles;
	
	//this list is what actually gets persisted for group qualifications
	private ArrayList<GroupQualifiedRoleAttribute> groupQualifiedRoleAttributes;

	/**
	 * This constructs a Role instance, primarily constructing necessary TypeArrayLists for the
	 * maintenance documents.
	 *
	 */
	public Role() {
	    this.permissions = new TypedArrayList(Permission.class);
	    this.groups = new TypedArrayList(Group.class);
	    this.principals = new TypedArrayList(Principal.class);
	    this.roleAttributes = new TypedArrayList(RoleAttribute.class);
	    this.groupQualifiedRoles = new TypedArrayList(GroupQualifiedRole.class);
	    this.principalQualifiedRoles = new TypedArrayList(PrincipalQualifiedRole.class);
	    this.groupQualifiedRoleAttributes = new TypedArrayList(GroupQualifiedRoleAttribute.class);
	}

	/**
	 * This method retrieves the description.
	 *
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method set the description.
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This method retrieves the id for the role.
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method sets the id (PK) of the role instance.
	 *
	 * @param id Long
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method retrieves the name.
	 *
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method sets the name.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * @return the groups
     */
    public ArrayList<Group> getGroups() {
        return this.groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the principals
     */
    public ArrayList<Principal> getPrincipals() {
        return this.principals;
    }

    /**
     * @param principals the principals to set
     */
    public void setPrincipals(ArrayList<Principal> principals) {
        this.principals = principals;
    }

    /**
     * @return the permissions
     */
    public ArrayList<Permission> getPermissions() {
        return this.permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the roleAttributes
     */
    public ArrayList<RoleAttribute> getRoleAttributes() {
        return this.roleAttributes;
    }

    /**
     * @param roleAttributes the roleAttributes to set
     */
    public void setRoleAttributes(ArrayList<RoleAttribute> roleAttributes) {
        this.roleAttributes = roleAttributes;
    }
    
    /**
     * @return the groupQualifiedRoles
     */
    public ArrayList<GroupQualifiedRole> getGroupQualifiedRoles() {
        return this.groupQualifiedRoles;
    }

    /**
     * @param groupQualifiedRoles the groupQualifiedRoles to set
     */
    public void setGroupQualifiedRoles(ArrayList<GroupQualifiedRole> groupQualifiedRoles) {
        this.groupQualifiedRoles = groupQualifiedRoles;
    }

    /**
     * @return the principalQualifiedRoles
     */
    public ArrayList<PrincipalQualifiedRole> getPrincipalQualifiedRoles() {
        return this.principalQualifiedRoles;
    }

    /**
     * @param principalQualifiedRoles the principalQualifiedRoles to set
     */
    public void setPrincipalQualifiedRoles(ArrayList<PrincipalQualifiedRole> principalQualifiedRoles) {
        this.principalQualifiedRoles = principalQualifiedRoles;
    }
    
    /**
     * @return the groupQualifiedRoleAttributes
     */
    public ArrayList<GroupQualifiedRoleAttribute> getGroupQualifiedRoleAttributes() {
        return this.groupQualifiedRoleAttributes;
    }

    /**
     * @param groupQualifiedRoleAttributes the groupQualifiedRoleAttributes to set
     */
    public void setGroupQualifiedRoleAttributes(ArrayList<GroupQualifiedRoleAttribute> groupQualifiedRoleAttributes) {
        this.groupQualifiedRoleAttributes = groupQualifiedRoleAttributes;
    }

    /**
	 * This overridden method retrieves a string representation of an instance of a Role.
	 *
	 * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
	 */
	protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> propMap = new LinkedHashMap<String, Object>();
        propMap.put("id", getId());
        propMap.put("name", getName());
        propMap.put("description", getDescription());
        return propMap;
	}

	/**
	 *
	 * This method returns a DTO for the BO
	 *
	 * @param role
	 * @return RoleDTO
	 */
	public static RoleDTO toDTO(final Role role) {
	    final RoleDTO dto = new RoleDTO();
	    dto.setDescription(role.getDescription());
	    dto.setId(role.getId());
	    dto.setName(role.getName());

	    final HashMap<String, GroupDTO> permissions = new HashMap<String, GroupDTO>();
//	    for (Group group : role.getPermissions()) {
//	        permissions.put(group.getName(), Group.toDTO(group));
//	    }
	    dto.setPermissions(permissions);

	    final HashMap<String, GroupDTO> groups = new HashMap<String, GroupDTO>();
	    for (Group group : role.getGroups()) {
	        groups.put(group.getName(), Group.toDTO(group));
	    }
	    dto.setGroups(groups);

	    final HashMap<String, PrincipalDTO> principals = new HashMap<String, PrincipalDTO>();
        for (Principal principal : role.getPrincipals()) {
            principals.put(principal.getName(), Principal.toDTO(principal));
        }
        dto.setPrincipals(principals);

	    final HashMap<String, RoleAttributeDTO> attrs = new HashMap<String, RoleAttributeDTO>();
	    for (RoleAttribute attr : role.getRoleAttributes()) {
	        attrs.put(attr.getAttributeName(), RoleAttribute.toDTO(attr));
	    }
	    dto.setRoles(attrs);

        return dto;
	}
}
