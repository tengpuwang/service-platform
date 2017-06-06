package wang.tengp.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页信息
 */
public class PageInfo {

    /**
     * 一页数据条数
     */
    private int pageSize;
    /**
     * 当前页码
     */
    private int pageNo;

    /**
     * 一共有多少条数据
     */
    private long totalCount;

    /**
     * 一共有多少页
     */
    private int totalPage;

    /**
     * 上一页
     */
    private int prePage;

    /**
     * 下一页
     */
    private int nextPage;


    public PageInfo() {
    }


    public PageInfo(int pageNo, int pageSize) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }


    public int getPageSize() {
        return pageSize;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getPageNo() {
        return pageNo;
    }


    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }


    public long getTotalCount() {
        return totalCount;
    }


    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;

        // 计算总页数
        calTotalPage();
        calPrePage();
        calNextPage();
    }


    public int getTotalPage() {
        return totalPage;
    }


    public int getPrePage() {
        return prePage;
    }


    public int getNextPage() {
        return nextPage;
    }


    /**
     * 获取第一条记录位置
     *
     * @return
     */
    public int getFirstResultIndex() {
        return (this.pageNo - 1) * this.pageSize;
    }


    /**
     * 获取最后记录位置
     *
     * @return
     */
    public int getLastResultIndex() {
        return this.pageNo * this.pageSize - 1;
    }


    /**
     * 计算一共多少页
     */
    private void calTotalPage() {
        this.totalPage = (int) ((this.totalCount % this.pageSize > 0) ? (this.totalCount / this.pageSize + 1)
                : this.totalCount / this.pageSize);
    }


    /**
     * 计算上一页
     */
    private void calPrePage() {
        this.prePage = (this.pageNo > 1) ? this.pageNo - 1 : this.pageNo;
    }


    /**
     * 计算下一页
     */
    private void calNextPage() {
        this.nextPage = (this.pageNo == this.totalPage) ? this.pageNo : this.pageNo + 1;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", getPageSize());
        map.put("pageNo", getPageNo());
        map.put("totalCount", getTotalCount());
        map.put("totalPage", getTotalPage());
        return map;
    }
}