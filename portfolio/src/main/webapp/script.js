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
 * Adds a fact to the page.
 */
function addRandomFact() {

  const facts = [
      'I talk in my sleep',
      'I have a dog named Yuki',
      'I used to be afraid of ceiling light reflections on the floor',
      'I live to eat',
      'Japan is my favourite holiday destination'
  ];

  // Pick a random greeting.
  const randomFact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = randomFact;
}

/**
 * Opens a new tab with the specified url.
 */
function openNewTab(url) {
  window.open(url, '_blank');
}

/**
 * Gets comments from the server and displays them
 * in the comments section of the home page.
 */
function getComments() {
  fetch('/data').then(response => response.json()).then((comments) => {
    const commentsListElement = document.getElementById('comments');
    commentsListElement.innerHTML = '';
    for (let index in comments) {
      commentsListElement.appendChild(createListElement(comments[index]));
    }
  });
}

/**
 * Creates an <li> element containing text.
 */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
