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

    public Optional<User> login(String cardNumber, String pin){
        Optional<User> user = userRepository.findByCardNumber(cardNumber);
        if(user.isPresent() && user.get().getPin().equals(pin)){
            return user;
        }
        return Optional.empty();
    }

}
