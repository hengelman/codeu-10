
/* Builds a message container with the username, timestamp, and date. */
function buildMessageDiv(message){
 const subjectDiv = document.createElement('div');
 subjectDiv.classList.add("left-align");
 subjectDiv.innerText = "Subject: ";
 subjectDiv.appendChild(document.createTextNode(message.subject));

 const usernameDiv = document.createElement('div');
 usernameDiv.classList.add("left-align");
 usernameDiv.appendChild(createLinkMessage('/user-page.html?user=' + message.user, message.user));

 const timeDiv = document.createElement('div');
 timeDiv.classList.add('right-align');
 timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));

 const headerDiv = document.createElement('div');
 headerDiv.classList.add('message-header');
 headerDiv.appendChild(subjectDiv);
 headerDiv.appendChild(usernameDiv);
 headerDiv.appendChild(timeDiv);

 const bodyDiv = document.createElement('div');
 bodyDiv.classList.add('message-body');
 bodyDiv.innerHTML = message.text;

 const messageDiv = document.createElement('div');
 messageDiv.classList.add("message-div");
 messageDiv.appendChild(headerDiv);
 messageDiv.appendChild(bodyDiv);

 return messageDiv;
}
