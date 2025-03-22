package mx.pagos.admc.socket.stomp;

public class RespSocket {

    private String content;

    public RespSocket() {
    }

    public RespSocket(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}