package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.ZipCode;

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCode, Integer> {

	public ZipCode findByCode(Integer code);
}
