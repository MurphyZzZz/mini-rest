package minirest;

import minirest.annotations.GET;
import minirest.annotations.Path;


@Path("/apple")
public class Apple implements Content {

    @GET
    @Path("/name")
    public String getFruitName(){
        return "Apple";
    }
}
