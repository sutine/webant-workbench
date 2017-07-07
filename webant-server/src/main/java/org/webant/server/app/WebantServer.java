package org.webant.server.app;

/**
 * run a worker server to debug
 */
public class WebantServer {
    public static void main(String[] args) {
        org.webant.worker.app.WorkerServer.main(args);
    }
}
