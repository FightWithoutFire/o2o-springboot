package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.entity.ShopCategory;
import com.fightwithoutfire.o2o.exceptions.ShopCategoryOperationException;

import java.util.List;

public interface ShopCategoryService {
	public static final String SCLISTKEY="shoptcategorylist";
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopCategoryOperationException;
}
