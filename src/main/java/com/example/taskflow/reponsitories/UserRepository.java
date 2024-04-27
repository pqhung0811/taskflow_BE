package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.User;
import com.example.taskflow.services.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.id = :id")
    public CustomUserDetails findUserDetailsById(@Param("id") int id);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    public boolean existsByEmail(@Param("email") String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :userId")
    public int updateNameById(String name, int userId);

}
