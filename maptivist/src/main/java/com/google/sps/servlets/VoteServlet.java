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
@WebServlet("/votes")
public final class VoteServlet extends HttpServlet {
    
    private static String voteCount;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
        Gson gson = new Gson();
 
        response.setContentType("text/html;");
        response.getWriter().println(voteCount);
    }
 
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean voteCheck = Boolean.parseBoolean(request.getParameter("vote-choice"));
        String title = request.getParameter("title");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        
        voteCount = Integer.toString(updateVotes(datastore, title));
        response.sendRedirect("/index.html");
    }
 

    public Entity getEntity(DatastoreService datastore, String title) {
        Query query = new Query("Marker");
        query.addFilter("title", Query.FilterOperator.EQUAL, title); 

        PreparedQuery results = datastore.prepare(query);
        Entity marker = results.asSingleEntity();
        return marker;
    }

    public int updateVotes(DatastoreService datastore, String title) {
        Entity markerEntity = getEntity(datastore, title);
        Key markerKey = markerEntity.getKey();
        int votes = Integer.parseInt((String) markerEntity.getProperty("votes"));
        votes += 1;
        markerEntity.setProperty("votes", votes);
        datastore.put(markerEntity);
        return votes;
    }

}
