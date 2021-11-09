package minirest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import minirest.example.Apple;
import minirest.example.Fruit;
import minirest.example.Peach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitTest {
    @Test
    void should_directly_return_content_when_result_is_not_sub_resource_given_url_and_method() {
        String methodName = "GET";
        String uri = "/quantity";
        Apple apple = new Apple();
        Fruit fruit = new Fruit(apple);

        String result = fruit.getContent(methodName, uri, null);

        assertEquals(fruit.getQuantity(), result);
    }

    @Test
    void should_return_subresource_class_when_result_is_subresource_given_method_name_and_url() {
        String methodName = "GET";
        String uri = "/apple/name";
        Apple apple = new Apple();
        Fruit fruit = new Fruit(apple);

        String result = fruit.getContent(methodName, uri, null);

        assertEquals("Apple", result);
    }

    @Test
    void should_pass_value_to_variable_given_a_path_param_value() {
        String methodName = "GET";
        String uri = "/type/1";
        Apple apple = new Apple();
        Fruit fruit = new Fruit(apple);

        String result = fruit.getContent(methodName, uri, null);

        assertEquals("This is type 1 - Pear.", result);
    }

    @Test
    void should_process_request_body_given_a_request_body() throws JsonProcessingException {
        String methodName = "POST";
        String uri = "/peach";
        Apple apple = new Apple();
        Fruit fruit = new Fruit(apple);
        Peach peach = new Peach("peach", "red");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(peach);

        String result = fruit.getContent(methodName, uri, requestBody);

        String expect = "peach name: " + peach.getName() + ", " + "peach color: " + peach.getColor();
        assertEquals(expect, result);
    }
}
