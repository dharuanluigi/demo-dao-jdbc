package application;

import java.util.Date;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		var input = new Scanner(System.in);
		var sellerDao = DaoFactory.createSellerDao();

		System.out.println("=== TEST 1: seller findById ====");
		var singleSeller = sellerDao.findById(3);
		System.out.println(singleSeller);

		System.out.println("\n=== TEST 2: seller findByDepartment ====");
		var department = new Department(2, null);
		var allSellers = sellerDao.findByDepartment(department);
		for (var seller : allSellers) {
			System.out.println(seller);
		}

		System.out.println("\n=== TEST 3: seller findAll ====");
		allSellers = sellerDao.findAll();
		for (var seller : allSellers) {
			System.out.println(seller);
		}

		System.out.println("\n=== TEST 4: seller insert ====");
		singleSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(singleSeller);
		System.out.println("Seller added! New id = " + singleSeller.getId());

		System.out.println("\n=== TEST 5: seller update ====");
		singleSeller = sellerDao.findById(1);
		singleSeller.setName("Martha Waine");
		sellerDao.update(singleSeller);
		System.out.println("Updated completed");

		System.out.println("\n=== TEST 6: seller delete ====");
		System.out.print("Enter id for delete test: ");
		var sellerId = input.nextInt();
		sellerDao.deleteById(sellerId);
		System.out.println("Delete seller successfully");
		input.close();
	}
}
