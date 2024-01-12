package com.krishna.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishna.auth.entity.UserCrediential;

public interface UserCrediantialRepository extends JpaRepository<UserCrediential,Integer> {

	Optional<UserCrediential> findByEmailId(String username);

}
