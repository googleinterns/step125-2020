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
  loadMarkers();

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

  map.addListener('bounds_changed', function() {
    loadMarkers();
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

/** Adds a new Marker based on form submission
 */

function createMarker(marker) {
  // Get the coordinates from the form input and create a new Latlng object
  var latitude = parseFloat(marker.latitude);
  var longitude = parseFloat(marker.longitude);
  var myLatlng = new google.maps.LatLng(latitude, longitude);
  
  // Create a new marker, it assumed that the position is a private attribute that cannot be accessed
  var new_marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: marker.title    
  });

  // Adds the new marker to the map and pans to the marker 
  new_marker.setMap(map);

  // Adds the new infowindow to the marker
  var infowindow = createInfowindow(new_marker.getPosition());
  new_marker.addListener('click', function() {
    infowindow.open(map, new_marker);
  });
}

/**
* Adds an infowindow based on the marker creation form
 */
function createInfowindow(marker, position) {
  //Set info window content from form
  var title = marker.title;
  var location = position.toString();
  var description = marker.description;
  var link = marker.links;
  var category = marker.categories;
  var upvotes = marker.votes;
  
  // Set the content of the info window 
  var contentString = 
  `<div class="marker-window">
    <h1>${title}</h1>
    <br>
    <h3>${category}</h3>
    <br>
    <h2>${location}</h2>
    <br>
    <p>${description}</p>
    <br>
    <a href=${link}>Related source</a>
    <br>
    <div class="upvote">
        <button>Upvote</button>
        <p>counter: ${upvotes}</p>
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

/** Adds a new Marker based on form submission
 */
function loadMarkers(){
    var map_bounds = map.getBounds();
    var bounds = [
        map_bounds[0].lat(), 
        map_bounds[0].lng(),
        map_bounds[1].lat(), 
        map_bounds[1].lng()
    ]

    document.getElementById("hiddenField").value= bounds;

    fetch('/marker?hiddenField[0]='+ bounds[0] + '&hiddenField[1]='+ bounds[1] +'&hiddenField[2]='+ bounds[2] + '&hiddenField[3]='+ bounds[3])
    .then(response => response.json()).then((markers) => {
        var i = 0;
        for(i = 0; i < markers.length; i++){
            createMarker(markers[i]);
        }
        map.panTo(markers[markers.length -1], new_marker.getPosition());
    });
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