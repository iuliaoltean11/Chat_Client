package L4;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    JFrame frame;
    JTextArea incoming;
    JTextField outgoing;
    JComboBox comboBox;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    boolean registered = false;
    List<History> messageHistory = new ArrayList<>();

    public void go() {
        frame = new JFrame("Chat Client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        comboBox = new JComboBox<>();
        comboBox.addActionListener(new ComboBoxListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        mainPanel.add(comboBox);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(700, 500);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                if (registered) {
                    String name = comboBox.getSelectedItem().toString();
                    writer.println(name + ":" + outgoing.getText());
                    writer.flush();
                } else {
                    String message = outgoing.getText();
                    writer.println("#new_username " + message);
                    writer.flush();
                    frame.setTitle(message);
                    registered = !registered;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class ComboBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            System.out.println("combobox action");
            incoming.setText("");
            if (registered) {
                String chat = comboBox.getSelectedItem().toString();
                //update mesaje
                for (History entry : messageHistory) {
                    if (entry.name.equals(chat)) {
                        incoming.append(entry.name + ": " + entry.message + "\n");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Client().go();
    }

    public class IncomingReader implements Runnable {
        //citeste de la server
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Client: " + message);
                    //client vechi
                    if (message.contains("#new_username")) {
                        System.out.println("updateList: " + message);
                        String[] usersList = message.split("\\@");
                        comboBox.addItem(usersList[1]);
                        //client nou
                    } else if (message.contains("#new_list")) {
                        String[] usersList = message.split("\\@");
                        usersList = Arrays.copyOfRange(usersList, 1, usersList.length);
                        for (String client : usersList) {
                            comboBox.addItem(client);
                        }
                    } else if (message.contains("#delete")) {
                        String[] usersList = message.split(" ");
                        comboBox.removeItem(usersList[1]);
                    } else if (message.contains("error:")) {
                        incoming.append(message);
                        registered = false;
                    } else {
                        //mesaj normal
                        System.out.println("incoming: " + message);
                        String[] messageSplit = message.split(":");
                        if (messageSplit.length > 1) {
                            messageHistory.add(new History(messageSplit[0], messageSplit[1]));
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}