package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Ethinicity;

@Repository
public interface EthinicityRepository extends JpaRepository<Ethinicity, Long> {

}
