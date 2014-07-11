package edu.ecnu.imc.bsma.httpserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class BSMAHttpServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		// HTTP connector
		/*
		 * ServerConnector http = new ServerConnector(server);
		 * http.setHost("localhost"); http.setPort(8080);
		 * http.setIdleTimeout(30000); // Set the connector
		 * server.addConnector(http);
		 */

		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new HelloServlet()), "/dump/*");

		server.setHandler(context);
		// server.setHandler(new HelloHandler());
		server.start();
		server.join();
	}

	@SuppressWarnings("serial")
	public static class HelloServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Hello SimpleServlet</h1>");
		}
	}
}
