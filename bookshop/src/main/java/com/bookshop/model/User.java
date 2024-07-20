package com.bookshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;
    
    @Column(name="password")
    private String password;
    
    @Column(name="username")
    private String username;
    
    @Column(name="mobile")
    private String mobile; 
    
    @Column(name="role_id")
    private Integer roleId; 
    
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id",insertable=false, updatable=false)
    private Role role;
 }
