package telran.fintech.accounting.service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.tomcat.util.bcel.classfile.ClassFormatException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.fintech.accounting.EmailService.EmailSenderService;
import telran.fintech.accounting.dao.UserAccountRepository;
import telran.fintech.accounting.dto.RolesDto;
import telran.fintech.accounting.dto.UserDto;
import telran.fintech.accounting.dto.UserEditDto;
import telran.fintech.accounting.dto.UserRegisterDto;
import telran.fintech.accounting.dto.exceptions.UserExistsException;
import telran.fintech.accounting.dto.exceptions.UserNotFoundException;
import telran.fintech.accounting.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;
	final EmailSenderService emailSenderService;
	private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
	   private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@gmail\\.com$");
	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		  String email = userRegisterDto.getEmail();
	if (!EMAIL_PATTERN.matcher(email).matches()) {
           logger.error("Логин {} не соответствует формату example@gmail.com",email);
            throw new ClassFormatException("Логин должен иметь формат example@gmail.com");
        }
				if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
        logger.error("Пользователь с логином {} уже существует", userRegisterDto.getLogin());
        throw new UserExistsException();
   }
		
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.addRole("USER");
		String password = passwordEncoder.encode(userRegisterDto.getPassword());
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
		 logger.info("Пользователь {} успешно зарегистрирован", userRegisterDto.getLogin());
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
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		userAccountRepository.deleteById(login);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}
		if (userEditDto.getEmail() != null) {
			userAccount.setEmail(userEditDto.getEmail());
		}
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role.toUpperCase());
		} else {
			res = userAccount.removeRole(role.toUpperCase());
		}
		if (res) {
			userAccountRepository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String password = passwordEncoder.encode(newPassword);
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);

	}

}
