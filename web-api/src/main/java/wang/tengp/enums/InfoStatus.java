package wang.tengp.enums;

/**
 * 消息类型
 * Created by shumin on 16-7-23.
 */
public enum InfoStatus {
    ONLINE("上线", 1),
    OFFLINE("下线", 2);

    private String name;
    private int index;

    // 构造方法
    private InfoStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (InfoStatus c : InfoStatus.values()) {
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
