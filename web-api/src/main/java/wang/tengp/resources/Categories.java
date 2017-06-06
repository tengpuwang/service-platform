package wang.tengp.resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import wang.tengp.common.TreeBuilder;
import wang.tengp.common.TreeNode;
import wang.tengp.core.annotation.Resource;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.model.Category;

import java.util.List;

import static spark.Spark.*;

/**
 * 信息分类API
 * Created by shumin on 16-10-20.
 */
@Resource
public class Categories {

    static {
        init();
    }

    private synchronized static void init() {

        /**
         * 查询所有分类
         */
        get("/categories", (request, response) -> {

            // 格式：tree/list 默认list格式
            String format = request.queryParams("format");

            // 查询分类列表
            List<Category> categories = Category.findAll(Category.class);
            List<TreeNode<Category>> treeNodeList = Lists.newArrayList();
            categories.parallelStream().forEach(category -> {
                TreeNode<Category> treeNode = new TreeNode<Category>();
                treeNode.setId(category.getId()).setName(category.getName()).setData(category).setOrder(category.getOrder());
                if (category.getSupCategory() != null) {
                    treeNode.setParentid(category.getSupCategory().getId());
                }
                treeNodeList.add(treeNode);
            });
            if (format != null && format.equals("tree")) {
                return TreeBuilder.buildListToTree(treeNodeList);
            }
            return categories;
        }, JSON::toJSONString);

        get("/categories/:id", (request, response) -> Category.findById(request.params(":id"), Category.class), JSON::toJSONString);

        /**
         * 添加分类
         */
        post("/categories", (request, response) -> {
            String request_playload = request.body();

            //  批量添加
            if (request_playload.startsWith("[") && request_playload.endsWith("]")) {
                JSONArray jsonArray = JSONArray.parseArray(request_playload);
                List<Category> categories = Lists.newArrayList();
                jsonArray.parallelStream().forEach(item -> {
                    JSONObject jsonObject = (JSONObject) item;
                    if (jsonObject.containsKey("supCategory")) {
                        String supCategoryId = jsonObject.getString("supCategory");
                        if (Strings.isNullOrEmpty(supCategoryId)) {
                            halt(HttpStatus.BAD_REQUEST.getCode(), "参数：supCategory 不能为空！");
                        }
                        Category supCategory = Category.findById(supCategoryId, Category.class);
                        jsonObject.put("supCategory", supCategory);
                    }
                    Category category = jsonObject.toJavaObject(Category.class).insert();
                    categories.add(category);
                });
                Category.insertAll(categories);
            }
            // 单个添加
            else if (request_playload.startsWith("{") && request_playload.endsWith("}")) {
                JSONObject jsonObject = JSON.parseObject(request_playload);
                if (jsonObject.containsKey("supCategory")) {
                    String supCategoryId = jsonObject.getString("supCategory");
                    if (Strings.isNullOrEmpty(supCategoryId)) {
                        halt(HttpStatus.BAD_REQUEST.getCode(), "参数：supCategory 不能为空！");
                    }
                    Category supCategory = Category.findById(supCategoryId, Category.class);
                    jsonObject.put("supCategory", supCategory);
                    Category category = jsonObject.toJavaObject(Category.class).insert();
                } else {
                    Category category = jsonObject.toJavaObject(Category.class).insert();
                }
            } else {
                halt();
            }

            response.status(HttpStatus.CREATED.getCode());
            return response;
        }, JSON::toJSONString);

        // 删除分类
        delete("/categories/:id", (request, response) -> {
            String id = request.params(":id");
            response.status(HttpStatus.NO_CONTENT.getCode());
            return response;
        }, JSON::toJSONString);
    }
}