package com.fightwithoutfire.o2o.service;

import com.fightwithoutfire.o2o.entity.HeadLine;
import com.fightwithoutfire.o2o.exceptions.HeadLineOperationException;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {
	public static final String HLLISTKEY="headlinelists";
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException, HeadLineOperationException;
}
