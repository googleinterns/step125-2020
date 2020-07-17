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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import com.google.sps.Marker;
import java.util.Set;
import java.util.List;
import java.util.Base64;
 
 
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
        String title = request.getParameter("title");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String description = request.getParameter("description"); 
        Set<String> linkSet = new HashSet<String>(Arrays.asList(request.getParameterValues("links")));
        String links = createLinkString(linkSet);
        Set<String> categorySet = new HashSet<String>(Arrays.asList(request.getParameterValues("category")));
        String categories = createCategoriesString(categorySet);
        String flag = request.getParameter("flags");
        Boolean voteCheck = Boolean.parseBoolean(request.getParameter("upvotes"));
        int votes = 0;
        if (voteCheck) {votes = 1;}
 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 
        if (!checkIfMarkerAlreadyInDatastore(title)) {
 
            Entity markerEntity = new Entity("Marker");
            markerEntity.setProperty("title", title);
            markerEntity.setProperty("description", description);
            markerEntity.setProperty("longitude", longitude);            
            markerEntity.setProperty("latitude", latitude);
            markerEntity.setProperty("flags", flag);
            markerEntity.setProperty("links", links);
            markerEntity.setProperty("category", categories);
            markerEntity.setProperty("votes", votes);
 
 
            datastore.put(markerEntity);
        } else {
            Entity markerEntity = getEntity(datastore, title);
            Key markerKey = markerEntity.getKey();
            if (flag == null) {
                updateFlags(markerEntity, datastore, markerKey, flag);
            }
            
            if (!voteCheck) {
                updateVotes(markerEntity, datastore, markerKey);
            }
        }
 
        response.sendRedirect("/maps.html");
    }
 
    private ArrayList<Marker> getMarkers(HttpServletRequest request){
        ArrayList<Marker> markers = new ArrayList<>();
 
        String[] bounds = request.getParameterValues("hiddenField");
        Double lowLat = Double.parseDouble(bounds[0]);
        Double lowLng = Double.parseDouble(bounds[1]);
        Double highLat = Double.parseDouble(bounds[2]);
        Double highLng = Double.parseDouble(bounds[3]);
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Collection<Query.Filter> filters = new ArrayList<Query.Filter>(Arrays.asList(
            new Query.FilterPredicate("latitude", Query.FilterOperator.GREATER_THAN_OR_EQUAL , lowLat),
            new Query.FilterPredicate("longitude", Query.FilterOperator.GREATER_THAN_OR_EQUAL , lowLng),
            new Query.FilterPredicate("latitude", Query.FilterOperator.LESS_THAN_OR_EQUAL , highLat),
            new Query.FilterPredicate("longitude", Query.FilterOperator.LESS_THAN_OR_EQUAL , highLng)
        ));

        Query query = new Query("Marker").setFilter(new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filters));
        
        PreparedQuery results = datastore.prepare(query);
 
        for (Entity entity : results.asIterable()) {
            String title = (String) entity.getProperty("title");
            String description = (String) entity.getProperty("description");
            Double longitude = Double.parseDouble((String) entity.getProperty("longitude"));
            Double latitude = Double.parseDouble((String) entity.getProperty("latitude"));
            Set<String> links = createLinkObject((String) entity.getProperty("links"));
            ArrayList<String> flags = createFlagObject((String) entity.getProperty("flags"));
            Set<String> categories = createCategoriesObject((String) entity.getProperty("category"));
            int votes = Integer.parseInt((String) entity.getProperty("votes"));
        
            Marker marker = new Marker(title, description, latitude, longitude, links, categories, flags, votes);
            markers.add(marker);
        }
        return markers;
    }
 
    public String createCategoriesString(Set<String> categories){
        String categoryString = "";
        Base64.Encoder encoder = Base64.getEncoder();  
        for (String category : categories) {
            String categoryByte = encoder.encodeToString(category.getBytes());  
            categoryString += categoryByte + ",";
        }
        return categoryString.substring(0, categoryString.length() - 1);     
    }
 
    public Set<String> createCategoriesObject(String categoryString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        Set<String> categorySet = new HashSet<String>();
        String[] categories;
        categories = categoryString.split(",");
        for (String category : categories) {
            String categoryDecode = new String(decoder.decode(category));
            categorySet.add(categoryDecode);
        }
        return categorySet;
    }
 
    public String createFlagString(ArrayList<String> flags) {
        String flagString = "";
        Base64.Encoder encoder = Base64.getEncoder();  
        for (String flag : flags) {
            String comment = encoder.encodeToString(flag.getBytes());  
            flagString += comment + ",";
        }
        return flagString.substring(0, flagString.length() - 1);
    }
 
    public ArrayList<String> createFlagObject(String flagString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        ArrayList<String> flagsList = new ArrayList<String>();
        String[] flags;
        flags = flagString.split(",");
        for (String flag : flags) {
            String flagDecode = new String(decoder.decode(flag));
            flagsList.add(flagDecode);
        }
        return flagsList;
    }
 
    public String createLinkString(Set<String> links) {
        String linkString = "";
        Base64.Encoder encoder = Base64.getUrlEncoder();  
        for (String link : links) {
            String linkUrl = encoder.encodeToString(link.getBytes());  
            linkString += linkUrl + ",";
        }
        return linkString.substring(0, linkString.length() - 1);
    }
 
    public Set<String> createLinkObject(String linkString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        Set<String> linkList = new HashSet<String>();
        String[] links;
        links = linkString.split(",");
        for (String link : links) {
            String linkDecode = new String(decoder.decode(link));
            linkList.add(linkDecode);
        }
        return linkList;
    }

    public Boolean checkIfMarkerAlreadyInDatastore(String title){
        Query query = new Query("Marker");
        query.addFilter("title", Query.FilterOperator.EQUAL, title);

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

    public void updateFlags(Entity markerEntity, DatastoreService datastore, Key markerKey, String newFlag) {
        ArrayList<String> flags = createFlagObject((String) markerEntity.getProperty("flags"));
        flags.add(newFlag);
        String flagsString = createFlagString(flags);     
        markerEntity.setProperty("flags", flagsString);
        datastore.put(markerEntity);
    }

    public void updateVotes(Entity markerEntity, DatastoreService datastore, Key markerKey) {
        int votes = Integer.parseInt((String) markerEntity.getProperty("votes"));
        votes += 1;
        markerEntity.setProperty("votes", votes);
        datastore.put(markerEntity);
    }

}
