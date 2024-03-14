package com.galaxe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.galaxe.advice.InvalidCredientialsException;
import com.galaxe.advice.LoginFailedException;
import com.galaxe.advice.SubscriptionFailedException;
import com.galaxe.advice.UserNotFoundException;
import com.galaxe.dto.UserDto;
import com.galaxe.entities.User;
import com.galaxe.repository.UserRepository;
import com.galaxe.serviceImpl.UserServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestUserService {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserServiceImpl userService;

  private User user;
  private UserDto userDto;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    user = new User(1, "Sumant", "sum@gmail.com", LocalDate.of(2001, 6, 27), "123", true, "User");
    userDto = new UserDto("Sumant", "sum@gmail.com", LocalDate.of(2001, 6, 27), "123");
  }

  /*@Test
  public void testSubscribeUser() throws DuplicateEmailException {
    when(userRepository.save(any())).thenReturn(user);
    doNothing().when(userService).sendEmail(any(), any(), any());
    User savedUser = userService.subscribeUser(userDto);
    assertEquals(user, savedUser);
  }*/

  @Test
  public void testloginUserWithValidData()
      throws InvalidCredientialsException, LoginFailedException {
    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);
    User actualUser = userService.loginUser(userDto);
    assertEquals(user, actualUser);
  }

  @Test
  public void testloginUserWithInvalidData()
      throws InvalidCredientialsException, LoginFailedException {
    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(null);

    assertThrows(
        InvalidCredientialsException.class,
        () -> {
          userService.loginUser(userDto);
        });
  }

  @Test
  public void testloginUserWithUnsubscribedUser()
      throws InvalidCredientialsException, LoginFailedException {
    user.setSubscriptionStatus(false);
    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);
    // doNothing().when(userService).sendEmail(a, null, null);
    assertThrows(
        LoginFailedException.class,
        () -> {
          userService.loginUser(userDto);
        });
  }

  @Test
  public void testRetrieveSubscriptionWithValidData()
      throws InvalidCredientialsException, SubscriptionFailedException {
    user.setSubscriptionStatus(false);
    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);
    when(userRepository.save(any())).thenReturn(user);
    User retrivedUser = userService.retrieveSubscription(userDto);
    assertTrue(retrivedUser.getSubscriptionStatus());
  }

  @Test
  public void testRetrieveSubscriptionWithInvalidData()
      throws InvalidCredientialsException, SubscriptionFailedException {
    user.setSubscriptionStatus(false);
    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(null);
    when(userRepository.save(any())).thenReturn(user);
    assertThrows(
        InvalidCredientialsException.class,
        () -> {
          userService.retrieveSubscription(userDto);
        });
  }

  @Test
  public void testRetrieveSubscriptionWithAlreadySubscribedUser()
      throws InvalidCredientialsException, SubscriptionFailedException {

    when(userRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);
    when(userRepository.save(any())).thenReturn(user);
    assertThrows(
        SubscriptionFailedException.class,
        () -> {
          userService.retrieveSubscription(userDto);
        });
  }

  @Test
  public void testGetAllUsers() throws UserNotFoundException {
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<User> users = userService.getAllUsers();
    assertEquals(users.size(), 1);
  }

  @Test
  public void testGetAllUsersWithUserTypeAsAdmin() throws UserNotFoundException {
    user.setType("Admin");
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<User> users = userService.getAllUsers();
    assertEquals(users.size(), 0);
  }

  @Test
  public void testGetAllUsersWhenNoUsersInDB() throws UserNotFoundException {
    when(userRepository.findAll()).thenReturn(null);
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.getAllUsers();
        });
  }

  @Test
  public void testGetDetailsOfAUser() throws UserNotFoundException {
    when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
    User retrievedUser = userService.getDetailsOfAUser(1);
    assertEquals(user, retrievedUser);
  }

  @Test
  public void testGetDetailsOfAUserWithNonExistingUserID() throws UserNotFoundException {
    when(userRepository.findById(11)).thenReturn(Optional.ofNullable(null));
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.getDetailsOfAUser(11);
        });
  }

  @Test
  public void testDeleteUserDetails() throws UserNotFoundException {
    when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
    doNothing().when(userRepository).deleteById(1);
    User deletedUser = userService.deleteUserDetails(1);
    assertEquals(user, deletedUser);
  }

  @Test
  public void testDeleteUserDetailsWithNonExistingUserID() throws UserNotFoundException {
    when(userRepository.findById(11)).thenReturn(Optional.ofNullable(null));
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.deleteUserDetails(11);
        });
  }

  @Test
  public void testUpdateUserDetails() throws UserNotFoundException {
    User userToUpdate =
        new User(1, "Sam", "sam@gmail.com", LocalDate.of(2001, 3, 27), "123678", true, "User");
    when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
    when(userRepository.save(any())).thenReturn(userToUpdate);
    User updatedUser = userService.updateUserDetails(userToUpdate);
    assertEquals(updatedUser, userToUpdate);
  }

  @Test
  public void testUpdateUserDetailsWithInvalidId() throws UserNotFoundException {
    User userToUpdate =
        new User(1, "Sam", "sam@gmail.com", LocalDate.of(2001, 3, 27), "123678", true, "User");
    when(userRepository.findById(11)).thenReturn(Optional.ofNullable(null));
    when(userRepository.save(any())).thenReturn(userToUpdate);
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.updateUserDetails(userToUpdate);
        });
  }

  @Test
  public void testUpdateUserDetailsWithNullValues() throws UserNotFoundException {
    user = new User(null, null, null, null, null, null, null);
    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
    when(userRepository.save(any())).thenReturn(user);
    User updatedUser = userService.updateUserDetails(user);
    assertEquals(updatedUser, user);
  }
}
