import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    public static void main(String[] args) throws IOException {

        //store all connections information
        List<Handler> conns = new ArrayList<>();
        //shared linked list to store asked questions
        LinkedList<Message> msgs = new LinkedList<>();
        //remark how many questions a client have been answered
        Map<Socket, Integer> clients = new HashMap<>();

        //question answer thread
        Runnable runnable = ()->{
            while (true){
                try {
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                conns.forEach(conn -> {
                    if (!msgs.isEmpty()){
                        Message msg = msgs.remove(); //pick up one msg to answer
                        Socket sender = msg.getSender();
                        int count;
                        count = clients.get(sender);
                        if (count < 10){
                            clients.replace(sender, count, count + 1);
                            conn.sendMsg(msg.getSender()+"(" +clients.get(sender)+") asked question : " +msg.getContent());
                        }else {
                            //after 10 times remove client socket
                            conns.remove(sender);
                            conn.sendMsg(msg.getSender()+" please register first to continue this game !!! ");
                        }
                    }
                });
            }
        };
        Thread listner = new Thread(runnable);
        listner.start();

        ServerSocket server = new ServerSocket(8899);
        while (true){
            Socket client = server.accept();
            Handler handler = new Handler(client,msgs);
            conns.add(handler);
            clients.put(client, 0);
            handler.start();
        }
    }
}
