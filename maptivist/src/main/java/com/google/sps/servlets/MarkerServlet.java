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
import com.google.sps.data.Book;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

 
/** Servlet that handles all my received book data */
@WebServlet("/marker")
public final class DataServlet extends HttpServlet {
    
 
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
        List<Marker> markers = new ArrayList<>();
        List<Marker> results = getMarkers(request);
 
        Gson gson = new Gson();
 
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(markers));
    }
 
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String longitude = request.getParameter("longitude"));
        String latitude = request.getParameter("latitude"));
        String description = request.getParameter("description");
        Boolean votes = Boolean.parseBoolean(request.getParameter("upvotes"));

        Set<String> linkSet = new HashSet<String>(Arrays.asList(request.getParameterValues("links")));
        String links = createLinkString(linkSet);
        Set<String> categorySet = new HashSet<String>(Arrays.asList(request.getParameterValues("category")));
        String categories = createCategoriesString(categorySet);
        ArrayList<String> flagSet = new ArrayList<>(Arrays.asList(request.getParameterValues("flags")));
        String flags = createFlagString(flagSet);
 
        if (validateInput(title, longitude, latitude, description, links)) {  
            Entity markerEntity = new Entity("Marker");
            markerEntity.setProperty("title", title);
            markerEntity.setProperty("description", description);
            markerEntity.setProperty("longitude", longitude);
            markerEntity.setProperty("latitude", latitude);
            markerEntity.setProperty("flags", flags);
            markerEntity.setProperty("links", links);
            markerEntity.setProperty("category", categories);
            markerEntity.setProperty("votes", votes);

 
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(markerEntity);
 
            response.sendRedirect("/maps.html");
        } else {
            response.sendRedirect("/maps.html");
        }
    }
 
    private ArrayList<Marker> markers getMarkers(HttpServletRequest request){
        ArrayList<Marker> markers = new ArrayList<>();
 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Marker");
        PreparedQuery results = datastore.prepare(query);
 
        for (Entity entity : results.asIterable()) {
            String title = (String) entity.getProperty("title");
            String description = (String) entity.getProperty("description");
            Double longitude = Double.parseDouble((String) entity.getProperty("longitude"));
            Double latitude = Double.parseDouble((String) entity.getProperty("latitude"));
            Set<String> links = createLinkObject((String) entity.getProperty("links"));
            ArrayList<String> flags = createFlagObject((String) entity.getProperty("flags"));
        
            Marker marker = new Marker(title, description, latitude, longitude, links, categories);
            markers.add(marker);
        }
        return markers;
    }
 
    private Boolean validateInput(String title, String longitude, String latitude, String description, String links) {
        if (title == null) {
            return false;
        }
 
        if (longitude == null){
            return false;
        }

        if (latitude == null){
            return false;
        }

        if (links == null){
            return false;
        }


        if (description == null) {
            return false;
        }        
        
        if (category == null) {
            return false;
        }
 
        return true;
    }

    private String createCategoriesString(Set<String> categories){
        String categoryString;
        Base64.Encoder encoder = Base64.getUrlEncoder();  
        for (String category : categories) {
            String categoryByte = encoder.encodeToString(category.getBytes());  
            categoryString += categoryByte + ",";
        }
        return categoryString.substring(0, categoryString.length() - 1);     
    }

    private Set<String> createCategoryObject(String categoryString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        Set<String> categorySet = new HashSet<String>();
        String[] categories = new String[];
        categories = categoryString.split(",");
        for (category : categories) {
            String category = new String(decoder.decode(category));
            categoryList.add(category);
        }
        return categorySet;
    }

    private String createFlagString(ArrayList<String> flags) {
        String flagString;
        Base64.Encoder encoder = Base64.getEncoder();  
        for (String flag : flags) {
            String comment = encoder.encodeToString(flag.getBytes());  
            flagString += comment + ",";
        }
        return flagString.substring(0, flagString.length() - 1);
    }

    private ArrayList<String> createFlagObject(String flagString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        ArrayList<String> flagsList = new ArrayList<String>();
        String[] flags = new String[];
        flags = flagString.split(",");
        for (flag : flags) {
            String flag = new String(decoder.decode(flag));
            flagsList.add(flag);
        }
        return flagsList;
    }

    private String createLinkString(Set<String> links) {
        String linkString;
        Base64.Encoder encoder = Base64.getUrlEncoder();  
        for (String link : links) {
            String linkUrl = encoder.encodeToString(flag.getBytes());  
            linkString += linkUrl + ",";
        }
        return linkString.substring(0, linkString.length() - 1);
    }

    private Set<String> createLinkObject(String linkString) {
        Base64.Decoder decoder = Base64.getDecoder();  
        Set<String> linkList = new HashSet<String>();
        String[] links = new String[];
        links = linkString.split(",");
        for (link : links) {
            String link = new String(decoder.decode(link));
            linkList.add(link);
        }
        return linkList;
    }
}
 
