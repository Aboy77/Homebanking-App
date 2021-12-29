package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AdminRepository adminRepository,ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Admin admin1 = new Admin("Brian", "Cuenca", "brian@admin.com", passwordEncoder.encode("1141"));

			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("Melba1"));
			Client cliente2 = new Client("Brian", "Cuenca", "correo@correo.com", passwordEncoder.encode("Aword7732#"));
			Account account1 = new Account("VIN001", LocalDate.now(), "5000", cliente1, AccountType.REGULAR, 2312312321L);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), "7500", cliente1, AccountType.SAVING, 3267656734L);
			Account account3 = new Account("Vin003", LocalDate.now(), "10000", cliente2, AccountType.REGULAR, 3415765654L);

			Transaction transaction1 = new Transaction(TransactionType.Credit, 3000, "BNB", LocalDate.now());
			Transaction transaction2 = new Transaction(TransactionType.Debit, 1000, "Donation", LocalDate.now());
			Transaction transaction3 = new Transaction(TransactionType.Credit, 2500, "BNB", LocalDate.now().plusDays(1));
			Transaction transaction4 = new Transaction(TransactionType.Credit, 1300, "Bolsa", LocalDate.now().plusDays(2));
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			Loan loan1 = new Loan("Hipoteca", 500000, List.of(12,24,36,48,60), 20);
			Loan loan2 = new Loan("Personal", 100000, List.of(6,12,24), 10);
			Loan loan3 = new Loan("Automotriz", 300000, List.of(6,12,24,36), 15);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60, cliente1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, cliente1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, cliente2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, cliente2, loan3);

			Card card1 = new Card(cliente1.getFirstName() + " " + cliente1.getLastName() ,CardType.DEBIT, CardColorType.GOLD, 3432123343234543321L, 231, LocalDate.now(), LocalDate.now().plusYears(-1), cliente1);
			Card card2 = new Card(cliente1.getFirstName() + " " + cliente1.getLastName() ,CardType.CREDIT, CardColorType.TITANIUM, 4543343323432123432L, 433, LocalDate.now(), LocalDate.now().plusYears(5), cliente1);
			Card card3 = new Card(cliente2.getFirstName() + " " + cliente2.getLastName() ,CardType.CREDIT, CardColorType.SILVER, 4144443323432113232L, 876, LocalDate.now(), LocalDate.now().plusYears(5), cliente2);

			account1.addCard(card1);
			account2.addCard(card2);
			account3.addCard(card3);

			// admins
			adminRepository.save(admin1);

			// clientes
			clientRepository.save(cliente1);
			clientRepository.save(cliente2);

			// cuentas
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			// transacciones
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			// loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};
	}
}
