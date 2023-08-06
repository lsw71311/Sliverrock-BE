package com.example.silverrock.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    Optional<User> findUserByPhoneNum(@Param("phoneNum") String phoneNum);

 /*   @Modifying
    @Query("delete from User u where u.id = :userId")
    void deleteUser(@Param("userId") Long userId);
*/
    boolean existsByNickname(String nickName);

}