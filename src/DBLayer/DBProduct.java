package DBLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBProduct implements IFDBProduct {
	private Connection con;

	/** Creates a new instance of DBProduct */
	public DBProduct() {
		con = DBConnection.getInstance().getDBcon();
	}

	// Implements the methods from the interface
	// get all Products
	public ArrayList<Product> getAllProducts(boolean retriveAssociation) {
		return miscWhere("", retriveAssociation);
	}

	// get one Product having the ssn
	public Product findProduct(String productName, boolean retriveAssociation) {
		String wClause = "  productName = '" + productName + "'";
		return singleWhere(wClause, retriveAssociation);
	}

	// find one Product having the fname
	public Product searchProductName(String attValue, boolean retriveAssociation) {
		String wClause = "name like '%" + attValue + "%'";
		System.out.println("SearchProduct " + wClause);
		return singleWhere(wClause, retriveAssociation);
	}

	// find one Product having the lname

	public int insertProduct(Product product) throws Exception {
		int nextproductName = GetMax
				.getMaxId("Select max(productName) from Product");
		nextproductName = nextproductName + 1;
		System.out.println("next productName = " + nextproductName);

		int rc = -1;
		String query = "INSERT INTO Product(productName, purchasePrice, salesPrice, rentPrice, countryOfOrigin, currentStock)  VALUES('"
				+ product.getProductName()
				+ "','"
				+ nextproductName
				+ "','"
				+ product.getPurchasePrice()
				+ "','"
				+ product.getSalesPrice()
				+ "','"
				+ product.getRentPrice
				+ "','"
				+ product.getCountryOfOrigin()
				+ "','"
				+ product.getCurrentStock();

		System.out.println("insert : " + query);
		try {
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);
			stmt.close();
		}// end try
		catch (SQLException ex) {
			System.out.println("Product ikke oprettet");
			throw new Exception("Product is not inserted correct");
		}
		return (rc);
	}

	@Override
	public int updateProduct(Product product) {
		Product productObj = Product;
		int rc = -1;

		String query = "UPDATE Product SET " + "productName ='"
				+ productObj.getProductName() + "', " + "purchasePrice ='"
				+ productObj.getPurchasePrice() + "', " + "salesPrice='"
				+ productObj.getSalesPrice() + "', " + "rentPrice ='"
				+ productObj.getRentPrice() + "', " + "countryOfOrigin='"
				+ productObj.getCountryOfOrigin() + "', " + "currentStock='"
				+ productObj.getCurrentStock() + "', "
				+ " WHERE productName = '" + productObj.getproductName() + "'";
		System.out.println("Update query:" + query);
		try { // update Product
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);

			stmt.close();
		}// slut try
		catch (Exception ex) {
			System.out.println("Update exception in Product db: " + ex);
		}
		return (rc);
	}

	public int delete(String productName) {
		int rc = -1;

		String query = "DELETE FROM Product WHERE productName = '"
				+ productName + "'";
		System.out.println(query);
		try { // delete from Product
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);
			stmt.close();
		}// slut try
		catch (Exception ex) {
			System.out.println("Delete exception in Product db: " + ex);
		}
		return (rc);
	}

	// private methods
	// michWere is used whenever we want to select more than one Product

	private ArrayList<Product> miscWhere(String wClause,
			boolean retrieveAssociation) {
		ResultSet results;
		ArrayList<Product> list = new ArrayList<Product>();

		String query = buildQuery(wClause);

		try { // read the Product from the database
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			results = stmt.executeQuery(query);

			while (results.next()) {
				Product productObj = new Product();
				productObj = buildProduct(results);
				list.add(productObj);
			}// end while
			stmt.close();
		} catch (Exception e) {
			System.out.println("Query exception - select: " + e);
			e.printStackTrace();
		}
		return list;
	}

	// Singelwhere is used when we only select one Product
	private Product singleWhere(String wClause, boolean retrieveAssociation) {
		ResultSet results;
		Product productObj = new Product();

		String query = buildQuery(wClause);
		System.out.println(query);
		try { // read the Product from the database
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			results = stmt.executeQuery(query);

			if (results.next()) {
				productObj = buildProduct(results);
				// assocaition is to be build
				stmt.close();

			} else { // no Product was found
				productObj = null;
			}
		}// end try
		catch (Exception e) {
			System.out.println("Query exception: " + e);
		}
		return productObj;
	}

	// method to build the query
	private String buildQuery(String wClause) {
		String query = "SELECT productName, purchasePrice, salesPrice, rentPrice, countryOfOrigin, currentStock  FROM Product";

		if (wClause.length() > 0)
			query = query + " WHERE " + wClause;

		return query;
	}

	// method to build an Product object
	private Product buildProduct(ResultSet results) {
		Product productObj = new Product();
		try { // the columns from the table emplayee are used
			productObj.setProductName(results.getString("productName"));
			productObj.setPurchasePrice(results.getDouble("purchasePrice"));
			productObj.setSalesPrice(results.getDouble("salesPrice"));
			productObj.setRentPrice(results.getDouble("rentPrice"));
			productObj.setCountryOfOrigin(results.getString("countryOfOrigin"));
			productObj.setCurrentStock(results.getInt("currentStock"));

		} catch (Exception e) {
			System.out.println("error in building the Product object");
		}
		return productObj;
	}

	@Override
	public Product searchProduct(String productName, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

}
