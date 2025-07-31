package com.atm.demo.controller;

import com.atm.demo.model.User;
import com.atm.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String cardNumber,@RequestParam String pin){
        Optional<User> user = userService.login(cardNumber,pin);

        if(user.isPresent()){
            return "Login successful Welcome, "+user.get().getCardNumber();
        }else{
            return "Invalid card number or Pin. Try again!";
        }
    }

    @GetMapping("/balance")
    public  String checkBalance(@RequestParam String cardNumber){
        Double balance = userService.getBalance(cardNumber);
        return "Your current balance is: "+balance;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String cardNumber, @RequestParam Double amount){
        Double updatedBalance = userService.withdraw(cardNumber,amount);
        return "Withdrawal successfull! New Balance: "+updatedBalance;

    }

}
