package resources;

import pojo.AddProduct;
import pojo.Data;

public class TestDataBuild {
	
	public AddProduct addProductPayLoad(String name, String CPU_model, String price)
	{
		AddProduct addProduct =new AddProduct();
		addProduct.setName(name);
			
		Data data =new Data();
		data.setYear("2019");
		data.setPrice(price);
		data.setCPU_model(CPU_model);
		data.setHard_disk_size("1 TB");
		
		addProduct.setData(data);
		return addProduct;
	}
		
}
