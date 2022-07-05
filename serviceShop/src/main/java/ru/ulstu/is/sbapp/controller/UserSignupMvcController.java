package ru.ulstu.is.sbapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ulstu.is.sbapp.dto.UserSignupDto;
import ru.ulstu.is.sbapp.model.User;
import ru.ulstu.is.sbapp.service.UserService;
import ru.ulstu.is.sbapp.util.validation.ValidationException;

import javax.validation.Valid;

@Controller
@RequestMapping(UserSignupMvcController.SIGNUP_URL)
public class UserSignupMvcController {

    public static final String SIGNUP_URL = "/signup";

    private final UserService userService;

    public UserSignupMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserSignupDto());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("userDto") @Valid UserSignupDto userSignupDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "signup";
        }
        try {
            final User user = userService.createUser(userSignupDto.getLogin(), userSignupDto.getPassword(), userSignupDto.getFirstName(), userSignupDto.getLastName(), userSignupDto.getPasswordConfirm());
            return "redirect:/login?created=" + user.getLogin();
        } catch (ValidationException e) {
            model.addAttribute("errors", e.getMessage());
            return "signup";
        }
    }
}
