package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Department findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
