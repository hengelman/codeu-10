

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn && loginStatus.username != parameterUsername) {
          location.replace('/index.html');
        }
      });


  const url = '/directmessages'; 
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>You have no direct messages yet.</p>';
          messagesContainer.classList.add("text-center");
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildDirectMessageDiv(message, parameterUsername);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  addLinks();
  fetchMessages();
}
