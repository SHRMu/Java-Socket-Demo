import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

/**
 * connection handler
 */
public class Handler extends Thread{

    private Socket client;
    private LinkedList<Message> msgs;
    private ObjectInputStream ois;
    private PrintWriter pw;

    public Handler(Socket client, LinkedList msgs) {
        this.client = client;
        this.msgs = msgs;
        try{
            this.ois = new ObjectInputStream(client.getInputStream());
            this.pw = new PrintWriter(client.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            Message msg;
            try{
                if ((msg = (Message) ois.readObject()) != null){
                    msg.setSender(client); //set socket info
                    synchronized (msgs){
                        msgs.add(msg); // synchronized by using shared linkedlist
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String msg){
        pw.println(msg);
        pw.flush();
    }
}
