package com.galaxe.serviceImpl;

import com.galaxe.advice.DuplicateEmailException;
import com.galaxe.advice.InvalidCredientialsException;
import com.galaxe.advice.LoginFailedException;
import com.galaxe.advice.SubscriptionFailedException;
import com.galaxe.advice.UserNotFoundException;
import com.galaxe.dto.UserDto;
import com.galaxe.entities.User;
import com.galaxe.repository.UserRepository;
import com.galaxe.service.UserService;
import com.galaxe.utils.Constants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepo;

  @Autowired private JavaMailSender sender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Autowired private Configuration config;

  public void sendEmail(String toEmail, String subject, Map<String, Object> model) {
    MimeMessage message = sender.createMimeMessage();

    if (subject.equalsIgnoreCase("Subscibed sucessfully")) {

      try {
        MimeMessageHelper helper =
            new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        Template t = config.getTemplate("Subscription-Email-Template.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(toEmail);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        sender.send(message);

      } catch (MessagingException | IOException | TemplateException e) {

      }

    } else {
      try {
        MimeMessageHelper helper =
            new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        Template t = config.getTemplate("Unsubscription-Email-Template.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(toEmail);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        sender.send(message);

      } catch (MessagingException | IOException | TemplateException e) {

      }
    }
  }

  /**
   * Method to subscribe a new user.
   *
   * @param userDto The user data transfer object containing user information.
   * @return The newly subscribed user.
   * @throws DuplicateEmailException If a user with the same email already exists.
   */
  @Override
  public User subscribeUser(UserDto userDto) throws DuplicateEmailException {
    Optional<User> optionalUser = Optional.ofNullable(userRepo.findByEmail(userDto.getEmail()));
    if (optionalUser.isPresent())
      throw new DuplicateEmailException(Constants.DuplicateEmailExceptionMsg);

    User user =
        User.builder()
            .name(userDto.getName())
            .email(userDto.getEmail())
            .dob(userDto.getDob())
            .password(userDto.getPassword())
            .build();

    user.setSubscriptionStatus(Constants.SubscrptionStausTrue);
    user.setType("User");
    User savedUser = userRepo.save(user);
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("name", savedUser.getName());
    model.put("msg", "You are subscribed successfully");
    sendEmail(savedUser.getEmail(), "Subscibed sucessfully", model);
    return savedUser;
  }

  /**
   * Method to login a user.
   *
   * @param userDto The user data transfer object containing user login credentials.
   * @return The logged in user.
   * @throws InvalidCredentialsException If the provided credentials are invalid.
   */
  @Override
  public User loginUser(UserDto userDto) throws InvalidCredientialsException, LoginFailedException {
    Optional<User> optionalUser =
        Optional.ofNullable(
            userRepo.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword()));

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.getSubscriptionStatus() == false)
        throw new LoginFailedException(
            "Log in failed because you are unsubscribed!! \n Please retrive you subscription");

      return user;
    } else throw new InvalidCredientialsException(Constants.InvalidCredentialsExceptionMSg);
  }

  /**
   * Method to cancel subscription of a user.
   *
   * @param userId The ID of the user whose subscription needs to be cancelled.
   * @return The updated user with subscription cancelled.
   * @throws UserNotFoundException If no user is found with the given ID.
   */
  @Override
  public User cancelSubscription(String email) throws UserNotFoundException {
    Optional<User> optionalUser = Optional.ofNullable(userRepo.findByEmail(email));

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setSubscriptionStatus(Constants.SubscrptionStausFalse);
      User updatedUser = userRepo.save(user);
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("name", updatedUser.getName());
      model.put("msg", "You are Usubscribed successfully");
      sendEmail(updatedUser.getEmail(), "Unsubscibed sucessfully", model);
      return updatedUser;
    } else throw new UserNotFoundException(Constants.UserNotFoundExceptionMsg);
  }

  /**
   * Method to retrieve subscription of a user.
   *
   * @param userDto The user data transfer object containing user login credentials.
   * @return The updated user with subscription retrieved.
   * @throws InvalidCredentialsException If the provided credentials are invalid.
   * @throws SubscriptionFailedException If subscription retrieval fails due to already subscribed
   *     status.
   */
  @Override
  public User retrieveSubscription(UserDto userDto)
      throws InvalidCredientialsException, SubscriptionFailedException {
    Optional<User> optionalUser =
        Optional.ofNullable(
            userRepo.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword()));
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.getSubscriptionStatus() == false) {
        user.setSubscriptionStatus(true);
        User updatedUser = userRepo.save(user);
        return updatedUser;
      } else {
        throw new SubscriptionFailedException(
            "Subscription is not retrived beacause you are already subscribed");
      }

    } else throw new InvalidCredientialsException(Constants.InvalidCredentialsExceptionMSg);
  }

  /**
   * Method to get details of all users.
   *
   * @return List of all users.
   * @throws UserNotFoundException If no users are found in the database.
   */
  @Override
  public List<User> getAllUsers() throws UserNotFoundException {
    Optional<List<User>> optionalUsers = Optional.ofNullable(userRepo.findAll());
    if (optionalUsers.isPresent()) {
      List<User> allUsers = optionalUsers.get();
      List<User> usersList = new ArrayList<User>();
      for (User user : allUsers) {
        if (user.getType().equalsIgnoreCase("User")) usersList.add(user);
      }
      return usersList;
    } else throw new UserNotFoundException("No users are available in DB");
  }

  /**
   * Method to get details of a specific user by ID.
   *
   * @param userId The ID of the user to retrieve details.
   * @return Details of the specified user.
   * @throws UserNotFoundException If no user is found with the given ID.
   */
  @Override
  public User getDetailsOfAUser(Integer userId) throws UserNotFoundException {
    Optional<User> optionalUser = userRepo.findById(userId);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      return user;
    } else throw new UserNotFoundException("No User is found with given Id");
  }

  /**
   * Method to delete details of a specific user by ID.
   *
   * @param userId The ID of the user to delete.
   * @return Details of the deleted user.
   * @throws UserNotFoundException If no user is found with the given ID.
   */
  @Override
  public User deleteUserDetails(Integer userId) throws UserNotFoundException {
    Optional<User> optionalUser = userRepo.findById(userId);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      userRepo.deleteById(userId);
      return user;
    } else throw new UserNotFoundException("No User is found with given Id");
  }

  /**
   * Method to update details of a specific user.
   *
   * @param user The user object containing updated details.
   * @return The updated user details.
   * @throws UserNotFoundException If no user is found with the given ID.
   */
  @Override
  public User updateUserDetails(User user) throws UserNotFoundException {
    Optional<User> optionalUser = userRepo.findById(user.getUserId());
    if (optionalUser.isPresent()) {
      User tempUser = optionalUser.get();
      if (user.getName() != null) tempUser.setName(user.getName());
      if (user.getDob() != null) tempUser.setDob(user.getDob());
      if (user.getEmail() != null) tempUser.setEmail(user.getEmail());
      if (user.getPassword() != null) tempUser.setPassword(user.getPassword());
      User updatedUser = userRepo.save(tempUser);
      return updatedUser;

    } else throw new UserNotFoundException("No User is found with given Id");
  }
}
