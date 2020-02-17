package com.fightwithoutfire.o2o.web.superadmin;

import com.fightwithoutfire.o2o.entity.Area;
import com.fightwithoutfire.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/superadmin")
public class AreaController {

	Logger logger= LoggerFactory.getLogger(AreaController.class);

	@Autowired
	private AreaService areaService;
	
	@GetMapping(value = "/listarea")
	private Map<String,Object> listArea(){
		logger.info("===start===");
		long startTime=System.currentTimeMillis();
		Map<String, Object> modelMap=new HashMap<String, Object>();
		List<Area> list=new ArrayList<Area>();
		try {
			list=areaService.getAreaList();
			modelMap.put("row", list);
			modelMap.put("total", list.size());
		}catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("ermeg", e.toString());
		}
		logger.error("test error!");
		long endTime=System.currentTimeMillis();
		logger.debug("constTime:[{}ms]",(endTime-startTime));
		logger.info("===end===");
		return modelMap;
	}
}
