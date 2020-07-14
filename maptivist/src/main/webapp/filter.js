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

/**
 * Filters markers based on category
 */

//All distance variable use the unit - Kilometer

async function getMarkersByCategory() {
    const category = document.getElementById("category");
    const markers = await fetch("/marker").then(response => response.json());
    const markersToDisplay = filterByCategory(markers, category);
    return markersToDisplay;
}

function filterByCategory(markers, markerCategory) {
    const markersToDisplay = [];
    for (let marker in markers) {
        if (markers[marker].category.includes(markerCategory) === true) {
            markersToDisplay.push(markers[marker]);
        }
    }
    return markersToDisplay;
}


async function getMarkersByProximity() {
    const userLongitude = document.getElementById("user-longitude");
    const userLatitude = document.getElementById("user-latitude");
    const markerRadius = document.getElementById("marker-radius");
    const markers = await fetch("/marker").then(response => response.json());
    
    const markersToDisplay = filterByProximity(markers, markerRadius, userLongitude, userLatitude);
    return markersToDisplay;
}

function filterByProximity(markers, markerRadius, userLongitude, userLatitude) {

    const markersToDisplay = [];

    for (let marker in markers) {
        if (distanceBetweenTwoCoordinates(markers[marker].longitude, markers[marker].latitude, userLongitude, userLatitude) < markerRadius) {
            markersToDisplay.push(markers[marker]);
        }
    }
    return markersToDisplay;
}

function toRadians(degrees) {
    var pi = Math.PI;
    return degrees * (pi/180);
}

function distanceBetweenTwoCoordinates(longitudeA, latitudeA, longitudeB, latitudeB) {
    longitudeA = toRadians(longitudeA);
    latitudeA = toRadians(latitudeA);
    longitudeB = toRadians(longitudeB);
    latitudeB = toRadians(latitudeB);

    longitudeDifference = longitudeB - longitudeA;
    latitudeDifference = latitudeB - latitudeA;
    
    // using haversine formula
    a = Math.sin(latitudeDifference / 2) ** 2 + Math.cos(latitudeA) * Math.cos(latitudeB) * Math.sin(longitudeDifference / 2) ** 2;
    c = 2 * Math.asin(Math.sqrt(a));

    earthRadius = 6378; 
    distance = earthRadius * c;

    return distance;
}

module.exports = {filterByCategory, filterByProximity};
