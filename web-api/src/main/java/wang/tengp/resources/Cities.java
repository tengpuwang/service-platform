package wang.tengp.resources;

/**
 * Created by shumin on 16-7-18.
 */

import com.alibaba.fastjson.JSON;
import wang.tengp.core.annotation.Resource;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.model.City;
import wang.tengp.spider.MeiTuan;

import static spark.Spark.*;

@Resource
public class Cities {

    static {
        init();
    }

    private synchronized static void init() {

        get("/cities", (request, response) -> City.findAll(City.class), JSON::toJSONString);

        get("/cities/:id", (request, response) -> City.findById(request.params(":id"), City.class), JSON::toJSONString);

        post("/cities", (request, response) -> {
            String request_playload = request.body();
            City city = JSON.parseObject(request_playload, City.class);
            // 拉取美图数据填充city
            try {
                MeiTuan.fill(city);
            } catch (Exception e) {
                halt(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "获取城市数据信息发生错误：" + e.getMessage() + "  请稍候重试！");
            }
            city.insert();
            // 添加成功
            response.status(HttpStatus.CREATED.getCode());
            return city;
        }, JSON::toJSONString);

        delete("/cities/:id", "application/json", (request, response) -> {
            String id = request.params(":id");
            City.removeById(id, City.class);
            response.status(HttpStatus.NO_CONTENT.getCode());
            return response;
        }, JSON::toJSONString);

    }

}