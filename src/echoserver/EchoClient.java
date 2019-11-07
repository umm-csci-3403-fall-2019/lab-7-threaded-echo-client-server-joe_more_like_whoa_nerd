package echoserver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {

    public static final int portNumber = 6013;

    public static void main(String[] args) throws IOException{
        EchoClient client = new EchoClient();
        client.start();
    }

    private void start() throws IOException {
        // Establish connection with the server on the specified portnumber
        Socket echoSocket = new Socket("localhost", portNumber);

        ToServerThread t = new ToServerThread(echoSocket);
        FromServerThread f = new FromServerThread(echoSocket);

        Thread toThread = new Thread(t);
        Thread fromThread = new Thread(f);

        toThread.start();
        fromThread.start();
    }

    class ToServerThread implements Runnable {
        int c;

        private Socket output;
        public ToServerThread(Socket input) {
            output = input;
        }

        public void run() {
            try {
                OutputStream os = output.getOutputStream();
                while ((c = System.in.read()) != -1) {
                    os.write(c);
                }

                os.flush();
                output.shutdownOutput();
            }
            // Minimal exception handling
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static class FromServerThread implements Runnable {
        int responseByte;

        private Socket input;
        public FromServerThread(Socket socket) {
            input = socket;
        }

        public void run() {
            try {
                InputStream is = input.getInputStream();
                while ((responseByte = is.read()) != -1) {
                    System.out.write(responseByte);
                }
                System.out.flush();
                input.shutdownInput();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




// model: https://introcs.cs.princeton.edu/java/84network/EchoClient.java.html