package com.atm.demo.service;

import com.atm.demo.model.User;
import com.atm.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String cardNumber, String pin) {
        Optional<User> user = userRepository.findByCardNumber(cardNumber);
        if (user.isPresent() && user.get().getPin().equals(pin)) {
            return user;
        }
        return Optional.empty();
    }

    public Double getBalance(String cardNumber) {
        Optional<User> user = userRepository.findByCardNumber(cardNumber);
        if (user.isPresent()) {
            return user.get().getBalance();
        }
        throw new RuntimeException("User not found with card number: " + cardNumber);
    }



    public Double withdraw(String cardNumber, Double amount) {
        Optional<User> userOptional = userRepository.findByCardNumber(cardNumber);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with card number: "+cardNumber);
        }

        User user = userOptional.get();

        if (amount > getBalance(cardNumber)) {
            throw new RuntimeException("The amount you have entered exceeds the balance");
        }

        user.setBalance(user.getBalance() - amount);

        userRepository.save(user);

        return user.getBalance();
    }

}
