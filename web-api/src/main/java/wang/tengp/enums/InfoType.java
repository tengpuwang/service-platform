package wang.tengp.enums;

/**
 * 信息类型
 * Created by shumin on 16-7-23.
 */
public enum InfoType {
    Attorn("转让", 1),
    Lease("出租", 2),
    Sell("出售", 3),
    Invite("招商", 4),
    SeekLease("求租", 5),
    SeekSell("求售", 6),
    Other("其他", 7);

    private String name;
    private int index;

    // 构造方法
    private InfoType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (InfoType c : InfoType.values()) {
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
