package com.galaxe.service;

import com.galaxe.advice.DuplicateEmailException;
import com.galaxe.advice.InvalidCredientialsException;
import com.galaxe.advice.LoginFailedException;
import com.galaxe.advice.SubscriptionFailedException;
import com.galaxe.advice.UserNotFoundException;
import com.galaxe.dto.UserDto;
import com.galaxe.entities.User;
import java.util.List;

public interface UserService {
  public User subscribeUser(UserDto userDto) throws DuplicateEmailException;

  public User cancelSubscription(String email) throws UserNotFoundException;

  public User loginUser(UserDto userDto) throws InvalidCredientialsException, LoginFailedException;

  public User retrieveSubscription(UserDto userDto)
      throws InvalidCredientialsException, SubscriptionFailedException;

  public List<User> getAllUsers() throws UserNotFoundException;

  public User getDetailsOfAUser(Integer userId) throws UserNotFoundException;

  public User deleteUserDetails(Integer userId) throws UserNotFoundException;

  public User updateUserDetails(User user) throws UserNotFoundException;
}
