
// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const searchParameter = urlParams.get('q');

  // Fetch messages and add them to the page.
  function fetchMessages(){
    var url = '/feed';
    if (searchParameter) {
      url += "?search-text=" + searchParameter;
    }
    fetch(url).then((response) => {
      return response.json();
    }).then((messages) => {
      const messageContainer = document.getElementById('message-container');
      if(messages.length == 0){
       messageContainer.innerHTML = '<p>There are no posts yet.</p>';
      }
      else{
       messageContainer.innerHTML = '';
      }
      messages.forEach((message) => {
       const messageDiv = buildMessageDiv(message);
       messageContainer.appendChild(messageDiv);
      });
    });
  }

  // Fetch data and populate the UI of the page.
  function buildUI(){
    addLinks();
    fetchMessages();
  }
