package com.springboot_db_ingestion_multithreading.repository;

import com.springboot_db_ingestion_multithreading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{

}
