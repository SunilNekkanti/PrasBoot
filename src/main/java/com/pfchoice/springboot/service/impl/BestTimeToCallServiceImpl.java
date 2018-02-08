package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.BestTimeToCall;
import com.pfchoice.springboot.repositories.BestTimeToCallRepository;
import com.pfchoice.springboot.service.BestTimeToCallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bestTimeToCallService")
@Transactional
public class BestTimeToCallServiceImpl implements BestTimeToCallService {

	@Autowired
	private BestTimeToCallRepository bestTimeToCallRepository;

	public BestTimeToCall findById(Short id) {
		return bestTimeToCallRepository.findOne(id);
	}

	public BestTimeToCall findByDescription(String name) {
		return bestTimeToCallRepository.findByDescription(name);
	}

	public void deleteBestTimeToCallById(Short id) {
		bestTimeToCallRepository.delete(id);
	}

	public void deleteAllBestTimeToCalls() {
		bestTimeToCallRepository.deleteAll();
	}

	public List<BestTimeToCall> findAllBestTimeToCalls() {
		return bestTimeToCallRepository.findAll();
	}

	public boolean isBestTimeToCallExist(BestTimeToCall bestTimeToCall) {
		return findById(bestTimeToCall.getId()) != null;
	}

}
