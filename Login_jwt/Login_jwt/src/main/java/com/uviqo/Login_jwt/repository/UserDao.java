package com.uviqo.Login_jwt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uviqo.Login_jwt.model.DAOUser;

@Repository
public interface UserDao extends CrudRepository<DAOUser, Integer> {

	DAOUser findByUsername(String username);

}
