package wang.tengp.resources;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import spark.globalstate.ServletFlag;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.annotation.Resource;
import wang.tengp.enums.http.HttpStatus;
import wang.tengp.qiniu.QiniuUploader;
import wang.tengp.qiniu.UploadResult;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * 上传
 * Created by shumin on 16-10-27.
 */

@Resource
public class Upload {

    static {
        init();
    }


    private synchronized static void init() {

        get("/upload", (request, response) -> {
            response.header("Content-Type", "text/html;charset=utf-8");
            String html = "<form method='post' enctype='multipart/form-data'>" // note the enctype
                    + "    <input type='file' name='uploaded_file' accept='.png'>" // make sure to call getPart using the same "name" in the post
                    + "    <button>Upload picture</button>"
                    + "</form>";
            return html;
        });

        get("/upload/files", (request, response) -> wang.tengp.model.File.findAll(wang.tengp.model.File.class), JSON::toJSONString);

        /**
         * 上传文件
         */
        post("/upload", (request, response) -> {

            List<wang.tengp.model.File> files = Lists.newArrayList();

            int max_upload_size = Integer.parseInt(ApplicationConextUtils.getPropertiesValue("file.max_upload_size"));
            // Check that we have a file upload request
            boolean isMultipart = ServletFileUpload.isMultipartContent(request.raw());

            if (isMultipart) {  // 带文件上传的表单 multipart/form-data
                // Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();

                if (ServletFlag.isRunningFromServlet()) {
                    // Configure a repository (to ensure a secure temp location is used)
                    ServletContext servletContext = request.raw().getServletContext();
                    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
                    factory.setRepository(repository);
                } else {
                    File repository = new File(System.getProperty("java.io.tmpdir"));
                    factory.setRepository(repository);
                }

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(max_upload_size);
                upload.setSizeMax(max_upload_size);

                // Parse the request
                Iterator<FileItem> items = upload.parseRequest(request.raw()).iterator();
//            FileItemIterator iters = upload.getItemIterator(request.raw());
                while (items.hasNext()) {

                    FileItem item = items.next();

                    //isFormField方法用于判断FileItem类对象封装的数据是一个普通文本表单字段，还是一个文件表单字
                    if (item.isFormField()) {
                        String fieldName = item.getFieldName();
                        String value = item.getString();
                        InputStream stream = item.getInputStream();
                    } else {
                        ObjectId id = new ObjectId();
                        // byte
                        Long size = item.getSize();

                        String fileName = item.getName();
                        String postfix = StringUtils.substringAfterLast(fileName, ".");
                        String contentType = item.getContentType();
                        wang.tengp.model.File file = new wang.tengp.model.File();
                        String key = id.toHexString() + "." + postfix;
                        // 上传文件
                        InputStream stream = item.getInputStream();
                        byte[] data = new byte[Math.toIntExact(size)];
                        stream.read(data);
                        stream.close();
                        UploadResult uploadResult = QiniuUploader.upload(data, key);
                        file
                                .setUploadTime(new Date())
                                .setId(id)
                                .setSizeInBytes(size)
                                .setContentType(contentType)
                                .setFileName(fileName)
                                .setPostfix(postfix)
                                .setHash(uploadResult.getHash())
                                .setToken(uploadResult.getToken())
                                .setPath(uploadResult.getPath())
                                .setKey(uploadResult.getKey())

                                .insert();
                        files.add(file);

                    }
                }
            } else {    // 二进制上传
                ObjectId id = new ObjectId();
//                Long size = Long.valueOf(request.queryParams("size"));
//                String contentType = request.queryParams("type");
                Long size = Long.valueOf(request.contentLength());
                String contentType = request.contentType();
                String fileName = request.queryParams("name");
                String postfix = StringUtils.substringAfterLast(fileName, ".");


                wang.tengp.model.File file = new wang.tengp.model.File();
                String key = id.toHexString() + "." + postfix;
                InputStream stream = request.raw().getInputStream();

                byte[] data = new byte[Math.toIntExact(size)];
                stream.read(data);
//                stream.close();
                // 上传文件
                UploadResult uploadResult = QiniuUploader.upload(data, key);
                file
                        .setUploadTime(new Date())
                        .setId(id)
                        .setSizeInBytes(size)
                        .setContentType(contentType)
                        .setFileName(fileName)
                        .setPostfix(postfix)
                        .setHash(uploadResult.getHash())
                        .setToken(uploadResult.getToken())
                        .setPath(uploadResult.getPath())
                        .setKey(uploadResult.getKey())
                        .insert();
                files.add(file);
            }

            response.status(HttpStatus.CREATED.getCode());
            return files;
        }, JSON::toJSONString);
    }
}