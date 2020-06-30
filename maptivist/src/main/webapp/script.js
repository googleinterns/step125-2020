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
function getMarkersByCategory() {
    fetch("/marker").then(response => response.json()).then((markers) => {
        console.log(markers);
        const markersToDisplay = filterCategories(markers);
    })
    return markersToDisplay;
}

function getMarkersByLocation() {
    fetch("/marker").then(response => response.json()).then((images) => {
    console.log(images);
    const markersToDisplay = filterCategories(markers);
    imageList.innerText = "";
    for (i = 0; i < images.length; i++) {
        imageList.appendChild(createImageList(images[i]["uploadUrl"]));
    }
  })
}

function filterCategories(markers){
    const category = document.getElementById("category");
    const markersToDisplay;
    for (i = 0; i < markers.length; i++) {
        if (markers[i]["category"] == category){
            markersToDisplay.push(markers[i]);
        }
    }
    return markersToDisplay;    
}