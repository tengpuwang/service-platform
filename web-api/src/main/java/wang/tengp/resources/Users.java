package wang.tengp.resources;

/**
 * Created by shumin on 16-7-18.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import wang.tengp.common.Pagination;
import wang.tengp.core.annotation.Resource;
import wang.tengp.model.User;

import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

@Resource
public class Users {

    static {
        init();
    }

    private static void init() {

        get("/users", "application/json", (request, response) -> {
            Map result = Maps.newHashMap();
            // 请求参数
            Boolean isPage = Boolean.valueOf(request.queryParams("isPage"));        // 是否分页
            Boolean isSort = Boolean.valueOf(request.queryParams("isSort"));        // 是否排序
            String pageNo = request.queryParams("pageNo");                          // 当前页码
            String pageSize = request.queryParams("pageSize");                      // 分页大小
            JSONObject orderBy = JSON.parseObject(request.queryParams("orderBy"));  // 排序字段 以json字符串形式传递 e.g：{"_id":"DESC" , "name":"DESC", "discription":"DESC"}

            if (isPage) {
                //  分页
                Pagination<User> pagination;
                if (isSort) {
                    //排序
//                    pagination = userService.findByPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize), orderBy);
                } else {
//                    pagination = userService.findByPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize), null);
                }
//                result.put("pageinfo", pagination.getPageInfo());
//                result.put("users", pagination.getPageDatas());
            } else {
                if (isSort) {
                    // 排序
//                    List hosts = userService.find(orderBy);
//                    result.put("users", hosts);
                } else {
                    // 返回所有
                    List<User> users = User.findAll(User.class);
                    result.put("users", users);
                }
            }
            return result;
        }, JSON::toJSONString);

        get("/users/:id", "application/json", (request, response) -> {
            String id = request.params(":id");
            Map result = Maps.newHashMap();
            User user = User.findById(id, User.class);
            result.put("user", user);
            return result;
        }, JSON::toJSONString);

        post("/users", "application/json", (request, response) -> {
            String request_playload = request.body();
            User user = JSON.parseObject(request_playload, User.class);
//            userService.add(user);
            // 添加成功
            response.status(201);
            return response;
        }, JSON::toJSONString);

        /**
         * update user
         * 后期看看有没有更加好的方法
         * zhengm
         */
        put("/users", "application/json", (request, response) -> {
            Map result = Maps.newHashMap();
            String request_playload = request.body();
            User user = JSON.parseObject(request_playload, User.class);

            //组织查询条件-Query
            Query query = new Query();
            Criteria criteria = Criteria.where("id").is(user.getId());
            query.addCriteria(criteria);

            //组织要跟新的数据-Update
            Update update = Update.update("username", user.getUsername()).set("password", user.getPassword());
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.returnNew(true);
            return user.findAndModify(query, update, options, User.class);
        }, JSON::toJSONString);
    }
}
