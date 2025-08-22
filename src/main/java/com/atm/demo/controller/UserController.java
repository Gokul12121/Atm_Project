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
    public String login(@RequestParam String accNumber,@RequestParam String pin){
        Optional<User> user = userService.login(accNumber,pin);

        if(user.isPresent()){
            return "Login successful Welcome, "+user.get().getAccNumber();
        }else{
            return "Invalid card number or Pin. Try again!";
        }
    }

    @GetMapping("/balance")
    public  String checkBalance(@RequestParam String accNumber){
        Double balance = userService.getBalance(accNumber);
        return "Your current balance is: "+balance;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accNumber, @RequestParam Double amount){
        Double updatedBalance = userService.withdraw(accNumber,amount);
        return "Withdrawal successfull! New Balance: "+updatedBalance;

    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accNumber,  @RequestParam Double amount){
        Double updatedBalance = userService.deposit(accNumber,amount);
        return "Deposit successfull! New Balance: "+updatedBalance;
    }

    @PostMapping("/reset-pin")
    public String resetPin(@RequestParam String accNumber,
                           @RequestParam String oldPin,
                           @RequestParam String newPin) {
        return userService.resetPin(accNumber, oldPin, newPin);
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String accNumber,
                          @RequestParam String pin,
                          @RequestParam Double balance) {
        return userService.addUser(accNumber, pin, balance);
    }


}
