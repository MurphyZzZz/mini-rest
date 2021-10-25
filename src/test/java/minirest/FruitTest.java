package minirest;

import minirest.example.Fruit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitTest {
    @Test
    void should_directly_return_content_when_result_is_not_sub_resource_given_url_and_method() {
        String methodName = "GET";
        String uri = "/quantity";
        Fruit fruit = new Fruit();

        String result = fruit.getContent(methodName, uri);

        assertEquals(fruit.getQuantity(), result);
    }

    @Test
    void should_return_subresource_class_when_result_is_subresource_given_method_name_and_url() {
        String methodName = "GET";
        String uri = "/apple/name";
        Fruit fruit = new Fruit();

        String result = fruit.getContent(methodName, uri);

        assertEquals("Apple", result);
    }
}
