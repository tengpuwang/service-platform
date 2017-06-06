package wang.tengp.core.sse;

import wang.tengp.enums.http.HttpHeader;
import spark.Request;
import spark.Response;

import java.io.IOException;

/**
 * Created by shumin on 16-9-20.
 */
public class HttpConnection {
    private Request request;
    private Response response;

    public HttpConnection() {
    }

    public HttpConnection(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public HttpConnection setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Response getResponse() {
        return response;
    }

    public HttpConnection setResponse(Response response) {
        this.response = response;
        return this;
    }

    public void close() {
        try {
            this.response.raw().flushBuffer();
            this.response.raw().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        if (this.getRequest().attribute(HttpHeader.CustomHeader.REQUEST_ID.headername()) != null) {
            return this.getRequest().attribute(HttpHeader.CustomHeader.REQUEST_ID.headername()).hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (this == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        if (obj instanceof HttpConnection) {
            HttpConnection that = (HttpConnection) obj;
            if (this.getRequest().attribute(HttpHeader.CustomHeader.REQUEST_ID.headername()) != null) {
                return this.getRequest().attribute(HttpHeader.CustomHeader.REQUEST_ID.headername()).equals(that.getRequest().attribute(HttpHeader.CustomHeader.REQUEST_ID.headername()));
            } else {
                return super.equals(that);
            }
        } else {
            return false;
        }
    }
}