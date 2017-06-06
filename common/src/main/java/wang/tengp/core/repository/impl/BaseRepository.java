package wang.tengp.core.repository.impl;

import wang.tengp.common.Pagination;
import wang.tengp.common.util.ReflectionUtils;
import wang.tengp.core.repository.IBaseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by shumin on 16-6-16.
 */
public abstract class BaseRepository<T> implements IBaseRepository<T> {

    private static final int DEFAULT_SKIP = 0;
    private static final int DEFAULT_LIMIT = 200;

    /**
     * spring mongodb　集成操作类
     */
    protected MongoTemplate mongoTemplate;

    /**
     * 注入mongodbTemplate
     *
     * @param mongoTemplate
     */


//    protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);
    @Autowired
    @Qualifier("mongoTemplate")
    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        if (this.mongoTemplate == null) {
            this.mongoTemplate = mongoTemplate;
        }
    }

    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    public List<T> findAll() {
        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        return find(query);
    }

    public T findById(ObjectId id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    public T findById(String id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    public void update(Query query, Update update) {
        mongoTemplate.findAndModify(query, update, this.getEntityClass());
    }

    public T update(T entity) {
        return save(entity);
    }

    public T save(T entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    public T insert(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }

    public List<T> insert(List<T> entities) {
        mongoTemplate.insert(entities);
        return entities;
    }


    public Pagination<T> findByPage(Pagination<T> page, Query query) {
        // 总记录数
        long count = this.count(query);
        page.getPageInfo().setTotalCount(count);
        int pageNumber = page.getPageInfo().getPageNo();
        int pageSize = page.getPageInfo().getPageSize();
        query.skip((pageNumber - 1) * pageSize).limit(pageSize);
        List<T> rows = this.find(query);
        page.setPageDatas(rows);
        return page;
    }

    public long count(Query query) {
        return mongoTemplate.count(query, this.getEntityClass());
    }

    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(getClass());
    }



    public void deleteById(String id){
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.findAllAndRemove(query,this.getEntityClass());
    }
}
