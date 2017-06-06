package wang.tengp.filters;

import spark.Filter;
import spark.Request;
import spark.Response;
import wang.tengp.common.util.WebContextUtils;
import wang.tengp.enums.http.HttpHeader;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RequestId
 * Created by shumin on 16-8-25.
 */
public final class RequestIdFilter implements Filter {

    private static final AtomicLong lastId = new AtomicLong();
    private static final Set<String> localIps = resolveLocalIps(); // 本机ip地址，用于requestId的生成过程
    private static final long startTimeStamp = System.currentTimeMillis();
    private static final String serverInfo = localIps.toString() + "-" + Long.toString(startTimeStamp, Character.MAX_RADIX);

    // Hide constructor
    private RequestIdFilter() {
    }

    /**
     * Initializes singleton.
     */
    private static final class SingletonHolder {
        private static final RequestIdFilter INSTANCE = new RequestIdFilter();
    }

    public static final RequestIdFilter getInstance() {
        return RequestIdFilter.SingletonHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {

        long requestTimeStamp = System.currentTimeMillis();
        String ip = WebContextUtils.getRemoteAddr(request.raw());
        String user_agent = request.headers(HttpHeader.USER_AGENT.headername());
        String requestInfo = hexIp(ip) + "-" + user_agent.hashCode() + "-" + Long.toString(requestTimeStamp, Character.MAX_RADIX);
        UUID uuid = UUID.nameUUIDFromBytes((serverInfo + "-" + requestInfo).getBytes());
        String requestId = uuid.toString() + "-" + lastId.incrementAndGet();
        response.header("Request-Id", requestId);
        request.attribute("request_id", requestId);
    }

    // 将ip转换为定长8个字符的16进制表示形式：255.255.255.255 -> FFFFFFFF
    private static String hexIp(String ip) {
        StringBuilder sb = new StringBuilder();
        for (String seg : ip.split("\\.")) {
            String h = Integer.toHexString(Integer.parseInt(seg));
            if (h.length() == 1) sb.append("0");
            sb.append(h);
        }
        return sb.toString();
    }

    private static Set<String> resolveLocalIps() {
        Set<InetAddress> addrs = resolveLocalAddresses();
        Set<String> ret = new HashSet<String>();
        for (InetAddress addr : addrs)
            ret.add(addr.getHostAddress());
        return ret;
    }

    /**
     * 获取本地ip地址，有可能会有多个地址, 若有多个网卡则会搜集多个网卡的ip地址
     */
    private static Set<InetAddress> resolveLocalAddresses() {
        Set<InetAddress> addrs = new HashSet<InetAddress>();
        Enumeration<NetworkInterface> ns = null;
        try {
            ns = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            // ignored...
        }
        while (ns != null && ns.hasMoreElements()) {
            NetworkInterface n = ns.nextElement();
            Enumeration<InetAddress> is = n.getInetAddresses();
            while (is.hasMoreElements()) {
                InetAddress i = is.nextElement();
                if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() && !i.isMulticastAddress()
                        && !isSpecialIp(i.getHostAddress())) addrs.add(i);
            }
        }
        return addrs;
    }

    private static boolean isSpecialIp(String ip) {
        if (ip.contains(":")) return true;
        if (ip.startsWith("127.")) return true;
        if (ip.startsWith("169.254.")) return true;
        if (ip.equals("255.255.255.255")) return true;
        return false;
    }
}

