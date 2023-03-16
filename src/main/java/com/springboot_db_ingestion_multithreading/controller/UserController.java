package com.springboot_db_ingestion_multithreading.controller;

import com.springboot_db_ingestion_multithreading.model.User;
import com.springboot_db_ingestion_multithreading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class UserController {


    @Autowired
    private UserService userService;
    @PostMapping("/add/users")
    public ResponseEntity<User> saveUsers(MultipartFile file[])
    {

        Arrays.stream(file).forEach(f-> {
            try {
                userService.saveUsersData(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("getUsers")
    public List<User> fetchUsers() throws IOException, ExecutionException, InterruptedException {

        CompletableFuture<List<User>> listCompletableFuture1 = userService.fetchUsersData();
        CompletableFuture<List<User>> listCompletableFuture2 = userService.fetchUsersData();
        CompletableFuture<List<User>> listCompletableFuture3 = userService.fetchUsersData();
        List<CompletableFuture<List<User>>> completableFutureList=new ArrayList<>();
        completableFutureList.add(listCompletableFuture1);
        completableFutureList.add(listCompletableFuture2);
        completableFutureList.add(listCompletableFuture3);
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        CompletableFuture<List<List<User>>> listCompletableFuture = allFuture.thenApply(future -> completableFutureList.stream().map(cf -> cf.join()).collect(Collectors.toList()));
        CompletableFuture<List<List<User>>> listCompletableFuture4 = listCompletableFuture.toCompletableFuture();
        List<User> collect = listCompletableFuture4.get().stream().flatMap(List::stream).collect(Collectors.toList());
        return collect;
    }

}
