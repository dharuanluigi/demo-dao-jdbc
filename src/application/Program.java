package application;

import model.dao.DaoFactory;

public class Program {

	public static void main(String[] args) {
		var sellerDao = DaoFactory.createSellerDao();

		var seller = sellerDao.findById(3);

		System.out.println(seller);
	}
}
