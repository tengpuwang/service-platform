package wang.tengp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import spark.route.RouteOverview;
import wang.tengp.api.Cities;
import wang.tengp.common.ObjectIdDeserializer;
import wang.tengp.common.ObjectIdSerializer;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.BaseDocument;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.filters.Filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;
import static spark.route.RouteOverview.enableRouteOverview;

/**
 * Created by shumin on 16-5-6.
 */
public final class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static PropertiesConfiguration config;
    public static MongoTemplate mongo;

    static {
        try {
            config = new PropertiesConfiguration(App.class.getClassLoader().getResource("propertites/application.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    //  FastJson配置
    static {
        logger.info("[TengPu] Setting Fastjson ……");
        //   注册 ObjectId 序列化/反序列化
        SerializeConfig.getGlobalInstance().put(ObjectId.class, ObjectIdSerializer.getInstance());
        ParserConfig.getGlobalInstance().putDeserializer(ObjectId.class, ObjectIdDeserializer.getInstance());
        SerializerFeature[] features = new SerializerFeature[]{
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect        // 关闭循环引用
        };
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.of(features);
    }

    public static synchronized void main(String[] args) {
        logger.info("[TengPu] Starting Server ……");
        initServer();
        logger.info("[TengPu] Init Spring Context ……");
        initSpringContext();
        logger.info("[TengPu] Wait For Server To Be Initialized ……");
        awaitInitialization(); // Wait for server to be initialized
        logger.info("[TengPu] Init The App ……");
        init();
        logger.info("[TengPu] The App Server Started");

    }

    // 初始化服务器
    private static final synchronized void initServer() {
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        port(8000); //<- Uncomment this if you want spark to listen to port 8010 in stead of the default 4567
    }

    // 初始化Spring上下文
    private static final synchronized void initSpringContext() {
        ApplicationConextUtils.init("/spring/applicationContext.xml");
        BaseDocument.initMongoTemplate();
    }


    // 初始化APP
    private static final synchronized void init() {

        // 异常处理
        exception(Exception.class, (e, req, rep) -> {
            e.printStackTrace();
            logger.error("[TengPu] Catch Exception:{}", e.getMessage());

            if (e instanceof IOException) {
                try {
                    rep.raw().getWriter().close();
                } catch (IOException ioe) {
                    logger.error("[TengPu] Catch Exception:{}", ioe.getMessage());
                }
            } else {
                try {
                    rep.status(500);
                    rep.body(e.getMessage());
                    return;
                } catch (Exception ex) {
                    logger.error("[TengPu] Catch Exception:{}", ex.getMessage());
                }
            }
        });

        before((req, rep) -> {
            logger.info("[TengPu] New Request：url-> " + req.pathInfo() + "; body ->" + req.body() + "; query ->" + req.queryString());
        });

        //before(Filters.ApiVersion);
        // 添加RequestId
        before(Filters.RequestId);
        // CORS 处理跨域访问jiet
        before(Filters.CORS);
        // 开启 GZIP 压缩
        before(Filters.Gzip);
        // 处理 MediaTypes
        after(Filters.ContentType);

        get("/ping", (request, response) -> "I am alive!");

        RouteOverview.enableRouteOverview();
    }
}