package com.sps.app.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.StringValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sps.authentication.AuthenticationUtils;
import com.sps.poem.PoemLine;
import com.sps.utils.PoemUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for adding a new poem. */
@WebServlet("/new-poem")
public class AddPoem extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    String sessionId = request.getSession().getId();
    boolean isUserLoggedIn = AuthenticationUtils.isUserLoggedIn(sessionId);
    
    if(isUserLoggedIn) {
      JsonObject json = new PoemUtils().convertRequestToJson(request);
      JsonArray poemLines = json.get("poemLines").getAsJsonArray();

      final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
      KeyFactory keyFactory = datastore.newKeyFactory().setKind("Poem");
      FullEntity poemEntity =
          Entity.newBuilder(keyFactory.newKey())
              .set("poemTitle", json.get("poemTitle").getAsString())
              .set("poetName", json.get("poetName").getAsString())
              .set("fullText", json.get("fullText").getAsString())
              .set("source", json.get("source").getAsString())
              /**
               * @FIXME - ERROR: The value of property `poemLines` is longer than 1500 bytes
               * This error occurs because the `poemLines` property exceeds 1500 bytes.
               * A workaround is excluding it from the index.
               * Ideally, this property should be stored as array values instead of strings
               */
              .set("poemLines",
                  StringValue.newBuilder(this.ParsePoemLines(poemLines))
                      .setExcludeFromIndexes(true)
                      .build())
              .set("dateAdded", new Date().getTime())
              .build();

      datastore.put(poemEntity);
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson("{\"status\": \"ok\", \"code\": 200}"));
    } else {
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson("{\"status\": \"unauthorized\", \"code\": 401}"));
    }
  }

  /**
   * Parse the poem lines
   * @param lines - A list of the individual poem lines
   * @return - The poem line and their ids
   */
  private String ParsePoemLines(final JsonArray lines) {
    final List<PoemLine> poemLines = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      final long lineId = Math.abs(new Random().nextLong());
      poemLines.add(new PoemLine(lineId, lines.get(i).getAsString()));
    }
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(poemLines);
  }
}
