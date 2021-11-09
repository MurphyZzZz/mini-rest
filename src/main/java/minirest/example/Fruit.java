package minirest.example;

import container.MiniDi;
import minirest.annotations.GET;
import minirest.annotations.POST;
import minirest.annotations.Path;
import minirest.annotations.PathParam;
import minirest.annotations.RequestBody;
import minirest.handler.Content;
import org.springframework.web.bind.annotation.RequestParam;

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

    @POST
    @Path("/peach")
    public String setPeach(@RequestBody Peach peach) {
        return "peach name: " + peach.name + ", " + "peach color: " + peach.color;
    }

    @GET
    @Path("/type/{typeId}")
    public String getType(@PathParam("typeId") String typeId) {
        if (typeId.equals("1")) return "This is type 1 - Pear.";
        else if (typeId.equals("2")) return "Apple";
        else return "no matching fruit with id " + typeId;
    }

    @GET
    @Path("/name")
    public String getFruitName(@RequestParam("nickName") String nickName) {
        if (nickName.equals("1")) return "My nickName is wonderful fruit!";
        else return "no return.";
    }

    @GET
    @Path("/level")
    public String getLevel(@RequestParam("level") int level) {
        if (level == 1) return "first level";
        if (level == 2) return "second level";
        else return "no return.";
    }
}
