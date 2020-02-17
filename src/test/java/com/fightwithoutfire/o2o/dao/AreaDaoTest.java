package com.fightwithoutfire.o2o.dao;



import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fightwithoutfire.o2o.entity.Area;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class AreaDaoTest{

	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQuery() {
		List<Area>  areaList=areaDao.queryArea();
		assertEquals(4, areaList.size());
	}
}
