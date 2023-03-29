import java.awt.*;
import java.io.*;
import java.net.*;

public class PingResponder implements Runnable {
    private final int port;
    private final String[] ipAddresses;
    private final String websiteUrl;

    public PingResponder(int port, String[] ipAddresses, String websiteUrl) {
        this.port = port;
        this.ipAddresses = ipAddresses;
        this.websiteUrl = websiteUrl;
    }


    public static void main(String[] args) {
        String[] ipAddresses = {"192.168.0.152", "192.168.0.119", "192.168.0.3", "192.0.0.4", "172.19.183.46", "192.168.0.167"};
        int port = 8080;
        String websiteUrl = "https://projectai.me/";
        PingResponder pingResponder = new PingResponder(port, ipAddresses, websiteUrl);
        Thread thread = new Thread(pingResponder);
        thread.start();
        // Send pings to each IP address
        for (String ipAddress : ipAddresses) {
            try {
                InetAddress address = InetAddress.getByName(ipAddress);
                System.out.println("Sending ping to " + address.getHostAddress());
                PingSender.sendPing(address);
            } catch (UnknownHostException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("PingResponder listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine = in.readLine();
                System.out.println("Received ping from " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);
                clientSocket.close();

                // Send website URL to each IP address
                for (String ipAddress : ipAddresses) {
                    try {
                        InetAddress address = InetAddress.getByName(ipAddress);
                        System.out.println("Sending website URL " + websiteUrl + " to " + address.getHostAddress());
                        PingSender.sendWebsiteURL(address, websiteUrl);
                    } catch (UnknownHostException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
}
}
}
} catch (IOException e) {
System.out.println("Error: " + e.getMessage());
}
}
}




class PingSender {
    public static void sendPing(InetAddress address) throws IOException {
        // Construct the ping request packet
        byte[] buf = new byte[32];
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 7);

        // Send the ping request packet
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
    }


public static void sendWebsiteURL(InetAddress address, String websiteUrl) throws IOException {
    Socket socket = new Socket(address, 80);
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println("GET " + websiteUrl + " HTTP/1.1\r\n");
    out.println("Host: " + address.getHostAddress() + "\r\n");
    out.println("Connection: Close\r\n");
    out.println("\r\n");
    out.flush();
    socket.close();
}
}
