package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.dto.ProductCategoryExecution;
import com.fightwithoutfire.o2o.entity.ProductCategory;
import com.fightwithoutfire.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
	List<ProductCategory> getProductCategoryList(long shopId);
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
