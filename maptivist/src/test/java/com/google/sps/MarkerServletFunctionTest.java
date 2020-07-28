
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

import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.Marker;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection; 
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.List;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Base64;
 
@RunWith(JUnit4.class)
public final class MarkerServletFunctionTest {
 
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    // All the different mock examples of marker info obtained by servlet through POST method or sent by querystring
    private final String TITLE = "Justice for Breonna Taylor";

    private final String DESCRIPT = "We will walk to the capital";

    private final String ADDY = "123 Black Lives Matter Plaza, Washington DC 20043";

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

    private final String DATE = "25/12/2019";
    
    @Before
    public void setUp() {
        helper.setUp();
    }

  @After
    public void tearDown() {
        helper.tearDown();
    }

  @Test
  public void setAndUnsetEntity() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    Set<String> CATS = new HashSet<>();
        CATS.add(CAT_A);
        CATS.add(CAT_B);
        CATS.add(CAT_C);

    Set<String> LINKS = new HashSet<>();
        LINKS.add(LINK_A);
        LINKS.add(LINK_B);

    Marker expected_marker = new Marker(TITLE, DESCRIPT, LAT, LONG, LINKS, CATS, ID, DATE);

        expected_marker.addFlagReport(FLAG_A);

    assertEquals(0, ds.prepare(new Query("Marker")).countEntities());
    Entity serialized_marker = expected_marker.toEntity();
    ds.put(serialized_marker);
    Marker deserialized_maker = new Marker(serialized_marker);
    assertEquals(1, ds.prepare(new Query("Marker")).countEntities());

    assertEquals(expected_marker.getUUID(), deserialized_maker.getUUID());
  }
 
 } 
