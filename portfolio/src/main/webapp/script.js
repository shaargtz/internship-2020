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
function sendToRandomVideo() {
  const videos = [
    "https://www.youtube.com/watch?v=Ng_Im-qsWzc&list=LLGpjNAdp_solk9m7e4SaPsg&index=8",
    "https://www.youtube.com/watch?v=sYd_-pAfbBw&list=LLGpjNAdp_solk9m7e4SaPsg&index=48",
    "https://www.youtube.com/watch?v=xuCn8ux2gbs&list=LLGpjNAdp_solk9m7e4SaPsg&index=61",
    "https://www.youtube.com/watch?v=huEtJw7pfLk&list=LLGpjNAdp_solk9m7e4SaPsg&index=68",
    "https://www.youtube.com/watch?v=Uu5zGHjRaMo&list=LLGpjNAdp_solk9m7e4SaPsg&index=22",
  ];

  const randomVideo = videos[Math.floor(Math.random() * videos.length)];

  window.open(randomVideo);
}

/**
 * Fetches comments from server and adds them to the DOM.
 */
async function fetchComments() {
  const response = await fetch('/data');
  const commentsObject = await response.json();
  const commentsContainer = document.getElementById('comments-container')
      .innerHTML = '';
  commentsObject.commentList.forEach(comment => {
    commentsContainer.appendChild(createListElement(comment));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
