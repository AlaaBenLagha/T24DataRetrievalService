package com.pfe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pfe.sInterface.T24ChequeInterface;

@SpringBootApplication
//@EnableScheduling									
public class T24DataRetrievalServiceApplication{ //implements CommandLineRunner
	
	@Autowired
	private T24ChequeInterface t24ChequeInterface;

	public static void main(String[] args) {
		SpringApplication.run(T24DataRetrievalServiceApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		t24ChequeInterface.readAndSaveJsonData();
//	    t24ChequeInterface.watchFileAndSaveJsonData();
//		
//	}

}
