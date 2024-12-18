// UserRole.java
package entity;

import java.util.*;

public class UserRole {
	public static final String[] ADMIN_PERMISSIONS = {
			// Quyền quản lý sản phẩm
			"VIEW_PRODUCTS", "ADD_PRODUCT", "EDIT_PRODUCT", "DELETE_PRODUCT", "EXPORT_PRODUCT",

			// Quyền quản lý danh mục
			"VIEW_CATEGORIES", "ADD_CATEGORY", "EDIT_CATEGORY", "DELETE_CATEGORY",

			// Quyền quản lý nhà cung cấp
			"VIEW_SUPPLIERS", "ADD_SUPPLIER", "EDIT_SUPPLIER", "DELETE_SUPPLIER",

			// Quyền quản lý nhân viên
			"VIEW_STAFF", "ADD_STAFF", "EDIT_STAFF", "DELETE_STAFF",

			// Quyền quản lý nhập hàng
			"VIEW_IMPORTS", "CREATE_IMPORT", "EDIT_IMPORT", "DELETE_IMPORT", "EXPORT_IMPORT",

			// Quyền quản lý đơn hàng
			"VIEW_ORDERS", "CREATE_ORDER", "EDIT_ORDER", "DELETE_ORDER", "EXPORT_ORDER",

			// Quyền hệ thống
			"VIEW_REPORTS", "MANAGE_SYSTEM", "VIEW_STATISTICS" };
	private String roleID;
	private String roleName;
	private String permissions; // Stored as comma-separated string in DB
	private Set<String> permissionSet; // For easier permission management in memory

	public UserRole() {
		this.permissionSet = new HashSet<>();
	}

	 public UserRole(String roleID, String roleName, String permissions) {
	        this.roleID = roleID;
	        this.roleName = roleName;
	        this.permissions = permissions;
	        this.permissionSet = new HashSet<>();
	        if (permissions != null && !permissions.isEmpty()) {
	            Collections.addAll(this.permissionSet, permissions.split(","));
	        }
	    }
	// Basic getters and setters
	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
		this.permissionSet.clear();
		if (permissions != null && !permissions.isEmpty()) {
			Collections.addAll(this.permissionSet, permissions.split(","));
		}
	}

	// Permission management methods
	public Set<String> getPermissionSet() {
		return new HashSet<>(permissionSet);
	}

	public void setPermissionSet(Set<String> permissions) {
		this.permissionSet = new HashSet<>(permissions);
		this.permissions = String.join(",", this.permissionSet);
	}

	public void addPermission(String permission) {
		if (permission != null && !permission.isEmpty()) {
			this.permissionSet.add(permission);
			updatePermissionsString();
		}
	}

	public void removePermission(String permission) {
		if (this.permissionSet.remove(permission)) {
			updatePermissionsString();
		}
	}

	public boolean hasPermission(String permission) {
		return this.permissionSet.contains(permission);
	}

	public void clearPermissions() {
		this.permissionSet.clear();
		this.permissions = "";
	}

	private void updatePermissionsString() {
		this.permissions = String.join(",", this.permissionSet);
	}

	// Utility methods for role management
	public boolean isAdminRole() {
		return "ADMIN".equalsIgnoreCase(this.roleName);
	}

	public boolean isManagerRole() {
		return "MANAGER".equalsIgnoreCase(this.roleName);
	}

	public boolean isStaffRole() {
		return "STAFF".equalsIgnoreCase(this.roleName);
	}

	// Override methods for proper object comparison and display
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserRole userRole = (UserRole) o;
		return Objects.equals(roleID, userRole.roleID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleID);
	}

	@Override
	public String toString() {
		return "UserRole{" + "roleID='" + roleID + '\'' + ", roleName='" + roleName + '\'' + ", permissions='"
				+ permissions + '\'' + '}';
	}


}