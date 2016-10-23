package me.lipu.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class CloseUtil {
	public static void closeSocket(ServerSocket server) {
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			System.out.println("server socket close error!!!");
		}
	}

	public static void closeSocket(Socket socket) {

		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("socket close error!!!");
		}
	}
	
	public static void closeSocket(DatagramSocket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			System.out.println("DatagramSocket close error!!!");
		}
	}
	
	public static <T extends Closeable> void closeIO(T... io){
		for(Closeable temp:io) {
			try {
				if (temp != null) {
					temp.close();
				}
			} catch (Exception e) {
				System.out.println("IO stream close error!!!");
			
		}
	}
	}
}
