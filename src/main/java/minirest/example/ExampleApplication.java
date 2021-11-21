package minirest.example;

import minirest.MiniRest;

public class ExampleApplication {
    public static void main(String[] args) throws Exception {
        new MiniRest(ExampleApplication.class).start();
    }
}
