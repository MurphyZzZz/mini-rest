package minirest.handler;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static minirest.handler.UriHandler.getQueryParams;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UriHandlerTest {

    @Test
    void should_return_url_given_a_url_without_path_param_and_query_string() {
        String url = "/Apple";
        String template = url;

        String result = UriHandler.getMatchedUri(url, template);

        assertEquals("/Apple", result);
    }

    @Test
    void should_return_url_only_contain_path_parameter_value_given_a_url_contain_a_path_parameter() {
        String url = "/fruit/1/apple";
        String template = "/fruit/{number}";

        String result = UriHandler.getMatchedUri(url, template);

        assertEquals("/fruit/1", result);
    }

    @Test
    void should_return_url_contain_query_string_value_given_a_url_contain_a_query_string() {
        String url = "/fruit/1?name=apple";
        String template = "/fruit/{number}";

        String result = UriHandler.getMatchedUri(url, template);

        assertEquals("/fruit/1?name=apple", result);
    }

    @Test
    void should_return_url_contain_query_string_value_given_a_single_url_contain_a_query_string() {
        String url = "/name?nickName=1";
        String template = "/name";

        Boolean result = UriHandler.isSubstringMatched(url, template);

        assertEquals(true, result);
    }

    @Test
    void should_return_url_contain_path_param_value_given_a_single_url_contain_a_path_param() {
        String url = "/type/1";
        String template = "/type/{typeId}";

        Boolean result = UriHandler.isSubstringMatched(url, template);

        assertEquals(true, result);
    }

    @Test
    void should_return_a_map_of_query_param_given_a_complete_url() {
        String url = "http://localhost:8080/fruit/apple?color=red&size=big";

        Map<String, String> result = getQueryParams(url);

        assertEquals("red", result.get("color"));
        assertEquals("big", result.get("size"));
    }

    @Test
    void should_return_a_map_of_query_param_given_a_incomplete_url() {
        String url = "/name?color=red&size=big";

        Map<String, String> result = getQueryParams(url);

        assertEquals("red", result.get("color"));
        assertEquals("big", result.get("size"));
    }

    @Test
    void should_return_a_empty_map_of_query_param_given_a_url_without_query_param() {
        String url = "/fruit/apple";

        Map<String, String> result = getQueryParams(url);

        assertEquals(0, result.size());
    }

    @Test
    void should_1_return_a_map_of_query_param_given_a_incomplete_url() {
        String url = "/name?nickName=fruitName";;

        Map<String, String> result = getQueryParams(url);

        assertEquals("fruitName", result.get("nickName"));
    }
}
