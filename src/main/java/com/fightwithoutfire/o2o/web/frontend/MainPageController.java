package com.fightwithoutfire.o2o.web.frontend;

import com.fightwithoutfire.o2o.entity.HeadLine;
import com.fightwithoutfire.o2o.entity.ShopCategory;
import com.fightwithoutfire.o2o.service.HeadLineService;
import com.fightwithoutfire.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class MainPageController {

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private HeadLineService headLineSerivce;
	
	@GetMapping(value = "/listmainpageinfo")
	private Map<String,Object> listMainPageInfo(){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(null);
			modelMap.put("shopCategoryList",shopCategoryList);
		}catch(Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineSerivce.getHeadLineList(headLineCondition);
			modelMap.put("headLineList",headLineList);
		}catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
