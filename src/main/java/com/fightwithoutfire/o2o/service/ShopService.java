package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.dto.ImageHolder;
import com.fightwithoutfire.o2o.dto.ShopExecution;
import com.fightwithoutfire.o2o.entity.Shop;

public interface ShopService {
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder);
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder);
	public Shop getByShopId(long shopId);
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
}
