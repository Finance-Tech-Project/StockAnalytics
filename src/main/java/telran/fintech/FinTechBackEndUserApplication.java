package telran.fintech;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import telran.fintech.accounting.EmailService.EmailSenderService;

@SpringBootApplication
public class FinTechBackEndUserApplication {
	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(FinTechBackEndUserApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail() {

		senderService.sendEmail("shkribaev@gmail.com", "This is temporary password", UUID.randomUUID().toString());

	}
}
