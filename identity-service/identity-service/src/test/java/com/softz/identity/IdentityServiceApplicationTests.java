package com.softz.identity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.softz.identity.entity.User;
import com.softz.identity.utils.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class IdentityServiceApplicationTests {

	// @Autowired
	// private JwtService jwtService;

	// String generateToken(){
	// 	return jwtService.generateToken(User.builder().username("thuan").password("thuan").build());
	// }
	// @Test
	// void testGenerateToken(){
	// 	log.info("Token: {}", generateToken());
	// }

	// @Test
	// void testVerifyToken(){
	// 	log.info("VerifyToken: {}", jwtService.verifyToken(generateToken()));
	// }
}
