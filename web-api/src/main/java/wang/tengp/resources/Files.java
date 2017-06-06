package wang.tengp.resources;

import com.alibaba.fastjson.JSON;
import spark.Spark;
import wang.tengp.core.annotation.Resource;
import wang.tengp.model.File;


/**
 * 文件
 * Created by shumin on 16-10-16.
 */
@Resource
public class Files {

    static {
        init();
    }

    private synchronized static void init() {
        Spark.get("/files", (req, rep) -> File.findAll(File.class), JSON::toJSONString);
    }
}
