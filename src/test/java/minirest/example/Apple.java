package minirest.example;

import container.MiniDi;
import minirest.annotations.GET;
import minirest.annotations.Path;
import minirest.handler.Content;


@Path("/apple")
@MiniDi
public class Apple implements Content {

    @GET
    @Path("/name")
    public String getFruitName(){
        return "Apple";
    }
}
