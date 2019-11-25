import java.io.Serializable;
import java.net.Socket;

public class Message implements Serializable {

    private Socket sender;
    private String content;

    public Message(String content) {
        this.content = content;
    }

    public Socket getSender() {
        return sender;
    }

    public void setSender(Socket sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
