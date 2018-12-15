package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author vian
 */
public class Client2 {
    Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
	Client2(){}
	void run()
	{
		try{
			//Membuat socket untuk koneksi ke server
			requestSocket = new Socket("localhost", 2004);
			System.out.println("Terkoneksi ke localhost port 2004");
			//membuat Output dan Input Stream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//Berkomunikasi dengan server
			do{
				try{
					message = (String)in.readObject();
					System.out.println("server>" + message);
					sendMessage("Tes Server");
					message = "tutup";
					sendMessage(message);
				}
				catch(ClassNotFoundException classNot){
					System.err.println("data diterima tidak diketahui");
				}
			}while(!message.equals("tutup"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("Anda mencoba menghubungkan ke host yang tidak diketahui!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
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
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Client2 client = new Client2();
		client.run();
	}
}
