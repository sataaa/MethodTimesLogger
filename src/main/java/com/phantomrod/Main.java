package com.phantomrod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.phantomrod.configuration.Config;
import com.phantomrod.model.Foo;
import com.phantomrod.service.Looper;

public class Main {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger("Main");
		try (AbstractApplicationContext context = new AnnotationConfigApplicationContext(Config.class)) {
			
			logger.info("Started run!");
			
			Looper loop = (Looper) context.getBean(Looper.class);
			
			loop.loop();
			
			Foo foo = new Foo();
			foo.bar();
		} catch (InterruptedException e) {
			logger.info(e.getMessage());
		}
	}

}
