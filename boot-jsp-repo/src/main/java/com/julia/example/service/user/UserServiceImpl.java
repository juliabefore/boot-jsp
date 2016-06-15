package com.julia.example.service.user;

import com.julia.example.domain.UserCreateForm;
import com.julia.example.repository.UserRepository;
import com.julia.example.service.exception.UserAlreadyExistsException;
import com.julia.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private JavaMailSender javaMailService;

    @Override
    @Transactional
    public Optional<User> getUserById(long id) {
        LOGGER.debug("Getting user={}", id);
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    @Transactional
    public Optional<User> getUserByEmail(String email) {
        LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
        return userRepository.findOneByEmail(email);
    }

    @Override
    @Transactional
    public Collection<User> getAllUsers() {
        LOGGER.debug("Getting all users");
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    @Transactional
    public User create(UserCreateForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setRole(form.getRole());
        sendEmail(user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(UserCreateForm form, Long id) {
        User user = userRepository.findOne(id);
        user.setEmail(form.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        User user = userRepository.findOne(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User save(@NotNull @Valid final User user) {
        LOGGER.debug("Creating {}", user);
        User existing = userRepository.findOne(user.getId());

        if (existing != null) {
            throw new UserAlreadyExistsException(
                    String.format("There already exists a user with id=%s", user.getId()));
        }
        return userRepository.save(user);
    }

    public void sendEmail(User user){

        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Registration");
        mailMessage.setText("Hello " +user.getEmail() +"\n Your registration is successfull");
        javaMailService.send(mailMessage);

    }

//    public Iterable<User> findAll() {
//
//        return userRepository.findAll();
//    }

}
