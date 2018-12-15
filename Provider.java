package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vian
 */
public class Provider {
    ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Provider(){}
	void run()
	{
		try{
			// membuat server socket
			providerSocket = new ServerSocket(2004, 10);
			// Menunggu Koneksi
			System.out.println("Menunggu Koneksi");
			connection = providerSocket.accept();
			System.out.println("Koneksi Diterima dari " + connection.getInetAddress().getHostName());
			// membuat Output dan Input Stream
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Koneksi Sukses!");
			// Berkomunikasi melalui Input dan output Stream
			do{
				try{
					message = (String)in.readObject();
					System.out.println("client>" + message);
					if (message.equals("tutup"))
						sendMessage("tutup");
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data diterima dalam format yang tidak diketahui");
				}
			}while(!message.equals("tutup"));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				providerSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Provider server = new Provider();
		while(true){
			server.run();
		}
	}
}
