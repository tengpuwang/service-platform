package wang.tengp.route;

import com.augustl.pathtravelagent.*;
import com.augustl.pathtravelagent.segment.IParametricSegment;
import com.augustl.pathtravelagent.segment.StringSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import wang.tengp.enums.http.HttpMethod;
import wang.tengp.exceptions.PathFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs route mapping and matching using a variant of a radix tree.
 * Matching time is always proportional to the number of path segments in the request regardless of how many routes are present.
 * <p>
 * To use, add all of the routes you wish to have and then call compile().
 * Compile assembles the radix tree to be used for route matching (very fast).
 * Compilation is only necessary once (after all routes have been added).
 *
 * @param <T>
 */
public class RouteMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(RouteMapper.class);

    static final class RouteRequest implements IRequest {
        private final Request request;
        private final List<String> segments;

        public RouteRequest(Request request) throws PathFormatException {
            this.request = request;
            this.segments = DefaultPathToPathSegments.parse(request.pathInfo());
        }

        public Request getRequest() {
            return request;
        }

        @Override
        public List<String> getPathSegments() {
            return segments;
        }
    }

    /**
     * Represents a route match (in the format it will be returned to clients).
     *
     * @param <T>
     */
    public static final class Match<T> {
        private final T data;
        private final Map<String, String> params;
        private final List<String> wildcards;

        public Match(T data, Map<String, String> params, List<String> wildcards) {
            this.data = data;
            this.params = params;
            this.wildcards = wildcards;
        }

        public T getData() {
            return data;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public List<String> getWildcards() {
            return wildcards;
        }

        public String toString() {
            return String.format("Match<>{data=%s, params=%s, wildcards=%s}", data, params, wildcards);
        }
    }

    static final class RouteMatchHandler<T> implements IRouteHandler<RouteRequest, Match<T>> {
        protected final PendingRoute<T> route;
        protected final List<String> routePath;

        public RouteMatchHandler(PendingRoute<T> route) {
            this.route = route;
            this.routePath = route.segments;
        }

        @Override
        public IRouteHandler<RouteRequest, Match<T>> merge(
                IRouteHandler<RouteRequest, Match<T>> other) {
            throw new IllegalStateException("Found duplicate/incompatible routes for path: " + route.method + " " + route.path);
        }

        @Override
        public Match<T> call(RouteMatch<RouteRequest> match) {
            List<String> wildcards = match.getWildcardRouteMatchResult();
            RouteMatchResult routeMatchResult = match.getRouteMatchResult();
            Iterator<String> results = routeMatchResult.getStringMatches().keySet().iterator();

            // Build the mapping of parameter names to values for this match.
            Map<String, String> params = new HashMap<>();

            for (String segment : routePath) {
                if (segment.startsWith(":")) {
                    params.put(segment.substring(1), match.getRouteMatchResult().getStringMatch(results.next()));
                }
            }

            logger.debug("Found Match: data={} segments={} wildcards={}, params={}",
                    route.action, match.getRequest().getPathSegments(), wildcards, params);

            return new Match<>(route.action, params, wildcards);
        }
    }

    /**
     * Represents the route (in the format that it will be provided by clients).
     *
     * @param <T>
     */
    static final class PendingRoute<T> {
        public final HttpMethod method;
        public final String path;
        public final T action;
        public final List<String> segments;

        public PendingRoute(HttpMethod method, String path, T action) throws PathFormatException {
            this.method = method;
            this.path = path;
            this.action = action;
            this.segments = DefaultPathToPathSegments.parse(path);
        }
    }

    @SuppressWarnings("serial")
    public static final class RouteFormatException extends Exception {
        public RouteFormatException(String message, Exception cause) {
            super(message, cause);
        }

        public RouteFormatException(String message) {
            super(message);
        }
    }

    /**
     * A builder used to help assemble the routing radix tree.
     *
     * @param <T_REQ>
     * @param <T_RES>
     */
    static final class RouteTreeNodeBuilder<T_REQ extends IRequest, T_RES> {
        private final String pathPrefix = "/";
        private final Pattern validSegmentChars = Pattern.compile("[\\w\\*\\-\\._~]+");
        public Map<String, RouteTreeNodeBuilder<T_REQ, T_RES>> pathChildren = new HashMap<>();
        public RouteTreeNodeBuilder<T_REQ, T_RES> wildcardChild;
        public RouteTreeNodeBuilder<T_REQ, T_RES> parametricChild;
        public IParametricSegment parametricSegment;
        public IRouteHandler<T_REQ, T_RES> handler;

        public RouteTreeNodeBuilder<T_REQ, T_RES> path(String pathName) {
            pathName = pathName.startsWith(pathPrefix) ? pathName.substring(pathPrefix.length()) : pathName;
            ensureContainsValidSegmentChars(pathName);
            if (pathChildren.containsKey(pathName)) {
                return pathChildren.get(pathName);
            } else {
                RouteTreeNodeBuilder<T_REQ, T_RES> child = new RouteTreeNodeBuilder<>();
                pathChildren.put(pathName, child);
                return child;
            }
        }

        public RouteTreeNodeBuilder<T_REQ, T_RES> parametric() {
            if (wildcardChild != null) {
                //throw new IllegalStateException("Incompatible handler for path.");
            }

            if (parametricChild == null) {
                parametricChild = new RouteTreeNodeBuilder<T_REQ, T_RES>();
                parametricSegment = new StringSegment(UUID.randomUUID().toString());
            }

            return parametricChild;
        }

        public RouteTreeNodeBuilder<T_REQ, T_RES> wildcard() {
            if (parametricChild != null) {
                //throw new IllegalStateException("Incompatible handler for path.");
            }

            if (wildcardChild == null) {
                wildcardChild = new RouteTreeNodeBuilder<T_REQ, T_RES>();
            }

            return wildcardChild;
        }

        public void handler(IRouteHandler<T_REQ, T_RES> handler) {
            if (this.handler != null) {
                throw new IllegalStateException("Duplicate handler for path.");
            }

            this.handler = handler;
        }

        public RouteTreeNode<T_REQ, T_RES> build() {
            return build("::ROOT::");
        }

        private RouteTreeNode<T_REQ, T_RES> build(String nodeName) {
            HashMap<String, RouteTreeNode<T_REQ, T_RES>> children = new HashMap<>();

            for (String key : this.pathChildren.keySet()) {
                children.put(key, this.pathChildren.get(key).build(key));
            }

            return new RouteTreeNode<>(
                    nodeName,
                    this.handler,
                    children,
                    this.parametricChild != null ? new ParametricChild<>(parametricSegment, this.parametricChild.build("::PARAM:" + parametricSegment.getParamName() + "::")) : null,
                    this.wildcardChild != null ? this.wildcardChild.build("::WILDCARD::") : null);
        }

        private void ensureContainsValidSegmentChars(String str) {
            Matcher matcher = validSegmentChars.matcher(str);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Param " + str + " contains invalid characters");
            }
        }
    }

    private DefaultRouteMatcher<RouteRequest, Match<T>> matcher;
    private Map<HttpMethod, RouteTreeNode<RouteRequest, Match<T>>> routes;
    private Map<HttpMethod, List<PendingRoute<T>>> pending;

    public RouteMapper() {
        matcher = new DefaultRouteMatcher<>();
        routes = new EnumMap<>(HttpMethod.class);
        pending = new EnumMap<>(HttpMethod.class);
    }

    /**
     * @param method An HTTP method.
     * @param path   A routing path (e.g. /path/:variable/*).
     * @param value  An object to be returned when this route is matched.
     */
    public synchronized void map(HttpMethod method, String path, T value) throws PathFormatException {

        if (path.equals("*")) {
            map(method, "/", value);
            map(method, "/*", value);
            return;
        }

        if (!pending.containsKey(method)) {
            pending.put(method, new ArrayList<>());
        }

        pending.get(method).add(new PendingRoute<>(method, path, value));
    }

    /**
     * Clears all existing routes.
     */
    public synchronized void clear() {
        pending.clear();
        routes.clear();
    }

    /**
     * Attempts to match an HTTP request to a route.
     *
     * @param request An HTTP request.
     * @return A routing match (if one exists) or null otherwise.
     */
    public Match<T> lookup(Request request) {
        HttpMethod method = HttpMethod.valueOf(request.requestMethod().toUpperCase());
        if (!routes.containsKey(method)) {
            return null;
        }
        try {
            return matcher.match(routes.get(method), new RouteRequest(request));
        } catch (PathFormatException e) {
            return null;
        }
    }

    /**
     * A recursive helper function for assembling the routing radix tree.
     *
     * @param route      The route being processed.
     * @param node       The current node in the tree.
     * @param components An iterator over components of the path.
     * @throws RouteFormatException
     */
    private synchronized void buildTree(PendingRoute<T> route, RouteTreeNodeBuilder<RouteRequest, Match<T>> node, Iterator<String> components) throws RouteFormatException {
        if (!components.hasNext()) {
            node.handler(new RouteMatchHandler<T>(route));
            return;
        }

        String component = components.next();
        RouteTreeNodeBuilder<RouteRequest, Match<T>> newChild;

        if (component.startsWith(":")) {
            //
            newChild = node.parametric();
        } else if (component.equals("*")) {
            if (components.hasNext()) {
//                throw new RouteFormatException("Invalid route format (wildcards may only appear at the end of paths): " + route.method + " " + route.path);
            }
            newChild = node.wildcard();
        } else {
            newChild = node.path(component);
        }

        buildTree(route, newChild, components);
    }

    /**
     * Compiles the routing radix tree.
     * This needs to be called at least once after all routes are added.
     * Must re-compile the radix tree each time routes are modified.
     *
     * @throws RouteFormatException
     */
    public synchronized void compile() throws RouteFormatException {
        routes.clear();

        // 遍历HTTPMethod
        for (HttpMethod method : pending.keySet()) {
            RouteTreeNodeBuilder<RouteRequest, Match<T>> root = new RouteTreeNodeBuilder<>();

            //
            for (PendingRoute<T> route : pending.get(method)) {
                logger.debug("Installing Route: {} {} -> {}", route.method, route.path, route.action);

                try {
                    List<String> components = DefaultPathToPathSegments.parse(route.path);
                    buildTree(route, root, components.iterator());
                } catch (IllegalArgumentException e) {
                    routes.clear();
                    throw new RouteFormatException("Routing path " + method + " " + route.path + " contains illegal characters.");
                } catch (IllegalStateException e) {
                    routes.clear();
                    throw new RouteFormatException("Duplicate/incompatible routing path " + method + " " + route.path + ".");
                }
            }

            logger.debug("Built Route Tree: {} -> {}", method, root.build());
            routes.put(method, root.build());
        }
    }
}
