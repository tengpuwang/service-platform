package wang.tengp.filters;

import com.alibaba.fastjson.JSONArray;

import com.google.common.base.Strings;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import spark.Filter;
import spark.Request;
import spark.Response;
import wang.tengp.common.dict.Consts;
import wang.tengp.enums.http.HttpHeader;
import wang.tengp.exceptions.NotAuthorizedException;
import wang.tengp.model.User;

import java.util.Map;

import static spark.Spark.halt;

/**
 * 身份认证过滤器
 * Created by shumin on 16-8-25.
 */
public final class AuthenticationFilter implements Filter {


    private AuthenticationFilter() {
    }

    private static final class SingletonHolder {
        private static final AuthenticationFilter INSTANCE = new AuthenticationFilter();
    }

    public static final AuthenticationFilter getInstance() {
        return AuthenticationFilter.SingletonHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        String auth_token = request.headers(HttpHeader.AUTHORIZATION.headername());
        String access_authority_token = request.headers(HttpHeader.CustomHeader.X_ACCESS_AUTHORITY.headername());
        String user_agent = request.headers(HttpHeader.USER_AGENT.headername());
        // token 不存在
        if (Strings.isNullOrEmpty(auth_token) || Strings.isNullOrEmpty(access_authority_token)) {
            halt(401, "请先申请令牌！");
            // 401
            throw new NotAuthorizedException("请先申请令牌！");
        } else {
            // 验证token
            try {

//                    String secretkey = ApplicationConextUtil.getPropertiesValue("jwt.token.secretkey");

                Jwt auth = Jwts.parser().setSigningKey(user_agent).parse(auth_token);
                Jwt access = Jwts.parser().setSigningKey(user_agent).parse(access_authority_token);

                String user_id = (String) ((Map) auth.getBody()).get("sub");
                String email = (String) ((Map) auth.getBody()).get("iss");

                User user = User.findById(user_id, User.class);
                String access_authority_str = (String) ((Map) access.getBody()).get("sub");
                JSONArray access_authority = JSONArray.parseArray(access_authority_str);
                request.attribute(Consts.AUTH_USER, user);                       // 认证用户
                request.attribute(Consts.AUTH_ROLE, user.getRole());             // 认证角色
                request.attribute(Consts.ACCESS_AUTHORITY, access_authority);    // 访问权限

            } catch (ExpiredJwtException e) {
                // 401
                halt(401, "令牌已失效！");
                throw new NotAuthorizedException("令牌已失效!");
            } catch (Exception e) {
                // 401
                halt(401, "令牌不合法！");
                throw new NotAuthorizedException("令牌不合法！");
            }
        }
    }
}