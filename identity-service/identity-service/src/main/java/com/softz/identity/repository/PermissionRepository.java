package com.softz.identity.repository;

import com.softz.identity.entity.Permission;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String name); 
    List<Permission> findByIdIn(Collection<Integer> ids);
}

