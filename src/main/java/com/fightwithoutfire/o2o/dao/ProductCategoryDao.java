package com.fightwithoutfire.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fightwithoutfire.o2o.entity.ProductCategory;


public interface ProductCategoryDao {
	List<ProductCategory> queryProductCategoryList(long shopId);
	int batchInserctProductCategory(List<ProductCategory> productCategoryList);
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
