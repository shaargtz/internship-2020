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
 * Sends the user to a random Youtube video on a different tab.
 */
async function sendToRandomVideo() {
  const response = await fetch('/video');
  const videoJSON = await response.json();
  window.open(videoJSON.videoURL);
}

/**
 * Fetches message from server and adds it to the DOM.
 */
async function fetchWelcomeMessage() {
  const response = await fetch('/data');
  const message = await response.text();
  document.getElementById('welcome-message-container').innerText = message;
}
