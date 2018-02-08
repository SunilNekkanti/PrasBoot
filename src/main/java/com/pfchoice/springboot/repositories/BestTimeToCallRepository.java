package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.BestTimeToCall;

@Repository
public interface BestTimeToCallRepository extends JpaRepository<BestTimeToCall, Short> {

	BestTimeToCall findByDescription(String description);
}
