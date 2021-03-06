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

import java.util.UUID;

/** A single message posted from a user to a recipient */
public class Message {


  private UUID id;
  private String user;
  private String recipient;
  private String text;
  private String subject;
  private long timestamp;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
   * random ID and uses the current system time for the creation time.
   */
  public Message(String user, String text, String recipient, String subject) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient, subject);
  }

  public Message(UUID id, String user, String text, long timestamp,
                 String recipient, String subject) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
    this.subject = subject;
  }

  public UUID getId() {
    return id;
  }

  public String getUser() {
    return user;
  }

  public String getRecipient(){
    return recipient;
  }

  public String getText() {
    return text;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getSubject() {
    return subject;
  }

  public boolean getPublic() {
    return user.equals(recipient);
  }
}
