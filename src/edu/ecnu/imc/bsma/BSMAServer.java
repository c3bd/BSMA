package edu.ecnu.imc.bsma;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import rpc.BSMAService;

public class BSMAServer {
	public static void main(String[] args) throws TTransportException {
		Scheduler handler = Scheduler.instance;
		handler.init();
		BSMAService.Processor processor = new BSMAService.Processor(handler);
		TServerTransport serverTransport = new TServerSocket(10010);
		// Use this for a multithreaded server
		TServer masterTServer = new TThreadPoolServer(
				new TThreadPoolServer.Args(serverTransport)
						.processor(processor));

		System.out.println("Starting the simple server...");
		masterTServer.serve();
	}
}
