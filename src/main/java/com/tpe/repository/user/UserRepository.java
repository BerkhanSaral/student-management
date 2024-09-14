package com.tpe.repository.user;

import com.tpe.entity.concretes.user.User;
import com.tpe.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameEquals(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String ssn);

    boolean existsByEmail(String email);

    @Query("select u from User u where u.userRole.roleName = :roleName")
    Page<UserResponse> findByUserByRole(String roleName, Pageable pageable);


}
