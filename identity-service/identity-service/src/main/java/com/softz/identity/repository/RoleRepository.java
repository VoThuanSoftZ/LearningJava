package com.softz.identity.repository;

import com.softz.identity.entity.Permission;
import com.softz.identity.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name); 
    List<Role> findByIdIn(Collection<Integer> ids);
}

