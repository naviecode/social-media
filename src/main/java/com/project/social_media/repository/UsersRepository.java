package com.project.social_media.repository;

import com.project.social_media.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

//    @Query("SELECT u FROM Users u WHERE (:name IS NULL OR :name = '' OR u.fullName LIKE %:name% OR u.username LIKE %:name%)")
//    List<Users> findByNameContaining(@Param("name") String name);

    @Query("SELECT u FROM Users u WHERE (:name IS NULL OR :name = '' OR u.fullName LIKE %:name% OR u.username LIKE %:name%) AND u.userId <> :loggedInUserId")
    List<Users> findByNameContaining(@Param("name") String name, @Param("loggedInUserId") Long loggedInUserId);
}
