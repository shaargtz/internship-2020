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
 * Get the styles from another file to improve readability and 
 * for making changes more easily.
 */
import mapStyles from './mapStyles.js'

/**
 * Function that calls other functions needed when the page loads.
 */
window.onBodyLoad = function () {
  fetchComments();
  createMap();

  // Functions to load the Charts API
  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);
}

/**
 * Creates a map and adds it to the page.
 */
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'), {
        center: {lat: 25.650955, lng: -100.289508},
        zoom: 16,
        styles: mapStyles,
      });
}

/**
 * Draws a chart and adds it to the page.
 */
function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('date', 'Year');
  data.addColumn('number', 'Interest');
        data.addRow([new Date(2020, 1), 85]);
        data.addRow([new Date(2020, 2), 85]);
        data.addRow([new Date(2020, 3), 97]);
        data.addRow([new Date(2020, 4), 100]);

  const options = {
    'title': 'Trend for the search "Meme"',
    'height':400
  };

  const chart = new google.visualization.LineChart(
      document.getElementById('chart-container'));
  chart.draw(data, options);
}

/**
 * Sends the user to a random Youtube video on a different tab.
 */
async function sendToRandomVideo() {
  const response = await fetch('/video');
  const videoJSON = await response.json();
  window.open(videoJSON.videoURL);
}

/**
 * Fetches comments from server, wraps each in an <li> element, 
 * and adds them to the DOM.
 */
async function fetchComments() {
  const response = await fetch('/data');
  const commentsObject = await response.json();
  const commentsContainer = document.getElementById('comments-list');
  commentsContainer.innerHTML = '';
  commentsObject.forEach(comment => {
    commentsContainer.appendChild(createListElement(
      comment.text, comment.author));
  });
}

/** 
 * Creates an <li> element containing text and author. 
 * Each element corresponds to a comment to be displayed in the DOM.
 */
function createListElement(text, author) {
  const liElement = document.createElement('li');
  liElement.setAttribute('class', 'list-group-item');
  liElement.innerText = text;
  const smallElement = document.createElement('small');
  smallElement.setAttribute('class', 'text-muted');
  smallElement.innerText = author;
  liElement.appendChild(document.createElement('br'));
  liElement.appendChild(smallElement);
  return liElement;
}
