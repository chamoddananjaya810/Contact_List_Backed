/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Chamod
 */

@Entity
@Table(name = "user")
public class User implements Serializable{
      @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name", length = 45, nullable = true)
    private String first_name;
    
     @Column(name = "last_name", length = 45, nullable = true)
    private String last_name;
     
      @Column(name = "email", length = 45, nullable = false)
    private String email;
      
       @Column(name = "password", length = 10, nullable = false)
    private String password;
}
