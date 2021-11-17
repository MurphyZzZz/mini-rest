package minirest.example;

import minirest.handler.Content;
import minirest.annotations.GET;
import minirest.annotations.Path;
import container.MiniDi;

@MiniDi
@Path("/book")
public class Book {

    @GET
    @Path("/content")
    public String getBookContent(){
        return "This is a book.";
    }
}
