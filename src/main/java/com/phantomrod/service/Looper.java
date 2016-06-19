package com.phantomrod.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phantomrod.model.Foo;

@Service
public class Looper {
	@Autowired
	Foo foo;
	
	public void loop() throws InterruptedException {
		foo.bar();
		foo.bar(289);
		
		foo.baz(null, "Sheit", null);
		foo.baz(new BigDecimal(1f/3), null, new Timestamp(new Date().getTime()));

		for (int i = 0; i < 2; i++) {
			System.out.println((i+1) + " - Leros");
			Thread.sleep(3000);
		}
	}
}
