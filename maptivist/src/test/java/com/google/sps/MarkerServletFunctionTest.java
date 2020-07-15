
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
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Base64;
import java.nio.CharBuffer; 
 
 
/** */
@RunWith(JUnit4.class)
public final class MarkerServletFunctionTest {
 
    private static final String[] categoryArray = new String[] {"BLM", "LGBTQ"};
    private static final String[] flagArray = new String[] {"boy, and wo,rld", "girl,", ",cow"};
    private static final String[] linkArray = new String[] {"www.google.com", "www.youtube.com", "www.pinterest.com"};
 
    private static final Set<String> categoryObject = new HashSet<String>(Arrays.asList(categoryArray));
    private static final ArrayList<String> flagObject = new ArrayList<String>(Arrays.asList(flagArray));
    private static final Set<String> linkObject = new HashSet<String>(Arrays.asList(linkArray));
 
    private MarkerServlet testMarkerServletFunctions;
 
    @Before
    public void setUp() {
        testMarkerServletFunctions = new MarkerServlet();
    }
 
    @Test
    public void matchingDelimiterCounts() {
        String categoriesString = testMarkerServletFunctions.createCategoriesString(categoryObject);
        int expected = 1;
        long actual = categoriesString.chars().filter(delim -> delim == ',').count();
        Assert.assertEquals(actual, expected);
 
    }
 
    @Test
    public void delimiterIncludedInString() {
        String flagString = testMarkerServletFunctions.createFlagString(flagObject);
        int expected = 2;
        long actual = flagString.chars().filter(delim -> delim == ',').count();

        Assert.assertEquals(actual, expected);
    }
 
    @Test
    public void inverseFunctionsWorkTogether() {
        String linkString = testMarkerServletFunctions.createLinkString(linkObject);
        Set<String> expected = testMarkerServletFunctions.createLinkObject(linkString);
 
        Assert.assertEquals(linkObject, expected);
    }
 
 }
