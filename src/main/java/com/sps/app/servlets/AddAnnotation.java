package com.sps.app.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sps.authentication.AuthenticationUtils;
import com.sps.utils.PoemUtils;
import java.io.IOException;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for adding annotations. */
@WebServlet("/new-annotation")
public class AddAnnotation extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {

    String sessionId = request.getSession().getId();
    boolean isUserLoggedIn = AuthenticationUtils.isUserLoggedIn(sessionId);

    if(isUserLoggedIn){
      JsonObject json = new PoemUtils().convertRequestToJson(request);
      final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
      KeyFactory keyFactory = datastore.newKeyFactory().setKind("Annotation");
      FullEntity annotationEntity =
          Entity.newBuilder(keyFactory.newKey())
              .set("poemId", json.get("poemId").getAsLong())
              .set("lineId", json.get("lineId").getAsString())
              .set("annotationText", json.get("annotationText").getAsString())
              .set("addedBy", AuthenticationUtils.getUserInfo(sessionId).getGivenName())
              .set("dateAdded", new Date().getTime())
              .build();

      datastore.put(annotationEntity);
      response.setContentType("application/json;");
      response.setStatus(200);
      response.getWriter().println(gson.toJson("{\"status\": \"ok\", \"code\": 200}"));
    } else {
      response.setStatus(401);
      response.getWriter().println(gson.toJson("{\"status\": \"unauthorized\", \"code\": 401}"));
    }
  }
}
