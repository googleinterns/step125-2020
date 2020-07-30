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

//MAPS API

/** The global variable, map, is the initialized primary map for the webpage 
* and should be used for any map parameters or variables.
* The global variable, drawn_markers, is an array of the UUIDs of 
* the markers that were already created*/
var map;

/**
* Intializes the primary map for the main webpage
 */
function initMap(){
  var minneapolis = {lat: 44.9778, lng: -93.2650};

  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 8,
    center: minneapolis,
    mapId: '837a93b1537b2a61'
  });

  // Initializes the search box
  searchBox();
  
  // Adds markers to map
  loadMarkersByBoundary();

  // If the bounds of the map changed markers in that area will be drawn

  map.addListener('bounds_changed', () => {
      loadMarkersByBoundary()
  });
}

/**
* Adds an Auto complete search box to the main map
 */
function searchBox(){
  // Create the search box, info windows for results and link them to their UI elements
  var box = document.getElementById("search-container");
  var input = document.getElementById("search-input");
  var searchBox = new google.maps.places.SearchBox(input);
  var infowindow = new google.maps.InfoWindow();
  var infowindowContent = document.getElementById('infowindow-content');
  infowindow.setContent(infowindowContent);
  
  // Grabs the hidden input values for the coordinates
  var lat = document.getElementById("marker-lat");
  var lng = document.getElementById("marker-lng");
  var address = document.getElementById("marker-address");

  // Add to the map
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(box);
  
  // Set the data fields to return when the user selects a place.
  var autocomplete = new google.maps.places.Autocomplete(input);
  autocomplete.setFields(
    ['formatted_address', 'geometry', 'icon', 'name']);

  autocomplete.addListener('place_changed', () => {
    var place = autocomplete.getPlace();
    if (!place.geometry) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
        }

    // Assign place information to marker attributes
    lat.value = place.geometry.location.lat();
    lng.value = place.geometry.location.lng();
    address.value = place.formatted_address;

    // Concatentates a readable address for an autocomplete result
    var result_address = place.formatted_address;

    // Add the result contents to the infowindow display
    infowindowContent.children['place-icon'].src = place.icon;
    infowindowContent.children['place-name'].textContent = place.name;
    infowindowContent.children['place-address'].textContent = result_address;
    infowindow.open(map);
  }); 
}

/** Fetches markers from the data servlet and runs filter methods
 */
function loadMarkersByBoundary() {
  fetch("/marker").then(response => response.json()).then((markers) => {
    var markersToDisplay = markers.filter(inBoundary);
    //var marker;
    for (const marker of markersToDisplay) {
        drawMarker(marker);          
    }
  });
}

function inBoundary(marker){
  var bounds = map.getBounds().toJSON();
  return (marker.latitude <= bounds.north && 
         marker.latitude >= bounds.south &&
         marker.longitude >= bounds.west &&
         marker.longitude <= bounds.east);
}

/** Adds a new Marker based on fetched JSON array of marker objects
 */
function drawMarker(markerObj) {
  // Get attributes from the marker object and create a new Latlng object
  var title = markerObj.title;
  var myLatlng = new google.maps.LatLng(markerObj.latitude, markerObj.longitude);

  // Create a new marker
  var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: title
  });

  // Adds the new marker to the map 
  marker.setMap(map);

  // Adds the new infowindow to the marker
  var infowindow = drawInfowindow(markerObj);
  marker.addListener('click', ()  => {
    infowindow.open(map, marker);
  });
}

/**

* Adds an infowindow based on the marker object attributes
 */
function drawInfowindow(markerObj) {
  // Set the content of the info window 
  var contentString =
    `<body onload="getVote">
    <div class="marker-window">
    <h1>${markerObj.title}</h1>
    <br>
    <h3>${markerObj.categories}</h3>
    <br>
    <h2>${markerObj.address}</h2>
    <br>
    <p>${markerObj.description}</p>
    <br>
    <a href=${markerObj.links}>Related source</a>
    <br>

    <div class="upvote">
        <p>Counter: <span id="counter"></span></p>
        <button type="submit" id="vote-button" name="id" value="${markerObj.id}" onclick="postVote()">Upvote</button>
    </div>
    <div class="flag">
        <button onclick = "flagFunction() id="info_window_flag_button">Flag</button>
    </div>
  </div>
  </body>`;

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });

  infowindow.domready = function() {
   var flag_btn =   document.getElementById("info_window_flag_button");
   flag_btn.onclick = function() {
      // the closure captures the marker.
      flagFunction(marker);
   }
  }
  
  google.maps.event.addListener(infowindow, 'domready', function() {
    const id = document.getElementById("vote-button").value;
    const params = new URLSearchParams();
    params.append("id", id);
    params.append("update", false);

    fetch('/votes', {method: 'POST', body: params}).then(response => response.json()).then((vote) => {
        document.getElementById("counter").innerHTML = vote;
        console.log(vote);
    });
  });  
  
  return infowindow;  
}


function flagFunction(marker) {
  var flagString = 
    `<div class="flag-window">
		<h1>Enter issue here</h1>
		<input type="text" placeholder="Problem" id="flag-problem" name="flag-problem" required></input>
        <input type="submit" value="Submit">

    </div>`;


	var flagWindow = new google.maps.InfoWindow({
    content: flagString
  });

    flagWindow.setPosition(map.getCenter());
    flagWindow.open(map);

    
}

// POPUP CREATE MARKER FORM
function openForm() {
  var form = document.getElementById("myFormPopup");
  form.style.display = "block";
}

function closeForm() {
  var form = document.getElementById("myFormPopup");
  form.style.display = "none";
}

window.onclick = function(event) {
  var form = document.getElementById("myFormPopup");

  if (event.target == form) {
    form.style.display = "none";
  }
}


  //Firebase
  var firebaseConfig = {
    apiKey: "AIzaSyDa2o12S4k-ILczNuf-WpmeY2zIFoUFxAQ",
    authDomain: "maptivist-step-2020.firebaseapp.com",
    databaseURL: "https://maptivist-step-2020.firebaseio.com",
    projectId: "maptivist-step-2020",
    storageBucket: "maptivist-step-2020.appspot.com",
    messagingSenderId: "1032109305013",
    appId: "1:1032109305013:web:bef8b67db0d5ae091422fc",
    measurementId: "G-E38S0D5C9B"
};

    firebase.initializeApp(firebaseConfig);
    //firebase.analytics();

    
var uid = firebase.auth().currentUser;



function signUp() {
    var email = document.getElementById("email");
    var password = document.getElementById("pass");
    const promise = firebase.auth().createUserWithEmailAndPassword(email.value, password.value);
    promise.catch(e => alert(e.message));
    uid = firebase.auth().currentUser;
  }
  
function signIn(){
    var email = document.getElementById("email");
    var password = document.getElementById("pass");
    const promise = firebase.auth().signInWithEmailAndPassword(email.value, password.value);
    promise.catch(e => alert(e.message));
    uid = firebase.auth().currentUser;

 }

function signOut(){
    firebase.auth().signOut();
    uid = firebase.auth().currentUser;
    signInButtons();
}




function signInOrOpenForm() {
//opens map if signed in, prompts to sign in if not signed in
    if (!uid) {
        openSignIn();
    } 
    else {
        openForm();
    }
}
function signInOrOut() {
//signs out if signed in, prompts to sign in if not signed in
    if (!uid) {
        openSignIn();
    } 
    else {
        signOut();
    }
}


 firebase.auth().onAuthStateChanged(function(user){
    closeLogin();
    closeForm();
    if(!user){
        uid = firebase.auth().currentUser;
        signInButtons();
    }
    else{
        uid = firebase.auth().currentUser;
        signOutButtons();
    }
 });


function signOutButtons(){
    $('#sign-in-or-out-button').html('Sign out');
    $('#authLink').html('Sign out');
    $('#myButton').html('Become a Maptivist');
    $('#myButton').css('display','block');
}

function signInButtons(){
    $('#authLink').html('Sign in here');
    $('#myButton').html('Sign in');
    $('#myButton').css('display','block');
}


function openSignIn() {
  var form = document.getElementById("loginPopup");
  form.style.display = "block";
}

function closeLogin(){
  var form = document.getElementById("loginPopup");
  form.style.display = "none";
}

  function updateSigninStatus(isSignedIn) {
    setSigninStatus();
  }

function postVote() {
  const id = document.getElementById("vote-button").value;
  const params = new URLSearchParams();
  params.append('id', id);
  params.append("update", true);
  fetch('/votes', {method: 'POST', body: params});
}

function postFlag() {
  const id = document.getElementById("flag-button").value;
  const flag = document.getElementById("flag-problem").value;
  const params = new URLSearchParams();
  params.append('flag', flag);
  params.append("id", id);
  fetch('/flags', {method: 'POST', body: params});
  document.getElementById("flag-problem").value = "";

}
