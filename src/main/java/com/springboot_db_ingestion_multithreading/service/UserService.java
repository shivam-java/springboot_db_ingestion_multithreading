package com.springboot_db_ingestion_multithreading.service;


import com.opencsv.CSVReader;
import com.springboot_db_ingestion_multithreading.model.User;
import com.springboot_db_ingestion_multithreading.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class UserService {




    @Autowired
    UserRepository userRepository;

      Logger logger=LoggerFactory.getLogger(UserService.class);

  @Async
 public CompletableFuture<List<User>> saveUsersData(MultipartFile file) throws IOException {
         long start=System.currentTimeMillis();
      List<User> userData = getUserData(file);
      logger.info("data is being inserted to db of users size "+userData.size()+"  "+Thread.currentThread().getName());
      userData = userRepository.saveAll(userData);
      long end=System.currentTimeMillis();
      logger.info("Time Taken For Insertion"+(end-start));
      return CompletableFuture.completedFuture(userData);
  }


    @Async
    public CompletableFuture<List<User>> fetchUsersData() throws IOException {
        long start=System.currentTimeMillis();
        logger.info("data is being fetched from db by"+Thread.currentThread().getName());
        List<User> all = userRepository.findAll();
        long end=System.currentTimeMillis();
        logger.info("Time Taken For Insertion"+(end-start));
        return CompletableFuture.completedFuture(all);
    }

    public List<User> getUserData(final MultipartFile file) throws IOException {

        CSVReader csvReader=new CSVReader(new InputStreamReader(file.getInputStream()));
        String data[];
        List<User> users=new ArrayList<>();
        csvReader.readNext();
        while ((data=csvReader.readNext())!=null)
        {
            User build = User.builder().name(data[0]).address(data[1]).phoneno(data[2]).build();
            users.add(build);
        }
        return users;

    }

}
