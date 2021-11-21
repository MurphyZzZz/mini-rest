package minirest.examples;

import lombok.AllArgsConstructor;
import minirest.annotations.GET;
import minirest.annotations.Path;

@AllArgsConstructor
public class Apple  {

    String name;

    @GET
    @Path("/name")
    public String getFruitName(){
        return name;
    }
}
