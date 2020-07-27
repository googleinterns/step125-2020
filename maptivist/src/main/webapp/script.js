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
            <button onclick="flagFunction()">Flag</button>
        </div>
    </div>`;

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });
  
  marker.addListener('click', function() {
    infowindow.open(map, marker);
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

function createMarker() {
  // Get the coordinates from the form input and create a new Latlng object
  var latitude = parseFloat(document.getElementById('marker-lat').value);
  var longitude = parseFloat(document.getElementById('marker-lng').value);
  var myLatlng = new google.maps.LatLng(latitude, longitude);
  
  // Create a new marker, it assumed that the position is a private attribute that cannot be accessed
  var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: document.getElementById('marker-title').value    
  });

  // Adds the new marker to the map and pans to the marker 
  marker.setMap(map);
  map.panTo(marker.getPosition());

  // Adds the new infowindow to the marker
  var infowindow = createInfowindow(marker.getPosition());
  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}

/**
* Adds an infowindow based on the marker creation form
 */
function createInfowindow(position, marker) {
  //Set info window content from form
  var title = document.getElementById('marker-title').value;
  var location = position.toString();
  var description = document.getElementById('marker-description').value;
  var link = document.getElementById('marker-link').value;
  var category = document.getElementById('marker-category').value;
  
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
        <p>counter: </p>
    </div>
    <div class="flag">
        <button onclick = "flagFunction() id="info_window_flag_button">Flag</button>
    </div>
  </div>`;


  infowindow.domready = function() {
   var flag_btn =   document.getElementById("info_window_flag_button");
   flag_btn.onclick = function() {
      // the closure captures the marker.
      flagFunction(marker);
   }
}
  var infowindow = new google.maps.InfoWindow({
    content: contentString
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
/*
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


      
    });
  }

  function signInOrOut() {
    //basic sign in/sign out functions
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
      $('#authLink').html('Sign out');
      $('#myButton').html('Become a Maptivist');
      $('#myButton').css('display','block');
    } else {
      $('#authLink').html('Sign in here');
      $('#myButton').html('Sign in');
      $('#myButton').css('display','block');
    }
  }


  function updateSigninStatus(isSignedIn) {
    setSigninStatus();
  }

*/
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




function signInOrOut2() {
//opens map if signed in, prompts to sign in if not signed in
    if (!uid) {
        openSignIn();
    } else {
        openForm();
        }
}
function signInOrOut() {
//opens map if signed in, prompts to sign in if not signed in
    if (!uid) {
        openSignIn();

    } else {
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

function closeLogin() {
  var form = document.getElementById("loginPopup");
  form.style.display = "none";
}
