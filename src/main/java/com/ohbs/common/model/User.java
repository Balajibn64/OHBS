package com.ohbs.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	private String username;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	private Role role;
}
