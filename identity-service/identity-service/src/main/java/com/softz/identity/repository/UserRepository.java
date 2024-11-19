package com.softz.identity.repository;

import com.softz.identity.entity.User;
import com.softz.identity.entity.projection.UserBasicInfo;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<UserBasicInfo> findUserBasicInfoByUsername(String username);
    void deleteByUsername(@NotNull String s);

}
