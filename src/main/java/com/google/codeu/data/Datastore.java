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

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());
    messageEntity.setProperty("ID", message.getId().toString());
    messageEntity.setProperty("subject", message.getSubject());

    datastore.put(messageEntity);
  }

  /** Deletes the Message in Datastore. */
  public void deleteMessage(String ID) {

    //queries for the message based on its UUID
    Filter propertyFilter = new FilterPredicate("ID", FilterOperator.EQUAL, ID);
    Query q = new Query("Message").setFilter(propertyFilter);
    PreparedQuery pq = datastore.prepare(q);
    Entity storedMessage = pq.asSingleEntity();
    datastore.delete(storedMessage.getKey());
  }

  /**
   * Gets messages with a given subject line.
   *
   * @return a list of messages with the given subject, or empty list if there are no
   *     messages with the subject. List is sorted by time descending.
   */
  public List<Message> getMessagesbySubjectSearch(String searchCriteria) {
    List<Message> messages = new ArrayList<>();

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("subject", FilterOperator.EQUAL, searchCriteria))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessagesHelper(results);
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has never posted a
   *     message. List is sorted by time descending.
   */
  public List<Message> getMessages(String recipient) {
    List<Message> messages = new ArrayList<>();

    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessagesHelper(results);
  }

   /**
   * Gets messages posted by all users.
   *
   * @return a list of messages posted by all users, or empty list if there are no
   * messages posted. List is sorted by time descending.
   */
  public List<Message> getAllMessages(){
  List<Message> messages = new ArrayList<>();

  Query query = new Query("Message")
    .addSort("timestamp", SortDirection.DESCENDING);
  PreparedQuery results = datastore.prepare(query);

  return getMessagesHelper(results);
 }

 /**
   * Helper function for getMessages and getAllMessages.
   *
   * @return a list of messages, or empty list if there are no
   * messages posted.
   */
 private List<Message> getMessagesHelper(PreparedQuery results) {
  List<Message> messages = new ArrayList<>();

  for (Entity entity : results.asIterable()) {
   try {
    String idString = entity.getKey().getName();
    UUID id = UUID.fromString(idString);
    String user = (String) entity.getProperty("user");
    String recipient = (String) entity.getProperty("recipient");
    String text = (String) entity.getProperty("text");
    long timestamp = (long) entity.getProperty("timestamp");
    String subject = (String) entity.getProperty("subject");

    Message message = new Message(id, user, text, timestamp, recipient, subject);
    messages.add(message);
   } catch (Exception e) {
    System.err.println("Error reading message.");
    System.err.println(entity.toString());
    e.printStackTrace();
   }
  }
  return messages;
 }
}
