package application;

import model.dao.DaoFactory;

public class Program {

	public static void main(String[] args) {
		var sellerDao = DaoFactory.createSellerDao();

		System.out.println("=== TEST 1: seller findById ====");
		var seller = sellerDao.findById(3);

		System.out.println(seller);
	}
}
