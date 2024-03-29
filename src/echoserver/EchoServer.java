package echoserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    public static final int portNumber = 6013;
    // Start listening on the specified port
    public static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            // Create the executor service. Using an arbitrary maximum threads of 3.
            ExecutorService threadPool = Executors.newFixedThreadPool(3);

            serverSocket = new ServerSocket(portNumber);

            threadPool.execute(new ServerThread());

            threadPool.shutdown();
        }

        // Very minimal error handling.
        catch (IOException ioe) {
            System.out.println("We caught an unexpected exception");
            System.err.println(ioe);
        }
    }

    public static class ServerThread extends Thread {

        public void run() {
            // The client is sending bytes, represented as an int
            int c;

            // Run forever, which is common for server style services
            while (true) {

                // Wait until someone connects, and indicate this to the user.
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from client.");

                    // Grab the input/output streams so we can write/read directly to/from them
                    OutputStream os = clientSocket.getOutputStream();
                    DataInputStream is = new DataInputStream(clientSocket.getInputStream());

                    // While there is valid input to be read...
                    while ( (c = is.read()) != -1) {
                        // Write that input back into the output stream, and send it all immediately.
                        os.write(c);
                        os.flush();
                    }

                    // Close the streams/client socket since we're done.
                    System.out.println("Closing client connection");
                    os.close();
                    is.close();
                    clientSocket.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// model: https://introcs.cs.princeton.edu/java/84network/EchoServer.java.html