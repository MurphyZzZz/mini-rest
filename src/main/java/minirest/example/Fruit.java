package minirest.example;

import container.MiniDi;
import minirest.Content;
import minirest.annotations.GET;
import minirest.annotations.Path;

import javax.inject.Inject;


@Path("/fruit")
@MiniDi
public class Fruit implements Content {
    Apple apple;

    @Inject
    public Fruit(Apple apple) {
        this.apple = apple;
    }

    @GET
    @Path("/quantity")
    public String getQuantity(){
        return "5";
    }

    @Path("/apple")
    public Apple getApple() {
        return this.apple;
    }
}
