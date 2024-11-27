package dao;

import entity.Permission;
import connection.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {
    private MyConnection myConnection;

    public PermissionDAO() {
        myConnection = new MyConnection();
    }

    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT * FROM permission";

        try (Connection conn = myConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Permission permission = new Permission();
                permission.setPermissionID(rs.getString("permissionID"));
                permission.setName(rs.getString("name"));
                permission.setDescription(rs.getString("description"));
                permissions.add(permission);
            }
            return permissions;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            myConnection.closeConnection();
        }
    }

    public boolean addPermission(Permission permission) {
        String sql = "INSERT INTO permission (permissionID, name, description) VALUES (?, ?, ?)";

        try (Connection conn = myConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, permission.getPermissionID());
            pst.setString(2, permission.getName());
            pst.setString(3, permission.getDescription());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            myConnection.closeConnection();
        }
    }

    public boolean updatePermission(Permission permission) {
        String sql = "UPDATE permission SET name=?, description=? WHERE permissionID=?";

        try (Connection conn = myConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, permission.getName());
            pst.setString(2, permission.getDescription());
            pst.setString(3, permission.getPermissionID());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            myConnection.closeConnection();
        }
    }

	public boolean deletePermission(String id) {
		// TODO Auto-generated method stub
		return false;
	}
	 public Permission getPermissionById(String id) {
	        String sql = "SELECT * FROM permission WHERE permissionID = ?";

	        try (Connection conn = myConnection.connect();
	             PreparedStatement pst = conn.prepareStatement(sql)) {

	            pst.setString(1, id);
	            ResultSet rs = pst.executeQuery();

	            if (rs.next()) {
	                Permission permission = new Permission();
	                permission.setPermissionID(rs.getString("permissionID"));
	                permission.setName(rs.getString("name"));
	                permission.setDescription(rs.getString("description"));
	                return permission;
	            }
	            return null;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        } finally {
	            myConnection.closeConnection();
	        }
	    }

}
