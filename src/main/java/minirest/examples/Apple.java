package minirest.examples;

import minirest.annotations.GET;
import minirest.annotations.Path;
import minirest.handler.Content;


public class Apple implements Content {

    @GET
    @Path("/name")
    public String getFruitName(){
        return "Apple";
    }
}
