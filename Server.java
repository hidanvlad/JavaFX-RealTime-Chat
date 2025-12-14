import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            // Infinite loop: Keep the server running forever
            while (!serverSocket.isClosed()) {

                // 1. Wait for a new guest
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // 2. Create a handler for them
                ClientHandler clientHandler = new ClientHandler(socket);

                // 3. Start that handler in a separate Thread
                // This allows the Server loop to go back to line 1 immediately!
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234); // Port 1234
        Server server = new Server(serverSocket);
        server.startServer();
    }
}