package minirest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import minirest.example.Fruit;
import minirest.example.Peach;
import minirest.handler.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitTest {
    @Test
    void should_directly_return_content_when_result_is_not_sub_resource_given_url_and_method() {
        String methodName = "GET";
        String uri = "/quantity";
        Fruit fruit = new Fruit();
        Resource resource = new Resource(fruit, Fruit.class);

        String result = resource.getContent(methodName, uri, null);

        assertEquals(fruit.getQuantity(), result);
    }

    @Test
    void should_pass_value_to_variable_given_a_path_param_value() {
        String methodName = "GET";
        String uri = "/type/1";
        Fruit fruit = new Fruit();
        Resource resource = new Resource(fruit, Fruit.class);

        String result = resource.getContent(methodName, uri, null);

        assertEquals("This is type 1 - Pear.", result);
    }

    @Test
    void should_process_request_body_given_a_request_body() throws JsonProcessingException {
        String methodName = "POST";
        String uri = "/peach";
        Fruit fruit = new Fruit();
        Peach peach = new Peach("peach", "red");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(peach);
        Resource resource = new Resource(fruit, Fruit.class);

        String result = resource.getContent(methodName, uri, requestBody);

        String expect = "peach name: " + peach.getName() + ", " + "peach color: " + peach.getColor();
        assertEquals(expect, result);
    }

    @Test
    void should_return_name_of_different_index_of_apple_given_a_root_resource_with_index_of_sub_resource() {
        String methodName = "GET";
        String uri = "/apple/name?index=1";
        Fruit fruit = new Fruit();
        Resource resource = new Resource(fruit, Fruit.class);

        String result = resource.getContent(methodName, uri, null);

        assertEquals("Apple 1", result);
    }
}
