#!/bin/bash

DDL_SCRIPT="ddl_script.sql"
DML_SCRIPT="dml_script.sql"

mysql < "$DDL_SCRIPT"

mysql < "$DML_SCRIPT"

echo "Database installation and data population complete."
