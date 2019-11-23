import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * we assume this is a very interesting chatting room
 * and every new client can ask 10 questions for free
 * the server will pick up question in msgs list one by one and answer it
 * every time after the answering will be remarked, that how many time this client has been asked
 * after 10 times free answer, server wont answer any question
 */
public class Server {
    public static void main(String[] args) throws IOException {

        List<Handler> connects = new ArrayList<>();
        //only one shared list to store msgs from clients
        LinkedList<Message> msgs = new LinkedList<>();
        //remark how many time clients have been answered
        Map<String, Integer> clients = new HashMap<>();

        Runnable runnable = ()->{

            while (true){
                try {
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                connects.forEach(conn -> {
                    if (!msgs.isEmpty()){
                        Message msg = msgs.remove();
                        String sender = msg.getSender();
                        if (!clients.containsKey(sender)){
                            clients.put(sender, 0);
                        }
                        conn.sendMsg(msg.getSender() +" asked question :" +msg.getContent());
                        Integer count = clients.get(sender);
                        clients.replace(sender, count, count+1);
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
            connects.add(handler);
            handler.start();
        }
    }
}
