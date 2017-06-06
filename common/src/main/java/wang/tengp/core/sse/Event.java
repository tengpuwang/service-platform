package wang.tengp.core.sse;

import com.alibaba.fastjson.JSON;
import wang.tengp.enums.http.HttpHeader;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * EventSource事件
 * Created by shumin on 16-9-9.
 */
public class Event {

    private static final Logger logger = LoggerFactory.getLogger(Event.class);

    // 事件Id
    private String id = "";
    // 事件类型
    private String event = "";
    // 事件数据
    private List<Object> data = Lists.newArrayList();
    //  失败重试时间
    private Integer retry = 3;

    public Event() {}

    public String getId() {
        return id;
    }

    public Event setId(String id) {
        this.id = id;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public Event setEvent(String event) {
        this.event = event;
        return this;
    }

    public List<Object> getData() {
        return data;
    }

    public Event setData(List<Object> data) {
        this.data = data;
        return this;
    }

    public Integer getRetry() {
        return retry;
    }

    public Event setRetry(Integer retry) {
        this.retry = retry;
        return this;
    }

    public Event put(Object data) {
        this.data.add(data);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!Strings.isNullOrEmpty(id)) {
            builder.append("id: ");
            builder.append(id);
            builder.append("\n");
        }
        if (retry > 0) {
            builder.append("retry: ");
            builder.append(retry);
            builder.append("\n");
        }
        if (!Strings.isNullOrEmpty(event)) {
            builder.append("event: ");
            builder.append(event);
            builder.append("\n");
        }
        if (data != null && data.size() > 0) {
            data.stream().forEach(item -> {
                builder.append("data: ");
                builder.append(JSON.toJSONString(item));
                builder.append("\n");
            });
        } else {
            builder.append("data: ");
            builder.append("\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    private void clean() {
        data.clear();
    }

    public boolean haveData() {
        return this.data.size() > 0;
    }

    /**
     * 发送到
     *
     * @param response
     */
    public void sentTo(Response response) {
        PrintWriter writer = null;
        try {
            writer = response.raw().getWriter();
            writer.print(this);
            response.raw().flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播
     */
    public void broadcast() {
        Map<Long, HttpConnection> connections = HttpConnectionPoll.getConnections();
        List<Long> connections_del = Lists.newArrayList();
        connections.keySet().parallelStream().forEach(key -> {
            HttpConnection connection = connections.get(key);
            Response response = connection.getResponse();
            Request request = connection.getRequest();
            String request_id = request.attribute(HttpHeader.CustomHeader.REQUEST_ID.headername());
//            this.setId(request_id);
            PrintWriter writer = null;
            try {
                writer = response.raw().getWriter();
                writer.print(this);
                response.raw().flushBuffer();
                request.attribute("close", false);
                logger.info("[TengPu] Event Broadcast Success:{}", request_id);
            } catch (IOException e) {
                // 捕获到异常后从连接池删除并关闭连接
                request.attribute("close", true);
                connections_del.add(key);
                logger.warn("[TengPu] Event Broadcast Exception:{}", e.getMessage());
            } catch (Exception e) {
                request.attribute("close", true);
                connections_del.add(key);
                logger.warn("[TengPu] Event Broadcast Exception:{}", e.getMessage());
            } finally {
                connection = null;
                response = null;
                request = null;
                request_id = null;
                writer = null;
            }
        });

        if (connections_del.size() > 0) {
            HttpConnectionPoll.removeConnections(connections_del);
        }

        logger.info("[TengPu] HttpConnectionPoll Current Count:{}", HttpConnectionPoll.count());
    }
}