package DBLayer;

import java.util.ArrayList;

public interface IFDBCustomer {
	public ArrayList<Customer> getAllCustomers(boolean retriveAssociation);

	public Customer findCustomer(String cpr, boolean retriveAssociation);

	public Customer searchCustomerName(String customerName,
			boolean retriveAssociation);

	public int insertCustomer(Customer customer) throws Exception;

	public int updateCustomer(Customer customer);

	public Customer searchcpr(String cpr, boolean b);

}
