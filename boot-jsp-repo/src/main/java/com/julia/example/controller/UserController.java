package com.julia.example.controller;

import com.julia.example.domain.UserCreateForm;
import com.julia.example.domain.validator.UserCreateFormValidator;
import com.julia.example.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;

    //@Autowired
    @Inject
    public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        LOGGER.debug("Getting user page for user={}", id);
        return new ModelAndView("user", "user", userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id))));
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        LOGGER.debug("Getting user create form");
        return new ModelAndView("user_create", "form", new UserCreateForm());
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_create";
        }
        try {
            userService.create(form);
            //userService.save(new User(form.getEmail(), form.getPasswordRepeated(), Role.USER));
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        // ok, redirect
        //return "redirect:/users";
        return "redirect:/";
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping(value = "/user/{id}/update", method = RequestMethod.GET)
    public ModelAndView getUserUpdatePage(@PathVariable Long id) {
        LOGGER.debug("Getting user update page for user={}", id);
        return new ModelAndView("user_update", "form", new UserCreateForm());
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping(value = "/user/{id}/update", method = RequestMethod.POST)
    public String handleUserUpdateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult, @PathVariable Long id) {
        LOGGER.debug("Processing user update form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_update";
        }
        try {
            userService.update(form, id);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.reject("email.exists", "Email already exists");
            return "user_update";
        }
        // ok, redirect
        return "redirect:/";
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping(value = "/user/{id}/delete", method = RequestMethod.GET)
    public ModelAndView getUserRemovePage(@PathVariable Long id) {
        LOGGER.debug("Getting user remove page for user={}", id);
        return new ModelAndView("user_delete", "form", new UserCreateForm());
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping(value = "/user/{id}/delete", method = RequestMethod.POST)
    public String handleUserRemoveForm(@PathVariable Long id) {
        LOGGER.debug("Processing user remove form={}", id);
        userService.remove(id);
        return "redirect:/";
    }

}
