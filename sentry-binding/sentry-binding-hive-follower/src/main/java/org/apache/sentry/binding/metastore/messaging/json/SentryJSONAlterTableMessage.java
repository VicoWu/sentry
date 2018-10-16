/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.sentry.binding.metastore.messaging.json;

import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hive.hcatalog.messaging.json.JSONAlterTableMessage;
import org.codehaus.jackson.annotate.JsonProperty;

public class SentryJSONAlterTableMessage extends JSONAlterTableMessage {
    @JsonProperty private String newLocation;
    @JsonProperty private String oldLocation;
    @JsonProperty private String newDbName;
    @JsonProperty private String oldDbName;
    @JsonProperty private String newTableName;
    @JsonProperty private String oldTableName;
    @JsonProperty private PrincipalType newOwnerType;
    @JsonProperty private PrincipalType oldOwnerType;
    @JsonProperty private String newOwnerName;
    @JsonProperty private String oldOwnerName;

    public SentryJSONAlterTableMessage() {
        super("", "", "", "", null);
    }

    public SentryJSONAlterTableMessage(String serverUrl, String servicePrincipal,
                                       String db, String table, Long timestamp,
                                       String oldLocation, String newLocation) {
        super(serverUrl, servicePrincipal, db, table, timestamp);
        this.newLocation = newLocation;
        this.oldLocation = oldLocation;
    }

    public SentryJSONAlterTableMessage(String serverUrl, String servicePrincipal,
      Long timestamp, Table before, Table after) {
        this(serverUrl, servicePrincipal, before.getDbName(), before.getTableName(), timestamp,
          before.getSd().getLocation(), after.getSd().getLocation());

        this.oldDbName = before.getDbName();
        this.newDbName = after.getDbName();
        this.oldTableName = before.getTableName();
        this.newTableName = after.getTableName();
        this.oldOwnerType = before.getOwnerType();
        this.newOwnerType = after.getOwnerType();
        this.oldOwnerName = before.getOwner();
        this.newOwnerName = after.getOwner();
    }

    public String getNewLocation() {
        return newLocation;
    }
    public String getOldLocation() {
        return oldLocation;
    }

    public String getOldDbName() {
        return oldDbName;
    }

    public String getNewDbName() {
        return newDbName;
    }

    public String getOldTableName() {
        return oldTableName;
    }

    public String getNewTableName() {
        return newTableName;
    }

    public PrincipalType getNewOwnerType() {
        return newOwnerType;
    }

    public PrincipalType getOldOwnerType() {
        return oldOwnerType;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    @Override
    public String toString() {
        return SentryJSONMessageDeserializer.serialize(this);
    }
}
