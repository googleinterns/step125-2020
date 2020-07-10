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
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

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

  // Example Marker
  var myLatlng = {lat: 44.8549, lng: -93.2422}

  var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title: "Justice for George Floyd"
  });

  // Adds the new marker to the map
  marker.setMap(map);
}

/**
* Adds a new Marker and info window to the map
 */

function createMarker() {
  map.addListener('click', function(mapsMouseEvent){
  //var myLatlng = {lat: 44.8549, lng: -93.2422}
    var marker = new google.maps.Marker({
        position: mapsMouseEvent.latLng,
        map: map,
        title:document.getElementsById('marker-title')
    });
    
    createInfowindow(marker);

    // Adds the new marker to the map
    marker.setMap(map);
  });
}

function createInfowindow(marker) {
  //Set info window content from form
  var title = document.getElementsById('marker-title');
  var location = marker.position.toString();
  var description = document.getElementsById('marker-description');
  var link = document.getElementsById('marker-link');
  var category = document.getElementsById('marker-category');
  
  // Set the content of the info window. (A popup may need to be used instead)
  var contentString = "<h1>" + title + "</h1>" + "<h2>" 
  + location + "</h2>" + "<p>" + description + "</p>" + 
  "<button>Upvote</button>" + "<button>Flag</button>" +
  "<h3>" + category + "</h3>";

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });

  infowindow.open(map, marker);
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
