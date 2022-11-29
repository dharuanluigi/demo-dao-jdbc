package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		var input = new Scanner(System.in);
		var departmentDao = DaoFactory.createDepartmentDao();

		System.out.println("=== Test 1: department insert ===");
		var dep = new Department("Music");
		departmentDao.insert(dep);
		System.out.println("New department added id is: " + dep.getId());

		System.out.println("=== Test 2: department findById ===");
		dep = departmentDao.findById(2);
		System.out.println(dep);

		System.out.println("=== Test 3: department update ===");
		dep = departmentDao.findById(6);
		dep.setName("TI");
		departmentDao.update(dep);
		System.out.println("Deparment updated successfully");

		System.out.println("=== Test 4: department findAll ===");
		var deps = departmentDao.findAll();
		for (var uniqueDep : deps) {
			System.out.println(uniqueDep);
		}

		System.out.println("=== Test 5: department deleteById ===");
		System.out.print("Type department id to delete: ");
		var depId = input.nextInt();
		departmentDao.deleteById(depId);
		System.out.println("Department was deleted");

		input.close();
	}
}
