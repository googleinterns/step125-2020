
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
 
 
/** */
@RunWith(JUnit4.class)
public final class MarkerServletFunctionTest {
 
    private static final String[] categoryArray = new String[] {"BLM", "LGBTQ"};
    private static final String[] flagArray = new String[] {"boy", "girl", "cow"};
    private static final String[] linkArray = new String[] {"www.google.com", "www.youtube.com", "www.pinterest.com"};
 
    private static final Set<String> categoryObject = new HashSet<String>(Arrays.asList(categoryArray));
    private static final ArrayList<String> flagObject = new ArrayList<String>(Arrays.asList(flagArray));
    private static final Set<String> linkObject = new HashSet<String>(Arrays.asList(linkArray));
 
    Base64.Decoder decoder = Base64.getDecoder();  
    Base64.Encoder encoder = Base64.getEncoder();  
 
 
    private MarkerServlet testMarkerServletFunctions;
 
    @Before
    public void setUp() {
        testMarkerServletFunctions = new MarkerServlet();
    }
 
    @Test
    public void testCreateCategoriesString() {
        String actual = testMarkerServletFunctions.createCategoriesString(categoryObject);
        String expected = "QkxN,TEdCVFE=";
 
        Assert.assertEquals(expected, actual);
    }
 
    @Test
    public void testCreateCategoriesObject() {
        Set<String> actual = testMarkerServletFunctions.createCategoriesObject("QkxN,TEdCVFE=");
        Set<String> expected = categoryObject;
 
        Assert.assertEquals(expected, actual);
    }
 
    @Test
    public void testCreateFlagString() {
        String actual = testMarkerServletFunctions.createFlagString(flagObject);
        String expected = "Ym95IA==,Z2lybCA=,Y293";
 
        Assert.assertEquals(expected, actual);
    }
 
    @Test
    public void testCreateFlagObject() {
        ArrayList<String> actual = testMarkerServletFunctions.createFlagObject("Ym95IA==,Z2lybCA=,Y293");
        ArrayList<String> expected = flagObject;
         
        Assert.assertEquals(expected, actual);
    }
 
    @Test
    public void testCreateLinkString() {
        String actual = testMarkerServletFunctions.createLinkString(linkObject);
        String expected = "d3d3Lmdvb2dsZS5jb20=,d3d3LnBpbnRlcmVzdC5jb20=,d3d3LnlvdXR!YmUuY29t";
        
        Assert.assertEquals(expected, actual);
    }
 
    @Test
    public void testCreateLinkObject() {
        Set<String> actual = testMarkerServletFunctions.createLinkObject("d3d3Lmdvb2dsZS5jb20=,d3d3LnBpbnRlcmVzdC5jb20=,d3d3LnlvdXR!YmUuY29t");
        Set<String> expected = linkObject;
 
        Assert.assertEquals(expected, actual);
    }
 
 }
