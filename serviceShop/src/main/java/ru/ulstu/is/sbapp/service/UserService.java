package ru.ulstu.is.sbapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ulstu.is.sbapp.model.Order;
import ru.ulstu.is.sbapp.model.User;
import ru.ulstu.is.sbapp.model.UserRole;
import ru.ulstu.is.sbapp.repository.UserRepository;
import ru.ulstu.is.sbapp.util.validation.ValidationException;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ValidatorUtil validatorUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ValidatorUtil validatorUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
    }

    public String findLoggedInUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Page<User> findAllPages(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size, Sort.by("id").ascending()));
    }

    public User findByLogin(String login) {
        return userRepository.findOneByLoginIgnoreCase(login);
    }

    public User createUser(String login, String password, String firstName, String lastName, String passwordConfirm) {
        return createUser(login, password, firstName, lastName, passwordConfirm, UserRole.CUSTOMER);
    }

    public User createUser(String login, String password, String firstName, String lastName, String passwordConfirm, UserRole role) {
        if (findByLogin(login) != null) {
            throw new ValidationException(Collections.singleton(String.format("Пользователь '%s' уже существует", login)));
        }
        final User user = new User(login, passwordEncoder.encode(password), firstName, lastName, role);
        validatorUtil.validate(user);
        if (!Objects.equals(password, passwordConfirm)) {
            throw new ValidationException(Collections.singleton("Пароли не одинаковы"));
        }
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByLogin(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getLogin(), userEntity.getPassword(), Collections.singleton(userEntity.getRole()));
    }
}
