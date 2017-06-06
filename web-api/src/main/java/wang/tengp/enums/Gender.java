package wang.tengp.enums;

/**
 * 消息类型
 * Created by shumin on 16-7-23.
 */
public enum Gender {
    Male("男", 1),
    Female("女", 2);

    private String name;
    private int index;

    // 构造方法
    private Gender(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (Gender c : Gender.values()) {
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
