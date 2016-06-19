package com.phantomrod.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class Foo {
	public int bar() {
		return 1;
	}
	public int bar(int i) {
		return i+1;
	}
	public void baz(BigDecimal num, String lero, Timestamp time) {
		System.out.println("baz!");
	}
}
