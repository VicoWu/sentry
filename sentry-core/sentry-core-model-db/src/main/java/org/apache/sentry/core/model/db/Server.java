/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sentry.core.model.db;

public class Server implements DBModelAuthorizable {

  /**
   * Represents all servers
   */
  public static final Server ALL = new Server(AccessConstants.ALL);

  private final String name;

  public Server(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Server [name=" + name + "]";
  }

  @Override
  public AuthorizableType getAuthzType() {
    return AuthorizableType.Server;
  }

  @Override
  public String getTypeName() {
    return getAuthzType().name();
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object o) {

    if(o == null) {
      return false;
    }

    if(!(o instanceof Server)) {
      return false;
    }

    if(((Server) o).getName() ==  null) {
      return false;
    }

    return ((Server) o).getName().equals(name);
  }
}
