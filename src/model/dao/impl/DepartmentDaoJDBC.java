package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("INSERT INTO department (Name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, department.getName());

			var rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				var resultSet = statement.getGeneratedKeys();
				while (resultSet.next()) {
					department.setId(resultSet.getInt(1));
				}
			} else {
				throw new DbException("Error to insert a department");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
		}

	}

	@Override
	public void update(Department department) {
		PreparedStatement state = null;

		try {
			state = connection.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");

			state.setString(1, department.getName());
			state.setInt(2, department.getId());

			var affectedRows = state.executeUpdate();

			if (affectedRows == 0) {
				throw new DbException("No department updated. No department was found with id: " + department.getId());
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(state);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement state = null;

		try {
			state = connection.prepareStatement("DELETE FROM department WHERE Id = ?");

			state.setInt(1, id);

			var affectedRows = state.executeUpdate();

			if (affectedRows == 0) {
				throw new DbException("No one department was deleted. Department with id " + id + " was not found");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(state);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement state = null;
		ResultSet result = null;

		try {
			state = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");

			state.setInt(1, id);

			result = state.executeQuery();

			if (result.next()) {
				return new Department(result.getInt("Id"), result.getString("Name"));
			} else {
				throw new DbException("No results found in deparment with id: " + id);
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(state);
			DB.closeResultSet(result);
		}

	}

	@Override
	public List<Department> findAll() {
		PreparedStatement state = null;
		ResultSet result = null;

		try {
			state = connection.prepareStatement("SELECT * FROM department");

			result = state.executeQuery();

			var departments = new ArrayList<Department>();

			while (result.next()) {
				departments.add(new Department(result.getInt("Id"), result.getString("Name")));
			}

			return departments;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(state);
			DB.closeResultSet(result);
		}
	}

}
