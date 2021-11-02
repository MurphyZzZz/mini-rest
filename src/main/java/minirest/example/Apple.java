package minirest.example;

import container.MiniDi;
import minirest.handler.Content;
import minirest.annotations.GET;
import minirest.annotations.Path;


@Path("/apple")
@MiniDi
public class Apple implements Content {

    @GET
    @Path("/name")
    public String getFruitName(){
        return "Apple";
    }
}
