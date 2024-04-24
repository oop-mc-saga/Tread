package example2WithWaitAndNotify;

import example2.Token;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Client class for the example2
 *
 * @author tadaki
 */
public class Client implements Runnable {

    private final int id;// client id
    private final Server server;
    private volatile boolean running = true;
    private final Queue<Token> tokens; // tokens that this client has

    /**
     * Constructor
     *
     * @param id client id
     * @param server server
     */
    public Client(int id, Server server) {
        this.id = id;
        this.server = server;
        tokens = new LinkedList<>();
    }

    /**
     * one update operation
     */
    private synchronized void update() {
        if (!tokens.isEmpty()) {// if this has tokens
            // put token to the server
            // if the server is terminated, the return value is false
            running = server.put(this, tokens.poll());
        }
        if (running) {
            Token t = server.get(this);// get token from the server
            if (t == null) {
                try {
                    Date dstart = new Date();
                    wait(1000);
                    Date d = new Date();
                    long dulation = d.getTime() - dstart.getTime();
                    System.out.println(d + " " + this + " restarts after " + dulation);
                } catch (InterruptedException ex) {
                }
            } else {
                if (t == Server.falseToken) {
                    running = false;
                } else {
                    tokens.add(t);
                }
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            update();
            int timeout = (int) (1000 * Math.random());
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public String toString() {
        return "client-" + id;
    }
}
