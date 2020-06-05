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
  const fact1 = 'I talk in my sleep'
  const fact2 = 'I have a dog named Yuki'
  const fact3 = 'I used to be afraid of ceiling light reflections on the floor'
  const fact4 = 'I live to eat'
  const fact5 = 'Japan is my favourite holiday destination'

  const facts = [fact1, fact2, fact3, fact4, fact5];

  // Pick a random greeting.
  const randomFact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = randomFact;
}
