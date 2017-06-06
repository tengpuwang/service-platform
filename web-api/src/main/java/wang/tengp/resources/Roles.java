package wang.tengp.resources;

/**
 * Created by shumin on 16-7-18.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import wang.tengp.common.Pagination;
import wang.tengp.core.annotation.Resource;
import wang.tengp.model.Role;

import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

@Resource
public class Roles {

    static {
        init();
    }

    private static void init() {

        get("/roles", "application/json", (request, response) -> {
            Map result = Maps.newHashMap();
            // 请求参数
            Boolean isPage = Boolean.valueOf(request.queryParams("isPage"));        // 是否分页
            Boolean isSort = Boolean.valueOf(request.queryParams("isSort"));        // 是否排序
            String pageNo = request.queryParams("pageNo");                          // 当前页码
            String pageSize = request.queryParams("pageSize");                      // 分页大小
            JSONObject orderBy = JSON.parseObject(request.queryParams("orderBy"));  // 排序字段 以json字符串形式传递 e.g：{"_id":"DESC" , "name":"DESC", "discription":"DESC"}

            if (isPage) {
                //  分页
                Pagination<Role> pagination;
                if (isSort) {
                    //排序
//                    pagination = roleService.findByPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize), orderBy);
                } else {
//                    pagination = roleService.findByPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize), null);
                }
//                result.put("pageinfo", pagination.getPageInfo());
//                result.put("roles", pagination.getPageDatas());
            } else {
                if (isSort) {
                    // 排序
//                    List<Role> roles = roleService.find(orderBy);
//                    result.put("roles", roles);
                } else {
                    // 返回所有
                    List<Role> roles = Role.findAll(Role.class);
                    result.put("roles", roles);
                }
            }
            return result;
        }, JSON::toJSONString);

        get("/roles/:id", "application/json", (request, response) -> {
            String id = request.params(":id");
            Map result = Maps.newHashMap();
            Role role = Role.findById(id, Role.class);
            result.put("role", role);
            return result;
        }, JSON::toJSONString);

        post("/roles", "application/json", (request, response) -> {
            String request_playload = request.body();
            Role Role = JSON.parseObject(request_playload, Role.class);
            Role.insert();
            // 添加成功
            response.status(201);
            return response;
        }, JSON::toJSONString);
    }
}