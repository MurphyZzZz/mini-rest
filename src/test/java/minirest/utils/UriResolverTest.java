package minirest.utils;

import minirest.resolver.UriResolver;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UriResolverTest {

    @Test
    void should_return_url_given_a_url_without_path_param_and_query_string() {
        String url = "/Apple";
        String template = url;

        String result = UriResolver.getMatchedUri(url, template);

        assertEquals("/Apple", result);
    }

    @Test
    void should_return_url_only_contain_path_parameter_value_given_a_url_contain_a_path_parameter() {
        String url = "/fruit/1/apple";
        String template = "/fruit/{number}";

        String result = UriResolver.getMatchedUri(url, template);

        assertEquals("/fruit/1", result);
    }

    @Test
    void should_return_url_contain_query_string_value_given_a_url_contain_a_query_string() {
        String url = "/fruit/1?name=apple";
        String template = "/fruit/{number}";

        String result = UriResolver.getMatchedUri(url, template);

        assertEquals("/fruit/1?name=apple", result);
    }

    @Test
    void should_return_path_param_value_map_given_a_url_and_template() {
        String url = "/fruit/apple?color=red";
        String template = "/fruit/{name}";

        Map<String, String> result = UriResolver.getPathVariable(url, template);

        assertEquals("apple?color=red", result.get("name"));
    }
}
