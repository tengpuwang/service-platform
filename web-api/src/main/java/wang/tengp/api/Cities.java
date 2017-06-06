package wang.tengp.api;

import com.alibaba.fastjson.JSON;
import spark.Request;
import spark.Response;
import spark.Route;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.model.City;
import wang.tengp.spider.MeiTuan;

import static spark.Spark.halt;

/**
 * Created by shumin on 2017/2/14.
 */
public class Cities {

    public static Route getAll = (req, rep) -> City.findAll(City.class);
    public static Route getById = (req, rep) -> City.findById(req.params(":id"), City.class);
    public static Route add = (req, rep) -> {
        String request_playload = req.body();
        City city = JSON.parseObject(request_playload, City.class);
        // 拉取美图数据填充city
        try {
            MeiTuan.fill(city);
        } catch (Exception e) {
            halt(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "获取城市数据信息发生错误：" + e.getMessage() + "  请稍候重试！");
        }
        city.insert();
        // 添加成功
        rep.status(HttpStatus.CREATED.getCode());
        return city;
    };
    public static Route del = (req, rep) -> {
        String id = req.params(":id");
        City.removeById(id, City.class);
        rep.status(HttpStatus.NO_CONTENT.getCode());
        return rep;
    };
}
