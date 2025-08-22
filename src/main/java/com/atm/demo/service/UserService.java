package com.atm.demo.service;

import com.atm.demo.model.User;
import com.atm.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public String addUser(String cardNumber, String pin, Double balance) {

        if (!"admin123".equals(cardNumber)) {
            // Otherwise must be exactly 8 digits
            if (cardNumber == null || !cardNumber.matches("\\d{8}")) {
                throw new RuntimeException("Card number must be 8 digits or 'admin123'.");
            }
        }

        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }

        User user = new User();
        user.setCardNumber(cardNumber);
        user.setPin(pin);
        user.setBalance(balance);
        user.setRole("USER"); // default role
        userRepository.save(user);
        return "User added successfully!";
    }

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

    public Double deposit(String cardNumber, Double amount){
        Optional<User> userOptional = userRepository.findByCardNumber(cardNumber);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found with account number:"+cardNumber);
        }
        User user = userOptional.get();

        if(amount ==0){
            throw new RuntimeException("Ivalid amount deposit");
        }

        user.setBalance(user.getBalance()+amount);

        userRepository.save(user);

        return user.getBalance();
    }

    public String resetPin(String cardNumber, String oldPin, String newPin) {
        Optional<User> userOptional = userRepository.findByCardNumber(cardNumber);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with card number: " + cardNumber);
        }

        User user = userOptional.get();

        if (!user.getPin().equals(oldPin)) {
            throw new RuntimeException("Old PIN does not match.");
        }

        user.setPin(newPin);
        userRepository.save(user);

        return "PIN updated successfully!";
    }
    public void deleteUser(String cardNumber) {
        Optional<User> user = userRepository.findByCardNumber(cardNumber);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with card number: " + cardNumber);
        }
        userRepository.delete(user.get());
    }

}
