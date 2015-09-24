package org.rga;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
public abstract class RestClient {

    public final URL baseUrl;

    public RestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public abstract GetBuilder get(String... uriFragment);

    public abstract WithBodyBuilder post(String... uriFragment);

    public abstract WithBodyBuilder put(String... uriFragment);

    public abstract WithBodyBuilder patch(String... uriFragment);
}
