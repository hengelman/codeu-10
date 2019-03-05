
/* Builds a message container with the username, timestamp, and date. */
function buildMessageDiv(message){
 const usernameDiv = document.createElement('div');
 usernameDiv.classList.add("left-align");
 usernameDiv.appendChild(document.createTextNode(message.user));

 const timeDiv = document.createElement('div');
 timeDiv.classList.add('right-align');
 timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

 const headerDiv = document.createElement('div');
 headerDiv.classList.add('message-header');
 headerDiv.appendChild(usernameDiv);
 headerDiv.appendChild(timeDiv);

 const bodyDiv = document.createElement('div');
 bodyDiv.classList.add('message-body');
 bodyDiv.appendChild(document.createTextNode(message.text));

 const messageDiv = document.createElement('div');
 messageDiv.classList.add("message-div");
 messageDiv.appendChild(headerDiv);
 messageDiv.appendChild(bodyDiv);

 return messageDiv;
}
