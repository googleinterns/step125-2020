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

package com.google.sps;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

/**
 * Marker is the container class for when an unique marker is creates of people are meeting and are therefore
 * busy. The marker's title, description, location, categories are considered read-only. All the other attributes 
 * should be modified by the actions of the viewer users.
 */

public class Marker {
  
  // Add a unique id for each marker (UUID)
  private final UUID id;

  private final String title;
  private final String address;
  private final String description;

  // Both double attributes provide the coordinates of where the marker is placed
  private final double latitude;
  private final double longitude;

  // The category attribute holds the enum values of the protest categories assigned to the marker
  private Set<String> categories = new HashSet<String>();

  // The links attributes holds the multiple URLs that are posted with the marker
  private Set<String> links = new HashSet<String>();

  // The flags attribute holds written reports that are posted when the marker is flagged
  private ArrayList<String> flags = new ArrayList<String>();
  
  // The votes attribute is a mutable counter for the number of upvotes a marker has gotten
  private int votes;
  
  /**
   * Creates a new marker.
   *
   * @param title The human-readable title for the marker. Must be non-null.
   * @param description The human-readable description of the marker. Must be non-null.
   * @param latitude The numerical value of the marker's latitude position. Must be non-null. 
   * @param longitude The numerical value of the marker's longitude position. Must be non-null.
   * @param categories The collection of categories assigned to the marker. Preferred to be 
   * non-null but if so can be assigned other.
   * @param links Collected list of all the urls connected to the specific marker and can be null.
   * @param id 128 bit UUID, must be non-null.
   */

  public Marker(String title, String description, String address, double latitude, double longitude, Set<String> links, Set<String> categories, UUID id) {

    if (title == null) {
      throw new IllegalArgumentException("title cannot be null");
    }

    if (description == null) {
      throw new IllegalArgumentException("description cannot be null");
    }

    if (address == null) {
      throw new IllegalArgumentException("address cannot be null");
    }
    
    if (categories.isEmpty()) {
      categories.add("Other");  
    }

    if (latitude < -90 || latitude > 90){
       throw new IllegalArgumentException("this isn't a viable value for latitude"); 
    }

    if (longitude < -180 || longitude > 180){
       throw new IllegalArgumentException("this isn't a viable value for longitude"); 
    }
    
    if (id == null) {
      throw new IllegalArgumentException("id cannot be null");
    }

    this.title = title;
    this.description = description;
    this.address = address;
    this.categories = categories;
    this.latitude = latitude;
    this.longitude = longitude;
    this.links = links;
    this.flags = new ArrayList<String>();
    this.votes = 0;
    this.id = id;
  }

  public Marker(Entity entity) {
    Set<String> links_object = createLinkObject((String) entity.getProperty("links"));
    ArrayList<String> flags_object = createFlagObject((String) entity.getProperty("flags"));
    Set<String> categories_object = createCategoriesObject((String) entity.getProperty("category"));

    this.title = (String) entity.getProperty("title");
    this.description = (String) entity.getProperty("description");
    this.address = (String) entity.getProperty("address");
    this.longitude = (Double) entity.getProperty("longitude");
    this.latitude = (Double) entity.getProperty("latitude");
    this.id = (UUID) UUID.fromString((String) entity.getProperty("id"));
    this.links = links_object;
    this.flags = flags_object;
    this.categories = categories_object;
  }

  /**
   * Returns the randomized id for this marker.
   */
  public UUID getUUID(){
      return id;
  }

  /**
   * Returns the human-readable title for this marker.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the human-readable description for this marker
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the human-readable address for this marker
   */
  public String getAddress() {
    return address;
  }

  /**
   * Returns a read-only set of assigned categories for this marker.
   */
  public Set<String> getCategories() {
    // Return the categories as an unmodifiable set so that the caller can't change our
    // internal data.
    return categories;
  }

  /**
   * Returns a read-only set of related links for this marker.
   */
  public Set<String> getLinks () {
    // Return the links as an unmodifiable set so that the caller can't change our
    // internal data.
    return Collections.unmodifiableSet(links);
  }

  /**
   * possibly needs to be fixed but Returns a read-only set for the coordinates of the marker.
   */
  public double getLat () {
    // Return the location as an unmodifiable set so that the caller can't change our
    // internal data.
    return latitude;
  }

  public double getLong () {
    // Return the location as an unmodifiable set so that the caller can't change our
    // internal data.
    return longitude;
  }

  /**
   * Returns a mutable set of flag reports for this marker.
   */
  public ArrayList<String> getFlags () {
    // Return the flag reports as a read-only ArrayList set so that the caller can add to our
    // internal data.
    return flags;
  }

  /**
   * Returns a modifiable integer for the count of upvotes for this Marker.
   */
  public int getVotes() {
    // Return count of upvotes for this marker
    return votes;
  }
  
  // This method adds a flag report to ArrayList (repeated reports are permitted)
  public void addFlagReport(String flagReport) {
    flags.add(flagReport);
  }
  
  // This method increments the upvote counter when it's called
  public void addVote() {
    votes++;   
  }

  // This method turns the atrributes of the marker object into properties of the Marker Entity
  public Entity toEntity() {
    String categories_string = createCategoriesString(this.categories);
    String links_string = createLinkString(this.links);
    String flags_string = "";
    String id_string = this.id.toString();

    Entity markerEntity = new Entity("Marker");
    markerEntity.setProperty("title", this.title);
    markerEntity.setProperty("description", this.description);
    markerEntity.setProperty("address", this.address);
    markerEntity.setProperty("longitude", this.longitude);            
    markerEntity.setProperty("latitude", this.latitude);
    markerEntity.setProperty("flags", flags_string);
    markerEntity.setProperty("links", links_string);
    markerEntity.setProperty("category", categories_string);
    markerEntity.setProperty("votes", this.votes);
    markerEntity.setProperty("id", id_string);

    return markerEntity;
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
 
  public static String createFlagString(ArrayList<String> flags) {
        String flagString = "";
        Base64.Encoder encoder = Base64.getEncoder(); 
        for (String flag : flags) {
            String comment = encoder.encodeToString(flag.getBytes());  
            flagString += comment + ",";
        }
        return flagString.substring(0, flagString.length() - 1);
    }

    public static ArrayList<String> createFlagObject(String flagString) {
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

    public static Entity getEntity(DatastoreService datastore, String id) {
        Query query = new Query("Marker"); 
        query.addFilter("id", FilterOperator.EQUAL, id); 
        PreparedQuery pq = datastore.prepare(query);    
        Entity entity = pq.asSingleEntity();

        return entity;
    }
}
