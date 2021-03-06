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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;
  private static final Filter PUBLIC_FILTER_TRUE = new Query.FilterPredicate("isPublic", FilterOperator.EQUAL, true);
  private static final Filter PUBLIC_FILTER_FALSE = new Query.FilterPredicate("isPublic", FilterOperator.EQUAL, false);


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
    messageEntity.setProperty("isPublic", message.getPublic());

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
   * Gets messages with a given subject line that are public.
   *
   * @return a list of messages with the given subject that are public, or empty list if there are no
   *     messages with the subject that are public. List is sorted by time descending.
   */
  public List<Message> getMessagesbySubjectSearch(String searchCriteria) {

    Filter searchFilter = new Query.FilterPredicate("subject", FilterOperator.EQUAL, searchCriteria);
    CompositeFilter compFilter = CompositeFilterOperator.and(searchFilter, PUBLIC_FILTER_TRUE);

    Query query =
        new Query("Message")
            .setFilter(compFilter)
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessagesHelper(results);
  }

  /**
   * Gets messages posted by a specific user that are public.
   *
   * @return a list of messages posted by the user that are public, or empty list if user has never posted a
   *     message or if they are all private. List is sorted by time descending.
   */
  public List<Message> getUserMessages(String recipient) {

    Filter recipientFilter = new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient);
    CompositeFilter compFilter = CompositeFilterOperator.and(recipientFilter, PUBLIC_FILTER_TRUE);

    Query query =
        new Query("Message")
            .setFilter(compFilter)
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    return getMessagesHelper(results);
  }

   /**
   * Gets messages posted by all users that are public.
   *
   * @return a list of messages posted by all users that are public, or empty list if there are no
   * messages posted or if they are all private. List is sorted by time descending.
   */
  public List<Message> getAllPublicMessages(){

  Query query = new Query("Message")
    .setFilter(PUBLIC_FILTER_TRUE)
    .addSort("timestamp", SortDirection.DESCENDING);
  PreparedQuery results = datastore.prepare(query);

  return getMessagesHelper(results);
 }

 /**
 * Gets messages posted by the user to another user, or posted by another user to the user.
 *
 * @return a list of messages posted by another to the user or posted by the user to another, or empty list if there are no
 * direct messages to or from the user. List is sorted by time descending.
 */
public List<Message> getDirectMessages(String user) {

  Filter senderFilter = new Query.FilterPredicate("user", FilterOperator.EQUAL, user);
  Filter recipientFilter = new Query.FilterPredicate("recipient", FilterOperator.EQUAL, user);
  CompositeFilter userFilter = CompositeFilterOperator.or(senderFilter, recipientFilter);
  CompositeFilter compFilter = CompositeFilterOperator.and(userFilter, PUBLIC_FILTER_FALSE);

  Query query =
      new Query("Message")
          .setFilter(compFilter)
          .addSort("timestamp", SortDirection.DESCENDING);
  PreparedQuery results = datastore.prepare(query);

  return getMessagesHelper(results);
}

 /**
   * Helper function for getUserMessages, getAllPublicMessages, and getMessagesbySubjectSearch.
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
