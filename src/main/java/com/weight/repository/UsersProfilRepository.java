package com.weight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weight.model.UsersProfil;
import com.weight.model.UsersProfilId;

public interface UsersProfilRepository extends JpaRepository<UsersProfil,UsersProfilId> {

}
