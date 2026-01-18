import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    // function inteface since arguement main pass kar payage

    public Consumer<Socket> getConsumer() throws IOException,Exception{
        return (clientSocket)->{
            try{
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
                toClient.println("Hello from the server");
                clientSocket.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        };
    }



    public static void main(String[] args) throws Exception{
        int port = 8010;
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
            System.out.println("Server is listening on port "+ port);
            while(true){
                Socket acceptedSocket = serverSocket.accept();
                Thread thread = new Thread(() -> {
                    try {
                        server.getConsumer().accept(acceptedSocket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();
            }


        }
        catch(IOException ex){
                ex.printStackTrace();
        }

    }
}
