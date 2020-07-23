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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
        Gson gson = new Gson();
 
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(Globals.voteCount));
    }
 
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String titleMatch = request.getParameter("title");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        updateVotes(datastore, titleMatch);

        response.sendRedirect("/index.html");
    }
 

    public Entity getEntity(DatastoreService datastore, String titleMatch) {
        Query query = new Query("Marker"); 
        query.addFilter("title", FilterOperator.EQUAL, titleMatch); 
        PreparedQuery pq = datastore.prepare(query);    
        Entity entity = pq.asSingleEntity();

        return entity;
    }

    public void updateVotes(DatastoreService datastore, String titleMatch) {
        Entity entity = getEntity(datastore, titleMatch);
        int votes = (int) (long) entity.getProperty("votes");

        votes += 1;
        Globals.voteCount = Integer.toString(votes);
        entity.setProperty("votes", votes);

        datastore.put(entity);        
    }

    static class Globals {
        public static String voteCount;
    }

}
