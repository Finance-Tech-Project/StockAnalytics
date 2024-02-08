package com.stockanalytics.accounting.service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.stockanalytics.portfolio.dao.PortfolioRepository;
import com.stockanalytics.portfolio.model.Portfolio;
import org.apache.tomcat.util.bcel.classfile.ClassFormatException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.stockanalytics.accounting.emailservice.EmailSenderService;
import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.dto.RolesDto;
import com.stockanalytics.accounting.dto.UserDto;
import com.stockanalytics.accounting.dto.UserEditDto;
import com.stockanalytics.accounting.dto.UserRegisterDto;
import com.stockanalytics.accounting.dto.exceptions.UserExistsException;
import com.stockanalytics.accounting.dto.exceptions.UserNotFoundException;
import com.stockanalytics.accounting.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;

@SuppressWarnings("CallToPrintStackTrace")
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    final EmailSenderService emailSenderService;
    final PortfolioRepository portfolioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$");

    private boolean validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            logger.error("Email {} does not match the format example@gmail.com", email);
            throw new ClassFormatException("Invalid email format");
        }
        return true;
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            logger.error("Login {} does not match the format example@gmail.com", email);
            throw new ClassFormatException("сexample@gmail.com");
        }
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            logger.error("User with login {} already exists", userRegisterDto.getLogin());
            throw new UserExistsException(userRegisterDto.getLogin());
        }

        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        userAccount.addRole("USER");

        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);

        logger.info("User {} has been successfully registered", userRegisterDto.getLogin());
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public void sendTemporaryPassword(String login) {
        String tempPassword = UUID.randomUUID().toString();
        UserDto userDto = getUser(login);
        String toEmail = userDto.getEmail();
        try {
            emailSenderService.sendEmail(toEmail, "This is temporary password", tempPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPasswordLink(String login) {
        String tempPassword = UUID.randomUUID().toString();
        UserDto userDto = getUser(login);
        String toEmail = userDto.getEmail();
        try {
            emailSenderService.sendEmail(toEmail, "This is temporary password", tempPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempPassword;
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto removeUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        List<Portfolio> userPortfolios = portfolioRepository.findByUserLogin(userAccount);
        if (!userPortfolios.isEmpty()) {
            portfolioRepository.deleteAllByUserLogin(userAccount);
        }
        userAccountRepository.deleteById(login);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (userEditDto.getFirstName() != null && !userEditDto.getFirstName().isEmpty()) {
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if (userEditDto.getLastName() != null && !userEditDto.getLastName().isEmpty()) {
            userAccount.setLastName(userEditDto.getLastName());
        }
        if (userEditDto.getEmail() != null && !userEditDto.getEmail().isEmpty()) {
            if (validateEmail(userEditDto.getEmail())) {
                userAccount.setEmail(userEditDto.getEmail());
            }
        }
        if (userEditDto.getPassword() != null && !userEditDto.getPassword().isEmpty()) {
            String password = passwordEncoder.encode(userEditDto.getPassword());
            userAccount.setPassword(password);
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        System.out.println("in service" + userAccount.getRole());

        if (isAddRole) {
            userAccount.addRole(role.toUpperCase());
        } else {
            userAccount.removeRole();
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void run(String... args) {
        if (!userAccountRepository.existsById("admin")) {
            String password = BCrypt.hashpw("admin", BCrypt.gensalt());
            UserAccount userAccount = new UserAccount("admin", password, "", "", "");
            userAccountRepository.save(userAccount);
        }
    }
}