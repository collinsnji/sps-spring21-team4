package com.sps.app.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.sps.annotation.Annotation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet responsible for fetching annotations. */
@WebServlet("/annotations")
public class GetAnnotation extends HttpServlet {
  @Override
  public void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws IOException {
    long poemIdParam = Long.parseLong(Jsoup.clean(request.getParameter("poemId"), Whitelist.none()));

    final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    final Query<Entity> query = Query.newEntityQueryBuilder()
                                    .setKind("Annotation")
                                    .setFilter(PropertyFilter.eq("poemId", poemIdParam))
                                    .build();
    final QueryResults<Entity> results = datastore.run(query);
    List<Annotation> annotations = new ArrayList<>();
    while (results.hasNext()) {
      final Entity entity = results.next();
      final long annotationId = entity.getKey().getId();
      final long poemId = entity.getLong("poemId");
      final String lineId = entity.getString("lineId");
      final String annotationText = entity.getString("annotationText");
      final String addedBy = entity.getString("addedBy");
      final long dateAdded = entity.getLong("dateAdded");

      final Annotation annotation =
          new Annotation(poemId, annotationId, lineId, annotationText, addedBy, dateAdded);
      annotations.add(annotation);
    }

    final Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(annotations));
  }
}
