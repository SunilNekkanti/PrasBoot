package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.FollowupType;

@Repository
public interface FollowupTypeRepository extends JpaRepository<FollowupType, Long> {

}
