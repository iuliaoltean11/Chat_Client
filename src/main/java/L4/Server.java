package L4;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.UUID;

public class Server {
    ArrayList<Socket> clientSockets;
    ArrayList<String> usernames = new ArrayList<>();

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("received: " + message);
                    if (message.contains("#new_username")) {
                        String[] messageSplit = message.split(" ");
                        if (usernames.contains(messageSplit[1])) {
                            sendToSomeone(sock, "Server", "error: name exists");
                        } else {
                            clientSockets.add(this.sock);
                            usernames.add(messageSplit[1]);
                            updateClientList(sock);
                        }

                    } else if (message.contains("logout")) {
                        String senderName = usernames.get(clientSockets.indexOf(this.sock));
                        logoutClient(senderName);
                    }
                    //B->A
                    else {
                        String[] messageSplit = message.split(":");
                        //messagesplit = [alex, ceau]
                        Socket destinationSocket = clientSockets.get(usernames.indexOf(messageSplit[0]));
                        String senderName = usernames.get(clientSockets.indexOf(this.sock));
                        sendToSomeone(destinationSocket, senderName, messageSplit[1]);
                    }

                    // tellEveryone(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void sendToSomeone(Socket socket, String senderName, String message) {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(senderName + ":" + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void updateClientList(Socket sender) {
            String updateClients = "#new_username ";
            String newClient = "#new_list ";
            PrintWriter printWriter;
            for (String name : usernames) {
                newClient += "@" + name;
            }
            int index = clientSockets.indexOf(sender);
            updateClients += "@" + usernames.get(index);
            for (Socket socket : clientSockets) {
                try {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    if (socket == sender) {
                        printWriter.println(newClient);
                    } else {
                        printWriter.println(updateClients);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    public void logoutClient(String sender) {
        for (Socket socket : clientSockets) {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);


                printWriter.println("#delete " + sender);

            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }

    public void go() {
        clientSockets = new ArrayList<Socket>();

        try {
            ServerSocket serverSock = new ServerSocket(5000);

            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());


                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().go();
    }
}
