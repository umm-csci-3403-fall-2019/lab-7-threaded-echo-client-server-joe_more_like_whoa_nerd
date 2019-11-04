package echoserver;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {

    public static InputStream is;
    public static OutputStream os;
    public static Socket echoSocket;
    public static final int portNumber = 6013;

    public static class ToServerThread extends Thread {
        int c;
        public void run() {
            try {
                while ((c = System.in.read()) != -1) {
                    os.write(c);
                    os.flush();
                }

                System.out.println("toServerThread: Shutting down echoSocket....");
                echoSocket.shutdownOutput();
            }
            // Minimal exception handling
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class FromServerThread extends Thread {
        int responseByte;
        public void run() {
            try {
                while ((responseByte = is.read()) != -1) {
                    System.out.write(responseByte);
                    System.out.flush();
                }
                System.out.println("fromServerThread: closing echoSocket now.");
                echoSocket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        String server;

        // Use "127.0.0.1", i.e., localhost, if no server is specified.
        if (args.length == 0) {
            server = "127.0.0.1";
        } else {
            server = args[0];
            server = args[0];
        }


        // Be prepared to handle any general exception
        try {
            // Establish connection with the server on the specified portnumber
            Socket echoSocket = new Socket(server, portNumber);

            // Grab the input/output streams so we can write/read directly to/from them
            OutputStream os = echoSocket.getOutputStream();
            InputStream is = echoSocket.getInputStream();

            new ToServerThread().start();
            new FromServerThread().start();

//
//            // While there is a valid input to be received from System.in...
//            while ( (c = System.in.read()) != -1) {
//
//                // Write it and send it all together, immediately
//                os.write(c);
//                os.flush();
//
//                // Read the response and response and output it all immediately.
//                responseByte = is.read();
//                System.out.write(responseByte);
//                System.out.flush();
//            }
//
//            // Feedback for the user. This is commented out so the tests pass.
////            System.out.println("Time to close.");
//
//            // Close everything when we are done.
//            os.close();
//            is.close();
//            echoSocket.close();
        }
//
            // Very minimal error handling.
        catch(Exception e){
                System.err.println("Exception:  " + e);
            }
        }
    }




// model: https://introcs.cs.princeton.edu/java/84network/EchoClient.java.html