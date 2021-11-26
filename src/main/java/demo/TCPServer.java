package demo;

import java.io.IOException;
import java.net.ServerSocket;
import tcp.SocketHandler;

public class TCPServer {
	private static final int PORT = 8989;
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(PORT);
		System.out.println("Server start ...");
		while(true) {
			System.out.println("Receiving ...");
			new SocketHandler(server.accept()).start();
		}
	}
}
