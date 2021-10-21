import minirest.annotations.GET;
import minirest.annotations.Path;
import container.MiniDi;

@MiniDi
@Path("/book")
public class Book {

    @GET
    public String getBookContent(){
        return "This is a book.";
    }
}
