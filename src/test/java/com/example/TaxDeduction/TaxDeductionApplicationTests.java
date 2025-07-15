package com.example.TaxDeduction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TaxDeductionApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void testApplicationContextLoads() {
		// ตรวจสอบว่า application context โหลดได้
		assertNotNull(context, "Application context should not be null");
	}
}