package minirest.handler;

import minirest.example.Apple;
import minirest.example.Fruit;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static minirest.handler.ParameterHandler.getRequestParameters;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterHandlerTest {
    Fruit fruit = new Fruit();

    @Test
    void should_return_path_variable_given_a_url_and_function() throws NoSuchMethodException {
        String separateUrl = "/type/1";
        Method method = fruit.getClass().getMethod("getType", String.class);

        Object[] result = getRequestParameters(method, separateUrl, null);

        assertEquals("1", result[0]);
    }

    @Test
    void should_return_request_variable_given_a_url_and_function() throws NoSuchMethodException {
        String separateUrl = "/name?nickName=fruitName";
        Method method = fruit.getClass().getMethod("getFruitName", String.class);

        Object[] result = getRequestParameters(method, separateUrl, null);

        assertEquals("fruitName", result[0]);
    }

    @Test
    void should_return_corresponding_class_type_given_a_url_and_function() throws NoSuchMethodException {
        String separateUrl = "/level?level=1";
        Method method = fruit.getClass().getMethod("getLevel", int.class);

        Object[] result = getRequestParameters(method, separateUrl, null);

        assertEquals(1, result[0]);
    }
}
