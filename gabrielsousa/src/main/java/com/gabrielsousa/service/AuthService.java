package com.gabrielsousa.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabrielsousa.domain.Client;
import com.gabrielsousa.repository.ClientRepository;
import com.gabrielsousa.service.exception.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		
		Client client = clientRepository.findByEmail(email);
		if(client==null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		client.setPassword(bCryptPasswordEncoder.encode(newPass));
		
		clientRepository.save(client);
		emailService.sendNewPasswordEmail(client, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = random.nextInt(3);
		if(opt == 0) {
			//Gera um dígito
			return (char) (random.nextInt(10)+48);
		}
		else if(opt==1) {
			//Gera letra maiúscula
			return (char) (random.nextInt(26)+65);
		}
		else {
			//Gera letra minúscula
			return (char) (random.nextInt(26)+97);
		}
		
	}
}
