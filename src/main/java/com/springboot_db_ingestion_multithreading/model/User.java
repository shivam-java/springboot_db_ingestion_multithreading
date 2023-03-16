package com.springboot_db_ingestion_multithreading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userModel")
@Builder
public class User {
@Id
@GeneratedValue
private int id;
private String name;
private String address;
private String  phoneno;
}
