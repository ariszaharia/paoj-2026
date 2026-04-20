package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.exception.StergereUtilizator;
import com.pao.project.Eticketing.model.user.Utilizator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, Utilizator> usersByUsername = new HashMap<>();

    @Override
    public void save(Utilizator utilizator) {
        usersByUsername.put(utilizator.getUsername(), utilizator);
    }

    @Override
    public Optional<Utilizator> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public List<Utilizator> findAll() {
        return new ArrayList<>(usersByUsername.values());
    }

    @Override
    public boolean deleteByUsername(String username) {
        if (usersByUsername.get(username).getBalance() != 0){
            throw new StergereUtilizator("Nu se poate sterge utilizatorul deoarece are un sold diferit de 0");
        }
        usersByUsername.remove(username);
        return true;
    }
}

