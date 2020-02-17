package com.fightwithoutfire.o2o.service.impl;

import com.fightwithoutfire.o2o.annotation.RedisCache;
import com.fightwithoutfire.o2o.cache.JedisUtil;
import com.fightwithoutfire.o2o.dao.AreaDao;
import com.fightwithoutfire.o2o.entity.Area;
import com.fightwithoutfire.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
	

	private static String AREALISTKEY="arealist";
	private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
	
	@Override
	@RedisCache(cacheKey = "arealist")
	public List<Area> getAreaList()  {
		List<Area> areaList=areaDao.queryArea();
		return areaList;
	}
	
}
