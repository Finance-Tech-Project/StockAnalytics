package com.stockanalytics.accounting.controller;

import java.security.Principal;

import com.stockanalytics.accounting.emailservice.EmailSenderService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.stockanalytics.accounting.dto.RolesDto;
import com.stockanalytics.accounting.dto.UserDto;
import com.stockanalytics.accounting.dto.UserEditDto;
import com.stockanalytics.accounting.dto.UserRegisterDto;
import com.stockanalytics.accounting.service.UserAccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userAccountService.register(userRegisterDto);
    }

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return userAccountService.getUser(principal.getName());
    }

    @PostMapping("/recovery/{login}")
    public void forgotPassword(@PathVariable String login) {
        userAccountService.sendTemporaryPassword(login);
    }

    @PostMapping("/message")
    String sendEmailMessage() {
        this.emailSenderService.sendEmail("Shkribaev@gmail.com", "Hello you send successfully", "this is temp password");
        return "Message is sent";
    }

        //  Endpoint to send a temporary password to the user's email account
   @PostMapping("/send-temporary-password/{login}")
   public ResponseEntity<String> sendTemporaryPassword(@PathVariable String login) {
        userAccountService.sendTemporaryPassword(login);
       return ResponseEntity.ok("Temporary password sent by email.");}
    @Transactional
    @PostMapping("/restore-password/{login}")
    public ResponseEntity<String> restorePassword(
            @PathVariable String login,
            @RequestParam String temporaryPassword,
            @RequestParam String newPassword
    ) {
        userAccountService.restorePassword(login, temporaryPassword, newPassword);
        return ResponseEntity.ok("The password has been successfully restored.");
    }
    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        System.out.println("in controller getUser");
        return userAccountService.getUser(login);
    }

    @DeleteMapping("/removeUser/{login}")
    public UserDto removeUser(@PathVariable String login) {
        return userAccountService.removeUser(login);
    }

    @PutMapping("/user/update/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UserEditDto userEditDto) {
        return userAccountService.updateUser(login, userEditDto);
    }

    @PutMapping("/user/{login}/role/{role}")
    public RolesDto addRole(@PathVariable String login, @PathVariable String role) {
        return userAccountService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public RolesDto deleteRole(@PathVariable String login, @PathVariable String role) {
        return userAccountService.changeRolesList(login, role, false);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@NonNull Principal principal, @RequestHeader("X-Password") String newPassword) {
        userAccountService.changePassword(principal.getName(), newPassword);
    }

    @GetMapping("/recovery/{login}")
    public String getPasswordLink(@PathVariable String login) {
        return userAccountService.getPasswordLink(login);
    }
}
