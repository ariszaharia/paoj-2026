package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.user.Utilizator;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(Utilizator utilizator);

    Optional<Utilizator> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Utilizator> findAll();

    boolean deleteByUsername(String username);
}

