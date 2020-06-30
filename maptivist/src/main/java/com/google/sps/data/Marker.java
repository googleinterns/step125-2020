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

package com.google.sps.data;

/** Represents a marker on the map. */
public class Marker {

  private final String title;
  private final String location;
  private final String description;
  private final String link;
  private final String category;
  private final Boolean upvotes;
  private final Boolean flag;

  public Marker(String title, String location, String description, String category) {
    this.title = title;
    this.location = location;
    this.description = description;
    this.category = category;
  }

  public Marker(String title, String location, String description, String category, Boolean upvotes) {
    this.title = title;
    this.location = location;
    this.description = description;
    this.category = category;
    this.upvotes = upvotes++;
  }

  public Marker(String title, String location, String description, String category, Boolean upvotes, Boolean flag) {
    this.title = title;
    this.location = location;
    this.description = description;
    this.category = category;
    this.upvotes = upvotes++;
    this.flag = flag++;
  }

  public String getTitle() {
    return title;
  }

  public String getLocation() {
    return location;
  }

  public String getDescription() {
    return description;
  }

  public String getLink() {
    return link;
  }

  public String getCategory() {
    return category;
  }

  public Boolean getUpvotes() {
    return upvotes;
  }

  public Boolean getFlag() {
    return flag;
  }

}
