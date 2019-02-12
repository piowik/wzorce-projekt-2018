package com.ammp.dp.actions;

import com.ammp.dp.factory.*;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PostgreSQLFactory;

import java.sql.SQLInput;
import java.util.List;
import java.util.Objects;

public class SaveAccessProtector {

    private Commit commit;
    private Connection connection;
    private Statement statement;

    private SQLFactory sqlFactory;

    public SaveAccessProtector(String hostname, String user, String password, String dbType) {

        if(dbType.equals(Constants.POSTGRESQL)){
            sqlFactory = new PostgreSQLFactory();
        } else if (dbType.equals(Constants.MYSQL)){
            sqlFactory = new MySQLFactory();
        }

        connection = Objects.requireNonNull(sqlFactory).createConenction(hostname, user, password);
    }


    public List<Object> selectFromDB(SQLInput sqlInput){
        return null;
    }

    java.sql.Connection conn = connection.getConnection();
}
