package me.lipu.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.lipu.util.CloseUtil;

public class Server {
	private ServerSocket server;
	boolean isTurnedOn = true;
	private ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	public void start() {
		start(9999);
		
	}

	public void start(int port) {
		WebApp.getLogger().info("server for port " + port + "isStarting..." );
		try {
			server = new ServerSocket(port);
			this.receive();
		} catch (IOException e) {
			this.stop();
		}
	}

	public void receive() {
		try {
			WebApp.getLogger().info("server is ready for receiving request." );
			while (isTurnedOn) {
				new Thread(new Dispatcher(server.accept())).start();
//				Socket socket = server.accept();
//				pool.submit(new Dispatcher(socket));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			stop();
		}
	}

	public void stop() {
		isTurnedOn = false;
		CloseUtil.closeSocket(server);
		WebApp.getLogger().info("server is closed." );
	}
}
