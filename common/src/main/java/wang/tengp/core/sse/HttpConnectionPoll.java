package wang.tengp.core.sse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 接入 /sse 推送 的 连接池
 * Created by shumin on 16-9-20.
 */
public class HttpConnectionPoll {

    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionPoll.class);

    private static final AtomicLong lastId = new AtomicLong();
    private static final Map<Long, HttpConnection> CONNECTIONS = Maps.newHashMap();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // 心跳检测线程，维护连接池的连接
    static {
        Thread sent = new Thread(
                () -> {
                    while (true) {
                        // 每隔5s广播一次当前服务器时间（心跳检测,将断掉的连接关闭）
                        if (CONNECTIONS.size() > 0) {
                            Event time = new Event();
                            time.setEvent("server-time");
                            time.setData(Lists.newArrayList(DATE_FORMAT.format(new Date())));
                            time.broadcast();
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        sent.start();
    }

    private HttpConnectionPoll() {
    }

    public static Map<Long, HttpConnection> getConnections() {
        return CONNECTIONS;
    }

    public static void addConnections(Collection<HttpConnection> connections) {
        connections.addAll(connections);
    }

    public static void addConnection(HttpConnection connection) {
        CONNECTIONS.put(lastId.incrementAndGet(), connection);
        logger.info("[TengPu] Add New Connection， HttpConnectionPoll Current Count:{}", count());
    }

    public static void removeConnections(List<Long> keys) {
        for (Long key : keys) {
            HttpConnection value = CONNECTIONS.get(key);
            if (CONNECTIONS.containsValue(value)) {
                boolean flag = CONNECTIONS.remove(key, value);
                if (flag) {
                    logger.info("[TengPu] HttpConnectionPoll Remove:[{}]", key);
                }
            }
        }
        logger.info("[TengPu] Remove Connection， HttpConnectionPoll Current Count:{}", count());
    }

    public static void removeConnection(Long key) {
        if (CONNECTIONS.containsKey(key)) {
            HttpConnection value = CONNECTIONS.get(key);
            boolean flag = CONNECTIONS.remove(key, value);
            if (flag) {
                logger.info("[TengPu] HttpConnectionPoll Remove:[{}]", key);
            }
        }
        logger.info("[TengPu] Remove Connection， HttpConnectionPoll Current Count:{}", count());
    }

    public static void clear() {
        CONNECTIONS.clear();
    }

    public static int count() {
        return CONNECTIONS.size();
    }

}
