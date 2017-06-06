package wang.tengp.resources;

/**
 * Created by shumin on 16-7-18.
 */

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.bson.types.ObjectId;
import wang.tengp.core.annotation.Resource;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.model.City;
import wang.tengp.model.Site;

import java.util.Map;

import static spark.Spark.*;

@Resource
public class Sites {

    static {
        init();
    }

    private synchronized static void init() {

        get("/sites", (request, response) -> Site.findAll(Site.class), JSON::toJSONString);

        get("/sites/:id", (request, response) -> City.findById(request.params(":id"), Site.class), JSON::toJSONString);

        /**
         * 添加站点
         */
        post("/sites", (request, response) -> {
            String request_playload = request.body();
            Site site = JSON.parseObject(request_playload, Site.class);
            if (site.getCityId() == null) {
                halt(HttpStatus.BAD_REQUEST.getCode(), "请选择站点所在城市！");
            }
            site.setCity(City.findById(site.getCityId(), City.class));
            site.insert();
            // 添加成功
            response.status(HttpStatus.CREATED.getCode());
            return site;
        }, JSON::toJSONString);

        /**
         * 修改站点
         */
        put("/sites/:id", (request, response) -> {
            String request_playload = request.body();
            String id = request.queryParams(":id");
            Site site = JSON.parseObject(request_playload, Site.class);
            site.setId(new ObjectId(id));
            site.save();
            return site;
        }, JSON::toJSONString);

        /**
         * 删除站点
         */
        delete("/sites/:id", (request, response) -> {
            String id = request.params(":id");
            Site.removeById(id, Site.class);
            response.status(HttpStatus.NO_CONTENT.getCode());
            return response;
        }, JSON::toJSONString);
    }
}