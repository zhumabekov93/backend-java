package com.maputo.maputo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

import static com.maputo.maputo.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class MaputoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaputoApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
