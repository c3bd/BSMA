package edu.ecnu.imc.bsma;

import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
	private AtomicInteger jobID = new AtomicInteger(0);
	private AtomicInteger subJobID = new AtomicInteger(0);
}
