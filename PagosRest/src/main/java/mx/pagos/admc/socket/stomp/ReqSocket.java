package mx.pagos.admc.socket.stomp;

public class ReqSocket {

    private String name;

    public ReqSocket() {
    }

    public ReqSocket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}