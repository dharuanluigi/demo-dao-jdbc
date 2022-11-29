package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection connection;

	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " + "VALUES " + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());

			var rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				var resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					seller.setId(resultSet.getInt(1));
				}
				DB.closeResultSet(resultSet);

			} else {
				throw new DbException("Unexpected error! No rows was affected when try insert new seller");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
		}

	}

	@Override
	public void update(Seller seller) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			statement.setInt(6, seller.getId());

			var affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new DbException("Unexpected error! No rows was updated!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);

		}

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				var dep = instantiateDepartment(resultSet);
				return instantiateSeller(resultSet, dep);
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");
			resultSet = statement.executeQuery();
			var sellers = new ArrayList<Seller>();
			var departments = new HashMap<Integer, Department>();

			while (resultSet.next()) {
				var dep = departments.get(resultSet.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(resultSet);
					departments.put(resultSet.getInt("DepartmentId"), dep);
				}

				sellers.add(instantiateSeller(resultSet, dep));
			}

			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			statement.setInt(1, department.getId());
			resultSet = statement.executeQuery();

			var sellers = new ArrayList<Seller>();
			var departments = new HashMap<Integer, Department>();

			while (resultSet.next()) {
				var dep = departments.get(resultSet.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(resultSet);
					departments.put(resultSet.getInt("DepartmentId"), dep);
				}

				var seller = instantiateSeller(resultSet, dep);
				sellers.add(seller);
			}

			return sellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(statement);
			DB.closeResultSet(resultSet);
		}
	}

	private Seller instantiateSeller(ResultSet resultSet, Department dep) throws SQLException {
		return new Seller(resultSet.getInt("Id"), resultSet.getString("Name"), resultSet.getString("Email"),
				resultSet.getDate("BirthDate"), resultSet.getDouble("BaseSalary"), dep);
	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
	}
}
