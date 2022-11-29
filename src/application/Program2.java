package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		var input = new Scanner(System.in);
		var departmentDao = DaoFactory.createDepartmentDao();

		System.out.println("=== Test 1: department insert ===");
		var newDep = new Department("Music");
		departmentDao.insert(newDep);
		System.out.println("New department added id is: " + newDep.getId());

		input.close();
	}
}
