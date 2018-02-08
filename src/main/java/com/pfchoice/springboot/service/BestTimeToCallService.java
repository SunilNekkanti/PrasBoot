package com.pfchoice.springboot.service;

import java.util.List;

import com.pfchoice.springboot.model.BestTimeToCall;

public interface BestTimeToCallService {

	BestTimeToCall findById(Short id);

	BestTimeToCall findByDescription(String description);

	void deleteBestTimeToCallById(Short id);

	void deleteAllBestTimeToCalls();

	List<BestTimeToCall> findAllBestTimeToCalls();

	boolean isBestTimeToCallExist(BestTimeToCall bestTimeToCall);
}