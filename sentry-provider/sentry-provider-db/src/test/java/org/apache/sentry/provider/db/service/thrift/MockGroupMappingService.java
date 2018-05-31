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
package org.apache.sentry.provider.db.service.thrift;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.sentry.provider.common.GroupMappingService;

public class MockGroupMappingService implements GroupMappingService {
  private static Map<String, Set<String>> userGroups = new HashMap<>();

  @SuppressWarnings("unused")
  public MockGroupMappingService(Configuration conf, String resource) {
  }

  public static void addUserGroupMapping(String user, Set<String> groups) {
    userGroups.put(user, groups);
  }

  @Override
  public Set<String> getGroups(String user) {
    if (userGroups.containsKey(user)) {
      return userGroups.get(user);
    }

    return Collections.emptySet();
  }


}
