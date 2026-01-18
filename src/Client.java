import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public Runnable getRunnable(int id) {
        return () -> {
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                Socket socket = new Socket(address, port);

                try (
                        PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    // Send a message to the server
                    toSocket.println("Hello from Client #" + id + " " + socket.getLocalSocketAddress());

                    // Read response from server
                    String line = fromSocket.readLine();
                    System.out.println("Response to Client #" + id + ": " + line);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        Client client = new Client();
        int totalRequests = 1000;

        // Use a thread pool instead of raw threads
        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= totalRequests; i++) {
            executor.submit(client.getRunnable(i));
        }

        executor.shutdown();
    }
}
