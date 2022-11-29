package application;

import model.dao.DaoFactory;
import model.entities.Department;

public class Program {

	public static void main(String[] args) {
		var sellerDao = DaoFactory.createSellerDao();

		System.out.println("=== TEST 1: seller findById ====");
		var singleSeller = sellerDao.findById(3);
		System.out.println(singleSeller);

		System.out.println("\n=== TEST 2: seller findByDepartment ====");
		var allSellers = sellerDao.findByDepartment(new Department(2, null));
		for (var seller : allSellers) {
			System.out.println(seller);
		}

		System.out.println("\n=== TEST 3: seller findAll ====");
		allSellers = sellerDao.findAll();
		for (var seller : allSellers) {
			System.out.println(seller);
		}
	}
}
