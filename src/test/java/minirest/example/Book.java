package minirest.example;

import container.MiniDi;
import minirest.annotations.GET;
import minirest.annotations.Path;

@MiniDi
@Path("/book")
public class Book {

    @GET
    @Path("/content")
    public String getBookContent(){
        return "This is a book.";
    }
}
