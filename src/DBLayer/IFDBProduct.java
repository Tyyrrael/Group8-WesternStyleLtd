package DBLayer;

import java.util.ArrayList;

public interface IFDBProduct {
	public ArrayList<Product> getAllProducts(boolean retriveAssociation);

	public Product findProduct(String productId, boolean retriveAssociation);

	public Product searchProductName(String productName,
			boolean retriveAssociation);

	public int insertProduct(Product product) throws Exception;

	public int updateProduct(Product product);

	public Product searchProductId(String productId, boolean b);

}
