package dao;

import entity.UserRole;
import connection.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDAO {
	private MyConnection myConnection;

	public UserRoleDAO() {
		myConnection = new MyConnection();
	}

	public List<UserRole> getAllRoles() {
		List<UserRole> roles = new ArrayList<>();
		String sql = "SELECT * FROM userrole";

		try (Connection conn = myConnection.connect();
				PreparedStatement pst = conn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				UserRole role = new UserRole();
				role.setRoleID(rs.getString("roleID"));
				role.setRoleName(rs.getString("roleName"));
				role.setPermissions(rs.getString("permissions"));
				roles.add(role);
			}
			return roles;

		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			myConnection.closeConnection();
		}
	}

	public UserRole getRoleByID(String roleID) {
		String sql = "SELECT * FROM userrole WHERE roleID = ?";

		try (Connection conn = myConnection.connect(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, roleID);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				UserRole role = new UserRole();
				role.setRoleID(rs.getString("roleID"));
				role.setRoleName(rs.getString("roleName"));
				role.setPermissions(rs.getString("permissions"));
				return role;
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			myConnection.closeConnection();
		}
	}

	public boolean updateRolePermissions(String roleID, String permissions) {
		String sql = "UPDATE userrole SET permissions = ? WHERE roleID = ?";

		try (Connection conn = myConnection.connect(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, permissions);
			pst.setString(2, roleID);

			// Ghi log thay đổi quyền
			boolean success = pst.executeUpdate() > 0;
			if (success) {
				ghiLogThayDoiQuyen(conn, roleID, permissions);
			}
			return success;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			myConnection.closeConnection();
		}
	}

	private void ghiLogThayDoiQuyen(Connection conn, String roleID, String permissions) throws SQLException {
		String sql = "INSERT INTO permission_change_log (roleID, change_time, new_permissions) VALUES (?, NOW(), ?)";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setString(1, roleID);
			pst.setString(2, permissions);
			pst.executeUpdate();
		}
	}

	public UserRole getCurrentUserRole(String username) {
		String sql = """
				SELECT r.*
				FROM userrole r
				JOIN user u ON u.role = r.roleID
				WHERE u.username = ? AND u.status = 'active'
				""";

		try (Connection conn = myConnection.connect(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				UserRole role = new UserRole();
				role.setRoleID(rs.getString("roleID"));
				role.setRoleName(rs.getString("roleName"));
				role.setPermissions(rs.getString("permissions")); // Tự động parse permissions string
				return role;
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			myConnection.closeConnection();
		}
	}

	public static final String ADMIN_ROLE_ID = "ROLE001";
	public static final String MANAGER_ROLE_ID = "ROLE002";
	public static final String STAFF_ROLE_ID = "ROLE003";

	// Method kiểm tra quyền dựa trên roleID
	public boolean hasPermission(String roleID, String permissionName) {
		// Nếu là Admin role, luôn có tất cả quyền
		if (ADMIN_ROLE_ID.equals(roleID)) {
			return true;
		}

		String sql = "SELECT permissions FROM userrole WHERE roleID = ?";

		try (Connection conn = myConnection.connect(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, roleID);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String permissions = rs.getString("permissions");
				if (permissions != null && !permissions.isEmpty()) {
					String[] permissionArray = permissions.split(",");
					for (String permission : permissionArray) {
						if (permission.trim().equals(permissionName)) {
							return true;
						}
					}
				}
			}
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			myConnection.closeConnection();
		}
	}

	// Method hỗ trợ kiểm tra roleID có phải admin không
	public boolean isAdminRole(String roleID) {
		return ADMIN_ROLE_ID.equals(roleID);
	}

	// Method lấy roleID từ username
	public String getRoleIDByUsername(String username) {
		String sql = "SELECT role FROM user WHERE username = ? AND status = 'active'";

		try (Connection conn = myConnection.connect(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getString("role");
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			myConnection.closeConnection();
		}
	}



	// Method kiểm tra nhiều quyền cùng lúc
	public boolean hasAllPermissions(String roleID, String... permissions) {
		// Nếu là Admin role, luôn trả về true
		if (ADMIN_ROLE_ID.equals(roleID)) {
			return true;
		}

		for (String permission : permissions) {
			if (!hasPermission(roleID, permission)) {
				return false;
			}
		}
		return true;
	}

	// Method kiểm tra có ít nhất một trong các quyền
	public boolean hasAnyPermission(String roleID, String... permissions) {
		// Nếu là Admin role, luôn trả về true
		if (ADMIN_ROLE_ID.equals(roleID)) {
			return true;
		}

		for (String permission : permissions) {
			if (hasPermission(roleID, permission)) {
				return true;
			}
		}
		return false;
	}

}