package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Byte> {

	public Gender findByCode(char code);

	public Gender findByDescription(String code);
}
