// UserRole.java
package entity;

import java.util.*;

public class UserRole {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(roleID, userRole.roleID);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roleID);
    }
    
    @Override
    public String toString() {
        return "UserRole{" +
               "roleID='" + roleID + '\'' +
               ", roleName='" + roleName + '\'' +
               ", permissions='" + permissions + '\'' +
               '}';
    }
    
    // Builder pattern for convenient object creation
    public static class Builder {
        private final UserRole userRole;
        
        public Builder() {
            userRole = new UserRole();
        }
        
        public Builder roleID(String roleID) {
            userRole.setRoleID(roleID);
            return this;
        }
        
        public Builder roleName(String roleName) {
            userRole.setRoleName(roleName);
            return this;
        }
        
        public Builder permissions(String permissions) {
            userRole.setPermissions(permissions);
            return this;
        }
        
        public Builder addPermission(String permission) {
            userRole.addPermission(permission);
            return this;
        }
        
        public UserRole build() {
            return userRole;
        }
    }
    
    // Factory methods for common role types
    public static UserRole createAdminRole() {
        return new Builder()
            .roleID("ADMIN_ROLE")
            .roleName("ADMIN")
            .permissions("VIEW_STAFF,ADD_STAFF,EDIT_STAFF,DELETE_STAFF," +
                       "VIEW_PERMISSIONS,MANAGE_PERMISSIONS," +
                       "VIEW_ACCOUNTS,MANAGE_ACCOUNTS," +
                       "VIEW_REPORTS,MANAGE_SYSTEM")
            .build();
    }
    
    public static UserRole createManagerRole() {
        return new Builder()
            .roleID("MANAGER_ROLE")
            .roleName("MANAGER")
            .permissions("VIEW_STAFF,ADD_STAFF,EDIT_STAFF," +
                       "VIEW_PERMISSIONS,VIEW_ACCOUNTS,VIEW_REPORTS")
            .build();
    }
    
    public static UserRole createStaffRole() {
        return new Builder()
            .roleID("STAFF_ROLE")
            .roleName("STAFF")
            .permissions("VIEW_STAFF,VIEW_ACCOUNTS")
            .build();
    }
    
    // Clone method for creating copies
    public UserRole clone() {
        return new UserRole(this.roleID, this.roleName, this.permissions);
    }
}