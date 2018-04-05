package com.pfchoice.springboot.repositories.intf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RecordDetailsAwareRepository<T, ID extends java.io.Serializable> extends JpaRepository<T, ID> {

}