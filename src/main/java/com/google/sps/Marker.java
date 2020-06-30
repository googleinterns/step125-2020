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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Marker is the container class for when an unique marker is createsof people are meeting and are therefore
 * busy. The marker's title, description, location, categories are considered read-only. All the other attributes 
 * should be modified by the actions of the viewer users.
 */

public class Marker {
  private final String title;
  private final String description;
  private final Hashtable<String, Double> location = Hashtable<String, Double>(2);
  private final Set<String> categories = new HashSet<String>();
  private Set<String> flags = new HashSet<String>();
  private Set<String> links = new HashSet<String>();
  private int votes = 0;

  /**
   * Creates a new marker.
   *
   * @param title The human-readable title for the marker. Must be non-null.
   * @param description The human-readable description of the marker. Must be non-null.
   * @param categories The collection of categories assigned to the marker. Preferred to be 
   * non-null but if so can be assigned other.
   */

  public Event(String title, String description, Collection<String> links, Collection<String> categories, Hashtable<String, Double> location) {
    if (title == null) {
      throw new IllegalArgumentException("title cannot be null");
    }

    if (description == null) {
      throw new IllegalArgumentException("description cannot be null");
    }

    if (categories == null) {
      throw new IllegalArgumentException("categories cannot be null. Add other to array instead.");
    }

    if (location == null){
       throw new IllegalArgumentException("location cannot be null."); 
    }

    this.title = title;
    this.description = description;
    this.attendees.add("Other");
    this.location = location;
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
  public Set<String> getCategories() {
    // Return the categories as an unmodifiable set so that the caller can't change our
    // internal data.
    return Collections.unmodifiableSet(categories);
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
  public Hashtable<String> getLocation () {
    // Return the location as an unmodifiable set so that the caller can't change our
    // internal data.
    return Collections.unmodifiableSet(location);
  }

  /**
   * Returns a read-only set of flag reports for this marker.
   */
  public Set<String> getFlags () {
    // Return the flag reports as a modifiable set so that the caller can add to our
    // internal data.
    return Collections(flags);
  }

  /**
   * Returns a modifiable integer for the count of upvotes for this Marker.
   */
  public int getVotes () {
    // Return count of upvotes for this marker
    return votes;
  }

  /**Functions I need: turn categories, links, and flags into list
  * Functions I want: send flag alert to admin, check for repeating markers 
  * (based on location and title), check if location coordinates are complete
  

  @Override
  public Set<String> makeCategoriesList(cat1, cat2, cat3, cat4) {
    // For the hash code, just use the title. Most events "should" have different names and will
    // mainly be used as a way to skip the costly {@code equals()} call.
    Set<String> categories = HashSet<String>();

    return categories;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Event && equals(this, (Event) other);
  }

  private static boolean equals(Event a, Event b) {
    // {@code attendees} must be a set for equals to work as expected. According to the {@code Set}
    // interface documentation, equals will check for set-equality across all set implementations.
    return a.title.equals(b.title) && a.when.equals(b.when) && a.attendees.equals(b.attendees);
  }
  */
}