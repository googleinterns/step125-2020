// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
 
package com.google.sps.servlets;
 
import java.io.IOException;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import com.google.sps.Marker;
import java.util.Set;
import java.util.List;
import java.util.Base64;
import java.util.UUID;
 
/** Servlet that handles all my received marker data */
@WebServlet("/marker")
public final class MarkerServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
        List<Marker> markers = new ArrayList<>();
        ArrayList<Marker> results = getMarkers(request);
 
        Gson gson = new Gson();
 
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(markers));
    }
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("marker-title");
        Double longitude = Double.parseDouble(request.getParameter("marker-lng"));
        Double latitude = Double.parseDouble(request.getParameter("marker-lat"));
        String description = request.getParameter("marker-description"); 
        UUID id = UUID.randomUUID();
        Set<String> linkSet = new HashSet<String>(Arrays.asList(request.getParameterValues("marker-links")));
        Set<String> categorySet = new HashSet<String>(Arrays.asList(request.getParameterValues("marker-category")));

        Marker postMarker = new Marker(title, description, latitude, longitude, linkSet, categorySet, id);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 
        if (!checkIfMarkerAlreadyInDatastore(id)) {
            Entity markerEntity = postMarker.toEntity();
            datastore.put(markerEntity);
        }
 
        response.sendRedirect("/index.html");
    }
 
    private ArrayList<Marker> getMarkers(HttpServletRequest request){
        ArrayList<Marker> markers = new ArrayList<>();
 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Marker");
        PreparedQuery results = datastore.prepare(query);
 
        for (Entity entity : results.asIterable()) {
            Marker marker = new Marker(entity);
            markers.add(marker);
        }
        return markers;
    }
 
    public Boolean checkIfMarkerAlreadyInDatastore(UUID id){
        Query query = new Query("Marker");
        query.addFilter("id", Query.FilterOperator.EQUAL, id);

        if (query == null) {return false;}
        return true;
    }

    public Entity getEntity(DatastoreService datastore, String title) {
        Query query = new Query("Marker");
        query.addFilter("title", Query.FilterOperator.EQUAL, title); 

        PreparedQuery results = datastore.prepare(query);
        Entity marker = results.asSingleEntity();
        return marker;
    }

    // I removed the flag and votes update methods since the votes (and 
    /**public void updateFlags(Entity markerEntity, DatastoreService datastore, Key markerKey, String newFlag) {
        ArrayList<String> flags = createFlagObject((String) markerEntity.getProperty("flags"));
        flags.add(newFlag);
        //String flagsString = createFlagString(flags);     
        markerEntity.setProperty("flags", flagsString);
        datastore.put(markerEntity);
    }

    public void updateVotes(Entity markerEntity, DatastoreService datastore, Key markerKey) {
        int votes = Integer.parseInt((String) markerEntity.getProperty("votes"));
        votes += 1;
        markerEntity.setProperty("votes", votes);
        datastore.put(markerEntity);
    }*/

}
