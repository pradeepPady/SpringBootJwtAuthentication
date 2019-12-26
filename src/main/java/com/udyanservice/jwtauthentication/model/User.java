package com.udyanservice.jwtauthentication.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Document(collection = "user")
@Data
public class User {
  @Id private String id;

  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
  private String email;

  @NotBlank
  @Size(min = 6, max = 100)
  private String password;

  @DBRef private Set<Role> roles;

  public User(String name, String username, String email, String encode) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = encode;
  }
}
