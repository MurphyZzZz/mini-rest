package minirest.fixture;

import minirest.handler.Content;
import minirest.annotations.GET;
import minirest.annotations.Path;
import container.MiniDi;

@MiniDi
@Path("/book")
public class Book implements Content {

    @GET
    @Path("/content")
    public String getBookContent(){
        return "This is a book.";
    }
}
