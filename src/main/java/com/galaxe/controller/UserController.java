package com.galaxe.controller;

import com.galaxe.advice.DuplicateEmailException;
import com.galaxe.advice.InvalidCredientialsException;
import com.galaxe.advice.InvalidPasswordException;
import com.galaxe.advice.LoginFailedException;
import com.galaxe.advice.SubscriptionFailedException;
import com.galaxe.advice.UserNotFoundException;
import com.galaxe.dto.UserDto;
import com.galaxe.entities.User;
import com.galaxe.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody UserDto userDto)
      throws DuplicateEmailException, InvalidPasswordException {
    return new ResponseEntity<User>(userService.subscribeUser(userDto), HttpStatus.OK);
  }

  @DeleteMapping("/cancelSubscription/{email}")
  public ResponseEntity<User> cancelSubscription(@PathVariable String email)
      throws UserNotFoundException {
    return new ResponseEntity<User>(userService.cancelSubscription(email), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody UserDto userDto)
      throws InvalidCredientialsException, LoginFailedException {
    return new ResponseEntity<User>(userService.loginUser(userDto), HttpStatus.OK);
  }

  @PutMapping("/retrieveSubscription")
  public ResponseEntity<User> retrieveSubsription(@RequestBody UserDto userDto)
      throws InvalidCredientialsException, SubscriptionFailedException {
    return new ResponseEntity<User>(userService.retrieveSubscription(userDto), HttpStatus.OK);
  }

  @GetMapping("/getAllUsers")
  public ResponseEntity<List<User>> getAllUsers() throws UserNotFoundException {
    return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
  }

  @GetMapping("/getDetailsOfAUser/{userId}")
  public ResponseEntity<User> getDetailsOfAUser(@PathVariable Integer userId)
      throws UserNotFoundException {
    return new ResponseEntity<User>(userService.getDetailsOfAUser(userId), HttpStatus.OK);
  }

  @DeleteMapping("/deleteAUser/{userId}")
  public ResponseEntity<User> deleteUserDetails(@PathVariable Integer userId)
      throws UserNotFoundException {
    return new ResponseEntity<User>(userService.deleteUserDetails(userId), HttpStatus.OK);
  }

  @PutMapping("/updatedUser")
  public ResponseEntity<User> updateUserDetails(@RequestBody User user)
      throws UserNotFoundException {
    return new ResponseEntity<User>(userService.updateUserDetails(user), HttpStatus.OK);
  }
}
