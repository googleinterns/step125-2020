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
import java.util.Base64;
import java.util.Collection; 
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.nio.CharBuffer; 

/** This is for testing the capabilities of the Marker Class (not started but did add JUnit to pom.xml)*/
@RunWith(JUnit4.class)
public final class MarkerTest {

  // All the different mock examples of marker info obtained by servlet through POST method or sent by querystring
  private final String TITLE = "Justice for Breonna Taylor";

  private final String DESCRIPT = "We will walk to the capital";

  private final double LAT = 32.565;
  private final double LONG = 45.8574;

  private final String CAT_A = "BLM";
  private final String CAT_B = "LGBTQ";
  private final String CAT_C = "Policy";

  private final String LINK_A = "google.com";
  private final String LINK_B = "bing.com";

  private final String FLAG_A = "Hateful, message";
  private final String FLAG_B = "Not real";

  private final int VOTES = 0;

  private final UUID ID = UUID.randomUUID();

  @Test
  public void createMarkerWithoutCategories() {
    Set<String> CATS = new HashSet<>();
    Set<String> LINKS = new HashSet<>();
        LINKS.add(LINK_A);
        LINKS.add(LINK_B);
   
    Marker noCatMarker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);

    Set<String> actual = noCatMarker.getCategories();
    Set<String> expected = new HashSet<String>();
        expected.add("Other");

    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void checkUUID() {
    Set<String> CATS = new HashSet<>();
        CATS.add(CAT_A);
        CATS.add(CAT_B);
        CATS.add(CAT_C);

    Set<String> LINKS = new HashSet<>();
        LINKS.add(LINK_A);
        LINKS.add(LINK_B);

    Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID); 

    UUID actual = marker.getUUID();
    UUID not_expected = null;
    
    Assert.assertNotEquals(not_expected, actual); 
  }

  @Test
  public void addReport() {
    Set<String> CATS = new HashSet<>();
        CATS.add(CAT_A);
        CATS.add(CAT_B);
        CATS.add(CAT_C);

    Set<String> LINKS = new HashSet<>();
        LINKS.add(LINK_A);
        LINKS.add(LINK_B);

    Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);
    marker.addFlagReport(FLAG_A); 

    int actual = marker.getFlags().size();
    int expected = 1;
    
    Assert.assertEquals(expected, actual); 
  }

  @Test
  public void addVotes() {
    Set<String> CATS = new HashSet<>();
        CATS.add(CAT_A);
        CATS.add(CAT_B);
        CATS.add(CAT_C);

    Set<String> LINKS = new HashSet<>();
        LINKS.add(LINK_A);
        LINKS.add(LINK_B);

    Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);
        marker.addVote();

    int actual = marker.getVotes();
    int expected = 1;
    
    Assert.assertEquals(expected, actual);
  }

   @Test
    public void inverseFunctionsWorkTogether() {
        Set<String> CATS = new HashSet<>();
            CATS.add(CAT_A);
            CATS.add(CAT_B);
            CATS.add(CAT_C);

        Set<String> LINKS = new HashSet<>();
            LINKS.add(LINK_A);
            LINKS.add(LINK_B);

        ArrayList<String> FLAGS = new ArrayList<>();
            FLAGS.add(FLAG_A);
            FLAGS.add(FLAG_B);

        Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);

        String linkString = marker.createLinkString(LINKS);
        Set<String> expected = marker.createLinkObject(linkString);
 
        Assert.assertEquals(LINKS, expected);
    }

  @Test
    public void matchingDelimiterCounts() {
        Set<String> CATS = new HashSet<>();
            CATS.add(CAT_A);
            CATS.add(CAT_B);
            CATS.add(CAT_C);

        Set<String> LINKS = new HashSet<>();
            LINKS.add(LINK_A);
            LINKS.add(LINK_B);

        ArrayList<String> FLAGS = new ArrayList<>();
            FLAGS.add(FLAG_A);
            FLAGS.add(FLAG_B);

        Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);

        String categoriesString = marker.createCategoriesString(CATS);
        int expected = 2;
        long actual = categoriesString.chars().filter(delim -> delim == ',').count();
        Assert.assertEquals(actual, expected);
 
    }
 
    @Test
    public void delimiterIncludedInString() {
        Set<String> CATS = new HashSet<>();
            CATS.add(CAT_A);
            CATS.add(CAT_B);
            CATS.add(CAT_C);

        Set<String> LINKS = new HashSet<>();
            LINKS.add(LINK_A);
            LINKS.add(LINK_B);

        ArrayList<String> FLAGS = new ArrayList<>();
            FLAGS.add(FLAG_A);
            FLAGS.add(FLAG_B);

        Marker marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID);
   
        String flagString = marker.createFlagString(FLAGS);
        int expected = 1;
        long actual = flagString.chars().filter(delim -> delim == ',').count();

        Assert.assertEquals(actual, expected);
    }

  
}