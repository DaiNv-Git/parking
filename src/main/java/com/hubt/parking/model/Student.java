package com.hubt.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;
    
    @Column(nullable = false)
    private String fullName;
    
    private String className;
    
    private String phone;
}
