package com.sps.app.servlets;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;
import com.sps.authentication.AuthenticationUtils;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/login")
public class LoginServlet extends AbstractAuthorizationCodeServlet {
    @Override
    protected String getUserId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return AuthenticationUtils.authFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest request) {
        GenericUrl url = new GenericUrl(request.getRequestURL().toString());
        System.out.println(request.getHeader("referer"));
        request.getSession().setAttribute("redirectURI", request.getHeader("referer"));
        url.setRawPath("/login-callback");
        return url.build();
    }
}
