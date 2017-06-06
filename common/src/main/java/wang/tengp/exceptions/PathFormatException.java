package wang.tengp.exceptions;

/**
 * Created by shumin on 16-10-12.
 */

public class PathFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public PathFormatException() {
        super();
    }

    public PathFormatException(String message) {
        super(message);
    }
}