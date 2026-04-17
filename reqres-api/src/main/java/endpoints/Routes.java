package endpoints;

public class Routes {
    // Base URL is appended by RestAssured dynamically
    
    // User endpoints
    public static final String POST_USER = "/api/users";
    public static final String GET_USER = "/api/users/{id}";
    public static final String PUT_USER = "/api/users/{id}";
    public static final String DELETE_USER = "/api/users/{id}";

    // Complex test endpoints
    public static final String POST_LOGIN = "/api/login";
    public static final String GET_USERS_DELAYED = "/api/users";
}
