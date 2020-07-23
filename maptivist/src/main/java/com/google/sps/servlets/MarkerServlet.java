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
        String email = request.getParameter("email");
        String title = request.getParameter("marker-title");
        String longitude = request.getParameter("marker-lng");
        String latitude = request.getParameter("marker-lat");
        String description = request.getParameter("marker-description"); 
        Set<String> linkSet = new HashSet<String>(Arrays.asList(request.getParameterValues("marker-link")));
        String links = createLinkString(linkSet);
        Set<String> categorySet = new HashSet<String>(Arrays.asList(request.getParameterValues("marker-category")));
        String categories = createCategoriesString(categorySet);
        String flag = request.getParameter("flags");
        int votes = 1; //  It is assumed that the creator of the marker would vote for it. Changes to voteChecking will made added.
 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        String ID = Integer.toString(title.hashCode());
        Entity entity = new Entity("Marker", ID);
            entity.setProperty("title", title.trim());
            entity.setProperty("description", description.trim());
            entity.setProperty("longitude", longitude.trim());     
            entity.setProperty("latitude", latitude.trim());
            entity.setProperty("flags", flag);
            entity.setProperty("links", links);
            entity.setProperty("category", categories);
            entity.setProperty("votes", votes);

        datastore.put(entity);
 
        response.sendRedirect("/index.html");
    }
 
    private ArrayList<Marker> getMarkers(HttpServletRequest request){
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

}
