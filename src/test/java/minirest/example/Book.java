package minirest.example;

import container.MiniDi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@MiniDi
@Path("/book")
public class Book {

    @GET
    @Path("/content")
    public String getBookContent(){
        return "This is a book.";
    }
}
