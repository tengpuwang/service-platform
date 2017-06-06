package wang.tengp.core.repository;

import wang.tengp.common.Pagination;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by shumin on 16-6-16.
 */
@NoRepositoryBean
public interface IBaseRepository<T> {

    /**
     * 通过条件查询实体(集合)
     *
     * @param query
     */
     List<T> find(Query query);

    /**
     * 通过一定的条件查询一个实体
     *
     * @param query
     * @return
     */
     T findOne(Query query);

    /**
     * 查询所有实体(集合)
     */
     List<T> findAll();


    /**
     * 通过ID获取记录
     *
     * @param id ObjectId
     * @return
     */
     T findById(ObjectId id);

    /**
     * 通过ID获取记录
     *
     * @param id
     * @return
     */
     T findById(String id);

    /**
     * 通过条件查询更新数据
     *
     * @param query
     * @param update
     * @return
     */
     void update(Query query, Update update);


    /**
     * 保存一个对象到mongodb
     *
     * @param entity
     * @return
     */
     T save(T entity);

    /**
     * 更新一个对象到mongodb
     *
     * @param entity
     * @return
     */
     T update(T entity);

    /**
     * 插入一个对象到mongodb
     *
     * @param entity
     * @return
     */
     T insert(T entity);

    /**
     * 插入一组对象到mongodb
     *
     * @param entities
     * @return
     */
     List<T> insert(List<T> entities);

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
     Pagination<T> findByPage(Pagination<T> page, Query query);

    /**
     * 求数据总和
     *
     * @param query
     * @return
     */
     long count(Query query);


    /**
     *
     * @param id
     */
    public void deleteById(String id);

}
