package helpers;

import models.User;
import play.mvc.Result;
import play.mvc.Http;
import play.mvc.Security.Authenticator;

public class ActionAuthenticator extends Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String token = getTokenFromHeader(ctx);
        if (token != null) {
            User user = User.find.where().eq("authToken", token).findUnique();
            if (user != null) {
                return user.getUser();
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get(Constants.Headers.USER_TOKEN);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        }
        return null;
    }
}