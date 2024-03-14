package com.galaxe.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "GxUser")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;

  private String name;
  private String email;
  private LocalDate dob;
  private String password;
  private Boolean subscriptionStatus;
  private String type;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    User other = (User) obj;
    return Objects.equals(dob, other.dob)
        && Objects.equals(email, other.email)
        && Objects.equals(name, other.name)
        && Objects.equals(password, other.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dob, email, name, password);
  }
}
