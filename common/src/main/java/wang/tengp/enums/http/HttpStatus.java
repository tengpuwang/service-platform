package wang.tengp.enums.http;

import com.google.common.collect.ImmutableMap;
import wang.tengp.exceptions.*;

import java.util.Map;

/**
 * HttpStatus HTTP 状态码
 * Created by shumin on 16/6/16.
 */
public enum HttpStatus {

    //  2xx
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),

    //  3xx
    MOVED_PERMANENTLY(301),
    MOVE_TEMPORARILY(302),
    NOT_MODIFIED(304),

    //  4xx
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),

    //  5xx
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    private final int code;

    private static final Map<Integer, HttpStatus> codeMapper = ImmutableMap.<Integer, HttpStatus>builder()

            .put(200, OK)
            .put(201, CREATED)
            .put(202, ACCEPTED)
            .put(204, NO_CONTENT)

            .put(301, MOVED_PERMANENTLY)
            .put(302, MOVE_TEMPORARILY)
            .put(304, NOT_MODIFIED)

            .put(400, BAD_REQUEST)
            .put(401, UNAUTHORIZED)
            .put(403, FORBIDDEN)
            .put(404, NOT_FOUND)
            .put(405, METHOD_NOT_ALLOWED)
            .put(406, NOT_ACCEPTABLE)

            .put(500, INTERNAL_SERVER_ERROR)
            .put(501, NOT_IMPLEMENTED)
            .put(502, BAD_GATEWAY)
            .put(503, SERVICE_UNAVAILABLE)
            .put(504, GATEWAY_TIMEOUT)

            .build();

    private HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    private static final Map<Class<? extends Throwable>, HttpStatus> translator = ImmutableMap.<Class<? extends Throwable>, HttpStatus>builder()
            .put(AccessViolationException.class, HttpStatus.FORBIDDEN)
            .put(BadRequestException.class, HttpStatus.BAD_REQUEST)
            .put(InternalServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR)
            .put(MethodNotAllowedException.class, HttpStatus.METHOD_NOT_ALLOWED)
            .put(NotAuthorizedException.class, HttpStatus.UNAUTHORIZED)
            .put(NotFoundException.class, HttpStatus.NOT_FOUND)
            .put(NotImplementedException.class, HttpStatus.NOT_IMPLEMENTED)
            .build();

    public static HttpStatus fromException(Throwable e) {
        return fromExceptionClass(e.getClass());
    }

    public static HttpStatus fromExceptionClass(Class<? extends Throwable> clazz) {
        if (translator.containsKey(clazz)) {
            return translator.get(clazz);
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static HttpStatus valueOf(int code) {
        return codeMapper.get(code);
    }
}
