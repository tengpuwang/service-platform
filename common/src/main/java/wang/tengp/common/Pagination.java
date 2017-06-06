package wang.tengp.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页封装
 * Created by shumin on 16-6-16.
 */
public class Pagination<T> {

    /**
     * 分页数据集合
     */
    private List<T> pageDatas;
    /**
     * 分页元数据信息
     */
    private PageInfo pageInfo;


    public Pagination() {

    }

    public Pagination(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Pagination(int pageNo, int pageSize) {
        this.pageInfo = new PageInfo(pageNo, pageSize);
    }


    public List<T> getPageDatas() {
        return pageDatas;
    }

    public void setPageDatas(List<T> pageDatas) {
        this.pageDatas = pageDatas;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }


    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("pageInfo", getPageInfo());
        map.put("pageDatas", getPageDatas());

        return map;
    }

}
