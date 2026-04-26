package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.exception.StergereUtilizator;
import com.pao.project.Eticketing.exception.UserDoesNotExistException;
import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.Organizator;
import com.pao.project.Eticketing.model.user.Utilizator;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static AuthService instance;

    private final Map<String, Utilizator> usersByUsername = new HashMap<>();
    private Utilizator currentUser;

    private AuthService() {
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public void register(Utilizator utilizator) {
        if (utilizator == null) {
            throw new IllegalArgumentException("Utilizatorul nu poate fi null");
        }
        if (usersByUsername.containsKey(utilizator.getUsername())) {
            throw new IllegalArgumentException("Username deja existent");
        }

        usersByUsername.put(utilizator.getUsername(), utilizator);
    }

    public Utilizator login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username/parola invalide");
        }

        Utilizator utilizator = usersByUsername.get(username);
        if (utilizator != null && utilizator.hasPassword(password)) {
            currentUser = utilizator;
            return utilizator;
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

        Utilizator existent = usersByUsername.get(utilizator.getUsername());
        if (existent == null) {
            throw new UserDoesNotExistException("Nu exista utilizatorul");
        }
        if (existent.getBalance() != 0) {
            throw new StergereUtilizator("Nu se poate sterge utilizatorul deoarece are un sold diferit de 0");
        }

        usersByUsername.remove(utilizator.getUsername());
        if (currentUser != null && currentUser.equals(utilizator)) {
            currentUser = null;
        }
    }

    public Utilizator getCurrentUser() {
        return currentUser;
    }

    public java.util.Optional<Utilizator> findByUsername(String username) {
        return java.util.Optional.ofNullable(usersByUsername.get(username));
    }

    public java.util.Collection<Utilizator> getAllUsers() {
        return java.util.Collections.unmodifiableCollection(usersByUsername.values());
    }

    public Client getLoggedClient() {
        if (currentUser instanceof Client client) {
            return client;
        }
        return null;
    }

    public Organizator getLoggedOrganizator() {
        if (currentUser instanceof Organizator organizator) {
            return organizator;
        }
        return null;
    }

}
