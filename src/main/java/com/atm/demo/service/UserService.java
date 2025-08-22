package com.atm.demo.service;

import com.atm.demo.model.User;
import com.atm.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public String addUser(String accNumber, String pin, Double balance) {

        if (!"admin123".equals(accNumber)) {
            // Otherwise must be exactly 8 digits
            if (accNumber == null || !accNumber.matches("\\d{8}")) {
                throw new RuntimeException("Card number must be 8 digits or 'admin123'.");
            }
        }

        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }

        User user = new User();
        user.setAccNumber(accNumber);
        user.setPin(pin);
        user.setBalance(balance);
        user.setRole("USER"); // default role
        userRepository.save(user);
        return "User added successfully!";
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String accNumber, String pin) {
        Optional<User> user = userRepository.findByAccNumber(accNumber);
        if (user.isPresent() && user.get().getPin().equals(pin)) {
            return user;
        }
        return Optional.empty();
    }

    public Double getBalance(String accNumber) {
        Optional<User> user = userRepository.findByAccNumber(accNumber);
        if (user.isPresent()) {
            return user.get().getBalance();
        }
        throw new RuntimeException("User not found with card number: " + accNumber);
    }



    public Double withdraw(String accNumber, Double amount) {
        Optional<User> userOptional = userRepository.findByAccNumber(accNumber);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with card number: "+accNumber);
        }

        User user = userOptional.get();

        if (amount > getBalance(accNumber)) {
            throw new RuntimeException("The amount you have entered exceeds the balance");
        }

        user.setBalance(user.getBalance() - amount);

        userRepository.save(user);

        return user.getBalance();
    }

    public Double deposit(String accNumber, Double amount){
        Optional<User> userOptional = userRepository.findByAccNumber(accNumber);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found with account number:"+accNumber);
        }
        User user = userOptional.get();

        if(amount ==0){
            throw new RuntimeException("Ivalid amount deposit");
        }

        user.setBalance(user.getBalance()+amount);

        userRepository.save(user);

        return user.getBalance();
    }

    public String resetPin(String accNumber, String oldPin, String newPin) {
        Optional<User> userOptional = userRepository.findByAccNumber(accNumber);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with card number: " + accNumber);
        }

        User user = userOptional.get();

        if (!user.getPin().equals(oldPin)) {
            throw new RuntimeException("Old PIN does not match.");
        }

        user.setPin(newPin);
        userRepository.save(user);

        return "PIN updated successfully!";
    }
    public void deleteUser(String accNumber) {
        Optional<User> user = userRepository.findByAccNumber(accNumber);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with card number: " + accNumber);
        }
        userRepository.delete(user.get());
    }

}
