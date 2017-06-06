package wang.tengp.enums;

/**
 * 物业类型
 * Created by shumin on 16-7-23.
 */
public enum EstateType {
    Store("商铺", 1),
    OfficeBuilding("写字楼", 2),
    Factory("厂房", 3),
    Land("地皮", 4),
    StoreHouse("仓库", 5),
    Parking("车位", 6),
    Other("其他", 7);

    private String name;
    private int index;

    // 构造方法
    private EstateType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EstateType c : EstateType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
