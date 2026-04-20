package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.exception.UserDoesNotExistException;
import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.Utilizator;
import com.pao.project.Eticketing.repository.InMemoryUserRepository;
import com.pao.project.Eticketing.repository.UserRepository;

import java.util.Optional;

public class AuthService {
    private static AuthService instance;

    private final UserRepository userRepository;
    private Utilizator currentUser;

    private AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService(new InMemoryUserRepository());
        }
        return instance;
    }

    public void register(Utilizator utilizator) {
        if (utilizator == null) {
            throw new IllegalArgumentException("Utilizatorul nu poate fi null");
        }
        if (userRepository.existsByUsername(utilizator.getUsername())) {
            throw new IllegalArgumentException("Username deja existent");
        }

        userRepository.save(utilizator);
    }

    public Utilizator login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username/parola invalide");
        }

        Optional<Utilizator> utilizator = userRepository.findByUsername(username);
        if (utilizator.isPresent() && utilizator.get().hasPassword(password)) {
            currentUser = utilizator.get();
            return utilizator.get();
        }

        return null;
    }

    public void logout(Utilizator utilizator) {
            if (utilizator == null) {
                throw new IllegalArgumentException("Nu exista utilizator logat");
            }
            if (currentUser != null && currentUser.equals(utilizator)) {
                currentUser = null;
            }
    }

    public void deleteUser(Utilizator utilizator){
        if (utilizator == null) {
            throw new IllegalArgumentException("Utilizatorul nu poate fi null");
        }
        if(!userRepository.existsByUsername(utilizator.getUsername())){
            throw new UserDoesNotExistException("Nu exista utilizatorul");
        }
        userRepository.deleteByUsername(utilizator.getUsername());
        if (currentUser != null && currentUser.equals(utilizator)) {
            currentUser = null;
        }
    }

    public Utilizator getCurrentUser() {
        return currentUser;
    }

    public Client getLoggedClient() {
        if (currentUser instanceof Client client) {
            return client;
        }
        return null;
    }

}
