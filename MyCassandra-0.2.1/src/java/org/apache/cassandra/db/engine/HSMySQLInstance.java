/*                                                                                                                                                                                 
 * Copyright 2011 Shunsuke Nakamura, and contributors.
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

package org.apache.cassandra.db.engine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;

import org.handlersocket.*;

import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.DecoratedKey;

public class HSMySQLInstance extends DBSchemafulInstance
{
    HandlerSocket hsR, hsW;
    Connection conn;

    private final String PREFIX = "_";
    private final String ID = "1";
    private final String KEY = "rkey";
    private final String VALUE = "cf";
    private final String SYSTEM = "system";
    private final String RANGEPR = "range_get_row";
    private final String BINARY = "BINARY";
    private final String BLOB = "BLOB";

    private String rangeSt, truncateSt, dropTableSt, dropDBSt, createDBSt;

    int debug = 0;

    public HSMySQLInstance(String ksName, String cfName)
    {
        this.ksName = ksName;
        this.cfName = PREFIX + cfName;
        setConfiguration();
        setStatementDefinition();
        createDB();
        
        try
        {
            /* HandlerSocket uses three ports. */
            conn = new MySQLConfigure().connect(this.ksName, host, 3306, user, pass);
            hsR = new HandlerSocket();
            hsR.open(host, 9998); // default port 9998
            hsR.command().openIndex(ID, this.ksName, this.cfName, "PRIMARY", KEY + "," + VALUE);
            hsR.execute();
            hsW = new HandlerSocket();
            hsW.open(host, 9999); // default port 9999
            hsW.command().openIndex(ID, this.ksName, this.cfName, "PRIMARY", KEY + "," + VALUE);
            hsW.execute();
        }
        catch (IOException e)
        {
            errorMsg("can't open hs", e);
        }
    }
    
    private void setStatementDefinition()
    {
        /* define statements */
        rangeSt = "SELECT " + KEY + ", " + VALUE + " FROM " + this.cfName + " WHERE " + KEY + " >= ? AND " + KEY + " < ? LIMIT ?";
        truncateSt = "TRUNCATE TABLE " + this.cfName;
        dropTableSt = "DROP TABLE IF EXISTS " + this.cfName;
        dropDBSt = "DROP DATABASE IF EXISTS " + this.ksName;
        createDBSt = "CREATE DATABASE IF NOT EXISTS " + this.ksName;
        //rangePr = "CREATE PROCEDURE IF NOT EXISTS " + RANGEPR + this.cfName + "(IN begin VARBINARY(?),IN end VARBINARY(?),IN limitNum INT) BEGIN SET SQL_SELECT_LIMIT = limitNum; SELECT " + KEY + "," + VALUE + " FROM " + this.cfName + " WHERE " +  KEY + " >= begin AND " + KEY + "< end; END";
    }

    private String getCreateSt(String statement)
    {
        String createStHeader = "CREATE TABLE IF NOT EXISTS "+ this.cfName + "(" +"`" + KEY + "` VARBINARY(?) NOT NULL," + "`" + VALUE + "` ";
        String createStFooter = ", PRIMARY KEY (`" + KEY + "`)" + ") ENGINE = ?";
        return createStHeader + statement + createStFooter;
    }

    public int insert(byte[] rowKey, ColumnFamily cf)
    {
        try
        {
            return doInsert(rowKey, cf.toBytes());
        }
        catch (IOException e)
        {
            return errorMsg("db insertion error", e);
        }
    }

    public int update(byte[] rowKey, ColumnFamily newcf)
    {
        try
        {
            return doUpdate(rowKey, newcf.toBytes());
        }
        catch (IOException e)
        {
            return errorMsg("db update error", e);
        }
    }

    public byte[] select(byte[] rowKey)
    {
       try
       {
           hsR.command().find(ID, new String(rowKey));
           List<HandlerSocketResult> res = hsR.execute();
           return res.isEmpty() ? null : res.get(0).getMessages().get(1);
       }
       catch (IOException e)
       {
           errorMsg("db select error", e);
           return null;
       }
    }

    public Map<ByteBuffer, ColumnFamily> getRangeSlice(DecoratedKey startWith, DecoratedKey stopAt, int maxResults)
    {
        Map<ByteBuffer, ColumnFamily> rowMap = new HashMap<ByteBuffer, ColumnFamily>();
        try
        {
            PreparedStatement pstRange = conn.prepareStatement(rangeSt);
            pstRange.setBytes(1, startWith.getTokenBytes());
            pstRange.setBytes(2, stopAt.getTokenBytes());
            pstRange.setInt(3, maxResults);
            ResultSet rs = pstRange.executeQuery();
            if (rs != null)
                while (rs.next())
                    rowMap.put(ByteBuffer.wrap(rs.getBytes(1)), bytes2ColumnFamily(rs.getBytes(2)));
            rs.close();
            pstRange.close();
            return rowMap;
        }
        catch (SQLException e)
        {
            errorMsg("db get range slice error", e);
        }
        catch (IOException e)
        {
            errorMsg("db get range slice error", e);
        }
        return null;
    }

    public synchronized int delete(byte[] rowKey)
    {
        try
        {
            hsW.command().findModifyDelete(ID, new String(rowKey), "=", "1", "0");
            List<HandlerSocketResult> Results =  hsW.execute();
            return SUCCESS;
        }
        catch (IOException e)
        {
            return errorMsg("db deletion error", e);
        }
    }

    public synchronized int truncate()
    {
        try
        {
            return conn.createStatement().executeUpdate(truncateSt);
        }
        catch (SQLException e)
        {
            return errorMsg("db truncation error", e);
        }
    }

    public synchronized int dropTable()
    {
        try
        {
            return conn.createStatement().executeUpdate(dropTableSt);
        }
        catch (SQLException e)
        {
            return errorMsg("db dropTable error", e);
        }
    }
    
    public synchronized int dropDB()
    {
        try
        {
            return conn.createStatement().executeUpdate(dropDBSt);
        }
        catch (SQLException e)
        {
            return errorMsg("db dropDB error" , e);
        }
    }

    // Init MySQL Table for ColumnFamily
    public synchronized int create(int rowKeySize, int columnFamilySize, String storageSize, String storageEngine)
    {
        try {
            return getCreatePreparedSt(rowKeySize, columnFamilySize, storageSize, storageEngine).executeUpdate();
        }
        catch (SQLException e) 
        {
            errorMsg("db table creation error", e);
            return -1;
        }
    }

    private PreparedStatement getCreatePreparedSt (int rowKeySize, int columnFamilySize, String storageSize, String storageEngine)
    {
        PreparedStatement pst = null;
        try {
            if (storageSize.contains(BLOB))
            {
                pst = conn.prepareStatement(getCreateSt(storageSize));
                pst.setInt(1, rowKeySize);
                pst.setString(2, storageEngine);
            }
            else
            {
                pst = conn.prepareStatement(getCreateSt(storageSize + "(?)"));
                pst.setInt(1, rowKeySize);
                pst.setInt(2, columnFamilySize);
                pst.setString(3, storageEngine);
            }
        }
        catch (SQLException e)
        {
            errorMsg("db table create statement error", e);
        }
        return pst;
    }

    /*public int createProcedure(int rowKeySize, int columnFamilySize)
    {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW PROCEDURE STATUS");
            while (rs.next())
            if (rs.getString(1).equals(ksName))
                return 0;
            PreparedStatement rst = conn.prepareStatement(rangePr);
            
            rst.setInt(1, rowKeySize);
            rst.setInt(2, rowKeySize);
            
            return rst.executeUpdate();
        }
        catch (SQLException e)
        {
            return errorMsg("db procedure creation error", e);
        }
    }*/

    public synchronized int createDB()
    {
        try
        {
            Statement stmt = new MySQLConfigure().connect("", host, port, user, pass).createStatement();
            return stmt.executeUpdate(createDBSt);
        }
        catch (SQLException e) 
        {
            return errorMsg("db database creation error", e);
        }
    }

    private int doInsert(byte[] rowKey, byte[] cfValue) throws IOException
    {
        hsW.command().insert(ID, new String(rowKey), cfValue);
        List<HandlerSocketResult> res = hsW.execute();
        return res.get(0).getStatus();
        //return doUpdate(rowKey, cfValue);
    }

    private synchronized int doUpdate(byte[] rowKey, byte[] cfValue) throws IOException
    {
        hsW.command().findModifyUpdate(ID, new String(rowKey), "=", "1", "0", cfValue);
        List<HandlerSocketResult> res = hsW.execute();
        return res.get(0).getStatus();
    }
}