/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing every user page.
 */
function showMessageFormIfLoggedIn() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn) {
          const messageForm = document.getElementById('message-form');
          messageForm.action = '/messages?recipient=' + parameterUsername;
          messageForm.classList.remove('hidden');
        }
      });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
          messagesContainer.classList.add("text-center");
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);

          const deleteButton = document.createElement('button');
          deleteButton.value = message.id;
          deleteButton.name = "message-id";
          deleteButton.innerText = "Delete";
          deleteButton.classList.add("btn");
          deleteButton.classList.add("btn-outline-dark");

          const deleteForm = document.createElement('form');
          deleteForm.action = "/delete";
          deleteForm.method = "GET";
          deleteForm.appendChild(deleteButton);

          messageDiv.firstChild.appendChild(deleteForm);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  addLinks();
  setPageTitle();
  showMessageFormIfLoggedIn();
  fetchMessages();
  const config = { removePlugins: ['ImageUpload', 'Table', 'MediaEmbed'] };
  ClassicEditor.create(document.getElementById('message-input'), config);
}
