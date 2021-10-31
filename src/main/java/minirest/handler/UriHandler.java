package minirest.handler;

import org.springframework.web.util.UriTemplate;

import java.util.Map;

public class UriHandler {

    public static String getMatchedUri(String uri, String templateUri) {
        int templateIndex = templateUri.indexOf("/");
        int numOfSlashInTemplateUri = 0;
        while (templateIndex >= 0) {
            numOfSlashInTemplateUri++;
            templateIndex = templateUri.indexOf("/", templateIndex + 1);
        }
        int uriIndex = uri.indexOf("/");
        int numOfSlashInUri = 0;
        while (uriIndex >= 0 && (numOfSlashInUri < numOfSlashInTemplateUri)) {
            numOfSlashInUri += 1;
            uriIndex = uri.indexOf("/", uriIndex + 1);
        }
        if (numOfSlashInUri < numOfSlashInTemplateUri) {
            return null;
        } else {
            if (uriIndex < 0) return uri;
            return uri.substring(0, uriIndex);
        }
    }

    public static Map<String, String> getPathVariable(String uri, String templateUri) {
        UriTemplate template = new UriTemplate(templateUri);
        return template.match(uri);
    }

    public static boolean isUriMatchTemplate(String uri, String templateUri) {
        UriTemplate template = new UriTemplate(templateUri);
        return template.matches(uri);
    }

    public static String findNextSubString(String uri) {
        if (uri.indexOf("/", 1) == -1) {
            return uri;
        } else {
            return uri.substring(0, uri.indexOf("/", 1));
        }
    }
}
