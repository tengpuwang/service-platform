package wang.tengp.exceptions;

/**
 * 平台异常
 * Created by shumin on 16-9-23.
 */
public class PlatformException extends RuntimeException {

    public PlatformException() {
        super();
    }

    public PlatformException(String e) {
        super(e);
    }

    public PlatformException(Exception e) {
        super(e);
    }

}
