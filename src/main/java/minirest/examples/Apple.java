package minirest.examples;

import lombok.AllArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@AllArgsConstructor
public class Apple  {

    String name;

    @GET
    @Path("/name")
    public String getFruitName(){
        return name;
    }
}
