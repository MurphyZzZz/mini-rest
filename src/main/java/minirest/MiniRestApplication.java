package minirest;

import minirest.server.Server;

public class MiniRestApplication {

    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
