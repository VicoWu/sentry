-- create index for foreign key AUTHZ_OBJ_ID
CREATE INDEX "AUTHZ_PATH_FK_IDX" ON "AUTHZ_PATH" USING btree ("AUTHZ_OBJ_ID");