package com.gabrielsousa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GabrielsousaApplication implements CommandLineRunner {

//	@Autowired
//	private S3Service s3Service;

//	@Autowired
//	private ClientRepository clientRepository;

	public static void main(String[] args) {
		SpringApplication.run(GabrielsousaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		s3Service.uploadFile("C:\\Users\\gabri\\Downloads\\Gabriel_Barros.jpg");
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		Client cli1 = new Client(null,"Gabriel Barros","gabriel.brs.gsousa@gmail.com","04912788104", 1);
//		Client cli2 = new Client(null,"Matheus Barros","matheus@gmail.com","04912788184", 2);
//		
//		clientRepository.save((cli1));
//		clientRepository.save((cli2));
//	}

}
