package DBLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DBCustomer.java
 * 
 * @author kbh
 * @version 5. oktober 2006, 2011.02.16
 *
 */
public class DBCustomer implements IFDBCustomer {
	private Connection con;

	/** Creates a new instance of DBCustomer */
	public DBCustomer() {
		con = DBConnection.getInstance().getDBcon();
	}

	// Implements the methods from the interface
	// get all Customers
	public ArrayList<Customer> getAllCustomers(boolean retriveAssociation) {
		return miscWhere("", retriveAssociation);
	}

	// get one Customer having the ssn
	public Customer findCustomer(String cpr, boolean retriveAssociation) {
		String wClause = "  cpr = '" + cpr + "'";
		return singleWhere(wClause, retriveAssociation);
	}

	// find one Customer having the fname
	public Customer searchCustomerName(String attValue,
			boolean retriveAssociation) {
		String wClause = "name like '%" + attValue + "%'";
		System.out.println("SearchCustomer " + wClause);
		return singleWhere(wClause, retriveAssociation);
	}

	// find one Customer having the lname

	public int insertCustomer(Customer customer) throws Exception { // call to
																	// get the
																	// next ssn
																	// number
		int nextCPR = GetMax.getMaxId("Select max(cpr) from Customer");
		nextCPR = nextCPR + 1;
		System.out.println("next cpr = " + nextCPR);

		int rc = -1;
		String query = "INSERT INTO Customer(name, cpr, address, country, phoneNo, email)  VALUES('"
				+ customer.getName()
				+ "','"
				+ nextCPR
				+ "','"
				+ customer.getAddress()
				+ "','"
				+ customer.getCountry()
				+ "','"
				+ customer.getPhoneNo() + "','" + customer.getEmail() + "','" +

				System.out.println("insert : " + query);
		try { // insert new Customer + dependent
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);
			stmt.close();
		}// end try
		catch (SQLException ex) {
			System.out.println("Customer ikke oprettet");
			throw new Exception("Customer is not inserted correct");
		}
		return (rc);
	}

	@Override
	public int updateCustomer(Customer customer) {
		Customer customerObj = customer;
		int rc = -1;

		String query = "UPDATE Customer SET " + "name ='"
				+ customerObj.getName() + "', " + "cpr ='"
				+ customerObj.getCpr() + "', " + "address='"
				+ customerObj.getAddress() + "', " + "country ='"
				+ customerObj.getCountry() + "', " + "phoneNo='"
				+ customerObj.getPhoneNo() + "', " + "email='"
				+ customerObj.getEmail() + "', " + " WHERE cpr = '"
				+ customerObj.getCpr() + "'";
		System.out.println("Update query:" + query);
		try { // update Customer
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);

			stmt.close();
		}// slut try
		catch (Exception ex) {
			System.out.println("Update exception in Customer db: " + ex);
		}
		return (rc);
	}

	public int delete(String cpr) {
		int rc = -1;

		String query = "DELETE FROM Customer WHERE cpr = '" + cpr + "'";
		System.out.println(query);
		try { // delete from Customer
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			rc = stmt.executeUpdate(query);
			stmt.close();
		}// slut try
		catch (Exception ex) {
			System.out.println("Delete exception in Customer db: " + ex);
		}
		return (rc);
	}

	// private methods
	// michWere is used whenever we want to select more than one Customer

	private ArrayList<Customer> miscWhere(String wClause,
			boolean retrieveAssociation) {
		ResultSet results;
		ArrayList<Customer> list = new ArrayList<Customer>();

		String query = buildQuery(wClause);

		try { // read the Customer from the database
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			results = stmt.executeQuery(query);

			while (results.next()) {
				Customer customerObj = new Customer();
				customerObj = buildCustomer(results);
				list.add(customerObj);
			}// end while
			stmt.close();
		} catch (Exception e) {
			System.out.println("Query exception - select: " + e);
			e.printStackTrace();
		}
		return list;
	}

	// Singelwhere is used when we only select one Customer
	private Customer singleWhere(String wClause, boolean retrieveAssociation) {
		ResultSet results;
		Customer customerObj = new Customer();

		String query = buildQuery(wClause);
		System.out.println(query);
		try { // read the Customer from the database
			Statement stmt = con.createStatement();
			stmt.setQueryTimeout(5);
			results = stmt.executeQuery(query);

			if (results.next()) {
				customerObj = buildCustomer(results);
				// assocaition is to be build
				stmt.close();

			} else { // no Customer was found
				customerObj = null;
			}
		}// end try
		catch (Exception e) {
			System.out.println("Query exception: " + e);
		}
		return customerObj;
	}

	// method to build the query
	private String buildQuery(String wClause) {
		String query = "SELECT name, cpr, address, country, phoneNo, email  FROM Customer";

		if (wClause.length() > 0)
			query = query + " WHERE " + wClause;

		return query;
	}

	// method to build an Customer object
	private Customer buildCustomer(ResultSet results) {
		Customer customerObj = new Customer();
		try { // the columns from the table emplayee are used
			customerObj.setName(results.getString("name"));
			customerObj.setCpr(results.getString("cpr"));
			customerObj.setAddress(results.getString("address"));
			customerObj.setCountry(results.getCountry("country"));
			customerObj.setPhoneNo(results.getPhoneNo("phoneNo"));
			customerObj.setEmail(results.getString("email"));

		} catch (Exception e) {
			System.out.println("error in building the Customer object");
		}
		return customerObj;
	}

	@Override
	public Customer searchCustomer(String cpr, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

}
