package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.dto.ImageHolder;
import com.fightwithoutfire.o2o.dto.ProductExecution;
import com.fightwithoutfire.o2o.entity.Product;
import com.fightwithoutfire.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {
	ProductExecution addProduct(Product product, ImageHolder imgholder,
                                List<ImageHolder> productImgList)throws ProductOperationException;
	ProductExecution modifyProduct(Product product, ImageHolder imghoder,
                                   List<ImageHolder> productImgList) throws ProductOperationException;
	Product getProductById(long productId);
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
