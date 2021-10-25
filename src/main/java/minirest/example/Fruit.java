package minirest.example;

import minirest.Content;
import minirest.annotations.GET;
import minirest.annotations.Path;


@Path("/fruit")
public class Fruit implements Content {

    @GET
    @Path("/quantity")
    public String getQuantity(){
        return "5";
    }

    @Path("/apple")
    public Apple getApple() {
        return new Apple();
    }

}
