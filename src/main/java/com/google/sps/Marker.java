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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

/**
 * Marker is the container class for when an unique marker is createsof people are meeting and are therefore
 * busy. The marker's title, description, location, categories are considered read-only. All the other attributes 
 * should be modified by the actions of the viewer users.
 */

public class Marker {
  
  enum Category {
      BLM,
      LGBT,
      Environment,
      Policy,
      Feminism,
      Other;
  }
  // Add a unique id for each marker (UUID)
  private final UUID id;

  private final String title;
  private final String description;

  // Both double attributes provide the coordinates of where the marker is placed
  private final double latitude;
  private final double longitude;

  // The category attribute holds the enum values of the protest categories assigned to the marker
  private Set<Category> categories = new HashSet<Category>();

  // The links attributes holds the multiple urls that are posted with the marker
  private Set<String> links = new HashSet<String>();

  // The flags attribute holds written reports that are posted when the marker is flagged
  private ArrayList<String> flags = new ArrayList<String>();
  
  // The comments attributes holds written comments that are submitted and posted on a marker popup
  private ArrayList<String> comments = new ArrayList<String>();

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
   */

  public Marker(String title, String description, double latitude, double longitude, Set<String> links, Set<Category> categories) {
    
    // Creates a random UUID for the marker can be identified. Can be used as the id for the HTML id
    this.id = UUID.randomUUID();

    if (title == null) {
      throw new IllegalArgumentException("title cannot be null");
    }

    if (description == null) {
      throw new IllegalArgumentException("description cannot be null");
    }

    if (categories.isEmpty()) {
      categories.add(Category.Other);  
      //throw new IllegalArgumentException("categories cannot be null. Other will be added instead.");
    }

    if (latitude < -90 || latitude > 90){
       throw new IllegalArgumentException("this isn't a viable value for latitude"); 
    }

    if (longitude < -180 || longitude > 180){
       throw new IllegalArgumentException("this isn't a viable value for longitude"); 
    }

    this.title = title;
    this.description = description;
    this.categories = categories;
    this.latitude = latitude;
    this.longitude = longitude;
    this.links = links;
    this.votes = 0;
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
   * Returns the human-readable description for when this marker
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns a read-only set of assigned categories for this marker.
   */
  public Set<Category> getCategories() {
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
    // Return the flag reports as a modifiable set so that the caller can add to our
    // internal data.
    return flags;
  }
  
  /**
   * Returns a mutable set of comments for this marker.
   */
  public ArrayList<String> getComments() {
    // Return the comments as a modifiable set so that the caller can add to our
    // internal data.
    return comments;
  }

  /**
   * Returns a modifiable integer for the count of upvotes for this Marker.
   */
  public int getVotes() {
    // Return count of upvotes for this marker
    return votes;
  }

  /**Functions I need:  flags into list
  * Functions I want: send flag alert to admin, increment votes 
  */

  public void addFlagReport(String flagReport) {
    flags.add(flagReport);
  }

  public void addComment(String comment) {
    comments.add(comment);   
  }

  public void addVote() {
    votes++;   
  }

}