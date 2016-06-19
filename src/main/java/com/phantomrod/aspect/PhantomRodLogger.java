package com.phantomrod.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PhantomRodLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger("PhantomRod");
	private static final boolean timeLogs = true;
	private static AtomicLong id = new AtomicLong(0);
	
	@Around("execution (* com.phantomrod..*.*(..))")
	public Object beforeMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		//time the method execution:
		long beginning = System.nanoTime();
		Object result = joinPoint.proceed();
		long elapsed = (System.nanoTime() - beginning) / 1000000; // in millis
		
		//get the method's meta-data
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = MethodSignature.class.cast(joinPoint.getSignature()).getMethod().getName();
		
		//casts the argument object array to string before joining every value with commas
		String args = Arrays.stream(joinPoint.getArgs()).map((o) -> o+"").collect(Collectors.joining(", "));
		
		//log the method's meta data and its execution time
		LOGGER.info("{}: {}.{}({}) - {}ms", id.getAndIncrement(),  className, methodName, args, elapsed);
		
		//record the method execution time
		if (timeLogs)
			addTime(className + "." + methodName, elapsed);
		
		return result;
	}
	
	//logs a method call time
	private static Map<String, List<Long>> methodTimes = Collections.synchronizedMap(new HashMap<String, List<Long>>());
	private static void addTime(String key, long time) {
		methodTimes.putIfAbsent(key, Collections.synchronizedList(new ArrayList<Long>()));
		
		methodTimes.get(key).add(time);
	}
	
	//aspect at the end of the loop() service to log the times
	@After("execution (* com.phantomrod.service.Looper.loop())")
	public void logTimes() {
		if (!timeLogs) 
			return;
		
		List<Long> times;
		double average;
		long maximum;
		long minimum;
		for (String methodId : methodTimes.keySet()) {
			times = methodTimes.get(methodId);
			average = times.stream().collect(Collectors.averagingDouble((number) -> Double.valueOf(number)));
			maximum = times.stream().max((x,y) -> Long.compare(x, y)).get();
			minimum = times.stream().min((x,y) -> Long.compare(x, y)).get();
			
			LOGGER.info("{} - average: {}ms, max: {}ms, min: {}ms", methodId, average, maximum, minimum);
		}
	}
}
