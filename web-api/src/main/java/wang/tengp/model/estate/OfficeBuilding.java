package wang.tengp.model.estate;

import wang.tengp.enums.EstateType;

/**
 * 写字楼
 * Created by shumin on 16-7-23.
 */
public class OfficeBuilding extends Estate {

    private EstateType estateType = EstateType.OfficeBuilding;

    // 类型（0：纯写字楼；1：商住楼；2：商业综合体；3：酒店写字楼）
    private String category;

    // 楼盘
    private String building;

    // 等级（0：普通；）
    private int grade;

    // 是否可以注册公司
    private boolean canRegistryCompany;
}