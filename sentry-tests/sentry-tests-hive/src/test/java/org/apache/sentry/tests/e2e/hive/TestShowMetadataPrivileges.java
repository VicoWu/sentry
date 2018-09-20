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
package org.apache.sentry.tests.e2e.hive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import org.apache.sentry.core.model.db.DBModelAction;
import org.apache.sentry.core.model.db.DBModelAuthorizable;
import org.apache.sentry.core.model.db.DBModelAuthorizable.AuthorizableType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestShowMetadataPrivileges extends AbstractTestWithStaticConfiguration {
  private static final boolean ALLOWED = true;
  private static final boolean NOT_ALLOWED = false;
  private static final String SERVER1 = "server1";

  private static Connection adminCon, user1Con;
  private static Statement adminStmt, user1Stmt;

  private AuthorizableType authorizableType;
  private DBModelAction action;
  private boolean allowed;

  @Parameterized.Parameters
  public static Collection describePrivileges() {
    return Arrays.asList(new Object[][] {
     { AuthorizableType.Server, null, NOT_ALLOWED }, // Means no privileges
     { AuthorizableType.Server, DBModelAction.ALL,     ALLOWED },
     { AuthorizableType.Server, DBModelAction.CREATE,  ALLOWED },
     { AuthorizableType.Server, DBModelAction.SELECT,  ALLOWED },
     { AuthorizableType.Server, DBModelAction.INSERT,  ALLOWED },
     { AuthorizableType.Server, DBModelAction.ALTER,   ALLOWED },
     { AuthorizableType.Server, DBModelAction.DROP,    ALLOWED },
      { AuthorizableType.Db, null, NOT_ALLOWED }, // Means no privileges
      { AuthorizableType.Db, DBModelAction.ALL,     ALLOWED },
      { AuthorizableType.Db, DBModelAction.CREATE,  ALLOWED },
      { AuthorizableType.Db, DBModelAction.SELECT,  ALLOWED },
      { AuthorizableType.Db, DBModelAction.INSERT,  NOT_ALLOWED },
      { AuthorizableType.Db, DBModelAction.ALTER,   ALLOWED },
      { AuthorizableType.Db, DBModelAction.DROP,    ALLOWED },
      { AuthorizableType.Table, null, NOT_ALLOWED }, // Means no privileges
      { AuthorizableType.Table, DBModelAction.ALL,     ALLOWED },
      { AuthorizableType.Table, DBModelAction.CREATE,  NOT_ALLOWED },
      { AuthorizableType.Table, DBModelAction.SELECT,  ALLOWED },
      { AuthorizableType.Table, DBModelAction.INSERT,  ALLOWED },
      { AuthorizableType.Table, DBModelAction.ALTER,   ALLOWED },
      { AuthorizableType.Table, DBModelAction.DROP,    ALLOWED },
    });
  }

  @BeforeClass
  public static void setupTestStaticConfiguration() throws Exception{
    useSentryService = true;
    AbstractTestWithStaticConfiguration.setupTestStaticConfiguration();
    setupAdmin();

    adminCon = context.createConnection(ADMIN1);
    adminStmt = context.createStatement(adminCon);
    user1Con = context.createConnection(USER1_1);
    user1Stmt = context.createStatement(user1Con);
  }

  @AfterClass
  public static void destroy() throws SQLException {
    adminStmt.close();
    adminCon.close();
    user1Stmt.close();
    user1Con.close();
  }

  public TestShowMetadataPrivileges(AuthorizableType authorizableType, DBModelAction action, boolean allowed) {
    this.authorizableType = authorizableType;
    this.action = action;
    this.allowed = allowed;
  }

  @Before
  public void setup() throws Exception {
    adminStmt.execute("DROP DATABASE IF EXISTS " + DB1 + " CASCADE");
    adminStmt.execute("CREATE DATABASE " + DB1);
    adminStmt.execute("CREATE TABLE " + DB1 + "." + TBL1 + " (id int)");
    adminStmt.execute("CREATE ROLE role1");
    adminStmt.execute("GRANT ROLE role1 TO group " + USERGROUP1);
  }

  @Test
  public void testShowTablesWithGrantOnTable() throws Exception {
    if(action == null || !allowed || authorizableType != AuthorizableType.Table) {
      return;
    }

    if(action != null) {
      adminStmt.execute("GRANT " + action + " ON TABLE " + DB1 + "." + TBL1 + " TO ROLE role1");
    }

    user1Stmt.execute("SHOW TABLES IN " + DB1);

    if (action == null) {
      assertFalse(
        "SHOW TABLES should NOT display tables with out privileges on the table.",
        user1Stmt.getResultSet().next());
    } else {
      assertTrue(
        "SHOW TABLES should display tables with " + action + " privileges on the table.",
        user1Stmt.getResultSet().next());
    }
  }

  @Test
  public void testShowTablesWithGrantOnDatabase() throws Exception {
    if(action == null || !allowed || authorizableType != AuthorizableType.Db) {
      return;
    }

    if(action != null) {
      adminStmt.execute("GRANT " + action + " ON DATABASE " + DB1 + " TO ROLE role1");
    }

    user1Stmt.execute("SHOW TABLES IN " + DB1);

    if (action == null) {
      assertFalse(
        "SHOW TABLES should NOT display tables with out privileges on the database.",
        user1Stmt.getResultSet().next());
    } else {
      assertTrue(
        "SHOW TABLES should display tables with " + action + " privileges on the database.",
        user1Stmt.getResultSet().next());
    }
  }

  @Test
  public void testShowTablesWithGrantOnServer() throws Exception {
    if(action == null || !allowed || authorizableType != AuthorizableType.Server) {
      return;
    }

    if(action != null) {
      adminStmt.execute("GRANT " + action + " ON SERVER " + SERVER1 + " TO ROLE role1");
    }

    user1Stmt.execute("SHOW TABLES IN " + DB1);

    if (action == null) {
      assertFalse(
        "SHOW TABLES should NOT display tables with out privileges on the server.",
        user1Stmt.getResultSet().next());
    } else {
      assertTrue(
        "SHOW TABLES should display tables with " + action + " privileges on the server.",
        user1Stmt.getResultSet().next());
    }
  }
}
