package com.fightwithoutfire.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightwithoutfire.o2o.cache.JedisUtil;
import com.fightwithoutfire.o2o.dao.ShopCategoryDao;
import com.fightwithoutfire.o2o.entity.ShopCategory;
import com.fightwithoutfire.o2o.exceptions.ShopCategoryOperationException;
import com.fightwithoutfire.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl  implements ShopCategoryService{

	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	private static Logger logger=LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
		
	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopCategoryOperationException {
		String key=SCLISTKEY;
		List<ShopCategory> shopCategoryList=null;
		ObjectMapper mapper = new ObjectMapper();
		if(shopCategoryCondition==null) {
			key=key+"_allfirstlevel";
		}else if(shopCategoryCondition!=null&&shopCategoryCondition.getShopCategoryId()!=null
				&&shopCategoryCondition.getParent().getShopCategoryId()!=null) {
			key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
		}else if(shopCategoryCondition!=null) {
			key=key+"_allsecondlevel";
		}
		if(!jedisKeys.exists(key)) {
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			String jsonString;
			try {
				jsonString= mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			jedisStrings.set(key,jsonString);
		}else {
			String jsonString=jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList=mapper.readValue(jsonString, javaType);
				
			}catch(JsonMappingException e) {
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}catch (IOException e) {
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}

}
