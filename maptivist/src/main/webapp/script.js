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

/** The global map variable is the initialized primary map for the webpage 
and should be used for any map parameters or variables */
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

  // Example Marker
  var myLatlng = {lat: 44.8549, lng: -93.2422}

  var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: "Justice for George Floyd"
  });

  // Adds the new marker and infowindow to the map
  marker.setMap(map);
  var contentString =
    `<div class="marker-window">
        <h1>Justice for George Floyd</h1>
        <br>
        <h3>BLM</h3>
        <br>
        <h2>Mall of America</h2>
        <br>
        <p>This is an example marker from a past protest</p>
        <br>
        <a href="https://en.wikipedia.org/wiki/George_Floyd">Related source</a>
        <br>
        <div class="upvote">
            <button>Upvote</button>
            <p>counter: </p>
        </div>
        <div class="flag">
            <button>Flag</button>
        </div>
    </div>`;

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });
  
  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });

  map.addListener('bounds_changed', function(){
    let markers = getMarkersByBoundary();
    for (let marker in markers){
        createMarker(marker);
    }
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

  // Add to the map
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(box);
  
  // Set the data fields to return when the user selects a place.
  var autocomplete = new google.maps.places.Autocomplete(input);
  autocomplete.setFields(
    ['formatted_address', 'geometry', 'icon', 'name']);

  autocomplete.addListener('place_changed', function() {
    var place = autocomplete.getPlace();
    if (!place.geometry) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
        }

    // If the place has a geometry, then present it on a map.
    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(17);  
    }
    
    // Concatentates a readable address for an autocomplete result
    var address = place.formatted_address;

    // Add the result contents to the infowindow display
    infowindowContent.children['place-icon'].src = place.icon;
    infowindowContent.children['place-name'].textContent = place.name;
    infowindowContent.children['place-address'].textContent = address;
    infowindow.open(map);
  }); 
}

/** Fetches markers from the data servlet and runs filter methods
 */
function getMarkersByBoundary() {
  fetch("/marker").then(response => response.json()).then( (markers) => {
    console.log("Marker JSON: " + markers);
    var markersToDisplay =  markers.filter(inBoundary);
    return markersToDisplay;
  });
}

function inBoundary(marker){
  var bounds = map.getBounds().toJSON();
  if (marker.latitude <= bounds.north && marker.latitude >= bounds.south){
    if (marker.longitude <= bounds.west && marker.longitude >= bounds.east){
        return true;
    }else{
        return false;
    }   
  }
  return false;
}

/** Adds a new Marker based on form submission
 */
function createMarker({latitude, longitude, title} ) {
  // Get the coordinates from the form input and create a new Latlng object
  var marker_title = markerObj.title;
  var latitude = markerObj.latitude;
  var longitude = markerObj.longitude;
  var myLatlng = new google.maps.LatLng(latitude, longitude);
  
  // Create a new marker, it assumed that the position is a private attribute that cannot be accessed
  var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: marker_title
  });

  // Adds the new marker to the map and pans to the marker 
  marker.setMap(map);
  //map.panTo(marker.getPosition());

  // Adds the new infowindow to the marker
  var infowindow = createInfowindow(markerObj, marker.getPosition());
  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}

/**
* Adds an infowindow based on the marker creation form
 */
function createInfowindow({title, description, links, categories}, position) {
  //Set info window content from form
  var title = markerObj.title;
  var location = position.toString();
  var description = markerObj.description;
  var links = markerObj.links;
  var categories = markerObj.categories;
  
  // Set the content of the info window 
  var contentString = 
  `<div class="marker-window">
    <h1>${title}</h1>
    <br>
    <h3>${categories}</h3>
    <br>
    <h2>${location}</h2>
    <br>
    <p>${description}</p>
    <br>
    <a href=${links}>Related source</a>
    <br>
    <div class="upvote">
        <button>Upvote</button>
        <p>counter: </p>
    </div>
    <div class="flag">
        <button>Flag</button>
    </div>
  </div>`;

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });

  return infowindow;  
}

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

//GOOGLE OAUTH

var GoogleAuth;
  var SCOPE = 'https://www.googleapis.com/auth/userinfo.email';
  function handleClientLoad() {
    gapi.load('client:auth2', initClient);
  }

  function initClient() {
    var discoveryUrl = 'https://people.googleapis.com/$discovery/rest?version=v1'

    gapi.client.init({
        'apiKey': 'AIzaSyAxjgLiAauEKT35UoAhinExXFUvQSCHTKM',
        'clientId': '1032109305013-hmru372nf0vslo52b0aqq2kpf20m88mb.apps.googleusercontent.com',
        'discoveryDocs': [discoveryUrl],
        'scope': SCOPE
    }).then(function () {
      GoogleAuth = gapi.auth2.getAuthInstance();

      GoogleAuth.isSignedIn.listen(updateSigninStatus);
      var user = GoogleAuth.currentUser.get();
      setSigninStatus();


      $('#sign-in-or-out-button').click(function() {
        handleAuthClick();
      });
      $('#revoke-access-button').click(function() {
        revokeAccess();
      });
    });
  }

  function handleAuthClick() {
    if (GoogleAuth.isSignedIn.get()) {

      GoogleAuth.signOut();
    } else {

      GoogleAuth.signIn();
    }
  }

  function revokeAccess() {
    GoogleAuth.disconnect();
  }

  function setSigninStatus(isSignedIn) {
    var user = GoogleAuth.currentUser.get();
    var isAuthorized = user.hasGrantedScopes(SCOPE);
    if (isAuthorized) {
      $('#sign-in-or-out-button').html('Sign out');
      $('#myButton').css('display', 'block');
      $('#auth-status').html('You are currently signed in and have granted ' +
          'access to this app.');
    } else {
      $('#sign-in-or-out-button').html('Sign In/Authorize');
      $('#myButton').css('display', 'none');
      $('#auth-status').html('You have not authorized this app or you are ' +
          'signed out.');
    }
  }

  function updateSigninStatus(isSignedIn) {
    setSigninStatus();
  }