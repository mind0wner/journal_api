package com.journal.repository;

import com.journal.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u ORDER BY u.lastName")
    List<User> findAllOrderedByLastName();

    User findByUsername(String username);

    User findByActivationCode(String code);

}