/**
 *
 */
package com.capgemini.pccc.manager;

import com.capgemini.pccc.util.Constants;
import com.capgemini.pccc.util.Enumeration;
import com.capgemini.pccc.util.Enumeration.MappedJavaType;
import com.capgemini.pccc.util.Enumeration.MappedJsonType;
import com.capgemini.pccc.util.UtilEntry;
import org.apache.commons.configuration2.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author sflacher
 */
public class DaoManager {

    // =========================================== class variables
    private final static Logger LOG = LoggerFactory.getLogger(DaoManager.class);
    public static Configuration _config = null;
    private static DaoManager _INSTANCE = null;
    private static Boolean useNoDBMode = null;

    // JDBC driver name and database URL
    private static String jdbcDriver = null;
    private static String db_url = null;
    private static Connection dbConn = null;

    //  Database credentials
    private static String dbUser = null;
    private static String dbPwd = null;

    // =========================================== instance variables

    /**
     * Singleton pattern
     */
    private DaoManager() {
        //jdbcDriver = "com.mysql.jdbc.Driver";

        // jdbc:postgresql://host:port/database
        dbUser = _config.getString("pccc.db.username");
        dbPwd = _config.getString("pccc.db.password");
        db_url = _config.getString("pccc.db.uri");
        useNoDBMode = _config.getBoolean("pccc.use.noDB.mode");

    }

    // =========================================== constructors

    // =========================================== class methods
    public static DaoManager getInstance(Configuration config) {
        _config = config;

        if (_INSTANCE == null)
            _INSTANCE = new DaoManager();

        return _INSTANCE;
    }

    /**
     * @param sql
     */
    public JSONObject executeSQL(String sql, int aggColumnId, String queryName) throws SQLException {

        Statement stmt = null;
        String method = "executeSQL(sql)" + Constants.DASHES;
        LOG.info("==== useNoDBMode : [" + useNoDBMode + "]");

        ResultSet rs = null;
        JSONObject rootJsonObject = new JSONObject();
        JSONObject responseJso = null;

        if (useNoDBMode) {
            String sqlResponse = _config.getString("pccc.sql.select." + queryName + ".nodb");
            LOG.info(sqlResponse);
            responseJso = new JSONObject(sqlResponse);
            return responseJso;
        }


        try {
            if (dbConn == null)
                dbConn = createDbConnection();

            // Execute a query
            LOG.info("Creating statement for [" + sql + "]");
            stmt = dbConn.createStatement();

            rs = stmt.executeQuery(sql);

            UtilEntry<List<String>, List<Enumeration.MappedJavaType>> pairBo = extractFieldMetaData(rs);

            /**
             * On extrait les 2 listes du UtilEntry
             */
            List<String> fieldNameList = pairBo.getKey();
            List<Enumeration.MappedJavaType> fieldMappedJavaTypeList = pairBo.getValue();

            JSONArray aggJsoAr = null;

            String aggColumnName = fieldNameList.get(aggColumnId);
            LOG.info("==== aggColumnName [" + aggColumnName + "]");


            // on crée le aggColumnJso "TRAFIC" dans lequel on va ajouter les boJso de chaque row

            // MappedJsonType mappedJsonType = null;

            // for each row found
            //            String lastAggColumnVal = null;
            String aggColumnVal = null;

            while (rs.next()) {

                aggColumnVal = rs.getString(aggColumnName);

                LOG.info("========================= For this row, aggColumnVal is [" + aggColumnVal + "]");

                JSONObject rowJso = buildRowJsonObject(rs, fieldNameList, fieldMappedJavaTypeList);

                //                if (lastAggColumnVal == null) {
                //                    lastAggColumnVal = aggColumnVal;
                aggJsoAr = addAggColumnJsoToRootJso(rootJsonObject, aggColumnVal);

                aggJsoAr.put(rowJso);
                //                }

            }
            rs.close();
        } catch (SQLException se) {
            LOG.error(se.getMessage(), se);
        } finally {
            try {
                if (!stmt.isClosed()) {
                    stmt.close();
                    LOG.info("OK -- dbConnection is CLOSED");
                }
                if (!rs.isClosed()) {
                    rs.close();
                    LOG.info("OK -- ResultSet is CLOSED");
                }

            } catch (SQLException se) {
            }
            try {
                if (dbConn != null) {
                    dbConn.close();
                }
            } catch (SQLException se) {
                LOG.error("SILENT exception closing dbConnection" + se.getMessage(), se);
            }//end finally try
        }//end try
        LOG.info(rootJsonObject.toString(4));
        LOG.info("Goodbye!");

        return rootJsonObject;
    }

    /**
     *
     */
    private JSONArray addAggColumnJsoToRootJso(JSONObject rootJsonObject, String aggColumnVal) {

        JSONArray aggColumnJsonAr = null;

        if (rootJsonObject.has(aggColumnVal)) {
            aggColumnJsonAr = rootJsonObject.getJSONArray(aggColumnVal);
        } else {

            aggColumnJsonAr = new JSONArray();
            rootJsonObject.put(aggColumnVal, aggColumnJsonAr);
        }

        return aggColumnJsonAr;
    }


    /**
     * @param rs
     * @param fieldNameList
     * @param fieldMappedJavaTypeList
     * @return
     */
    private JSONObject buildRowJsonObject(
            ResultSet rs, List<String> fieldNameList,
            List<MappedJavaType> fieldMappedJavaTypeList) throws SQLException {

        MappedJavaType mappedJavaType = null;
        // String jsonValue = null;
        JSONObject boJso = new JSONObject();

        // for each column
        for (int i = 0; i < fieldNameList.size(); i++) {

            String fieldName = fieldNameList.get(i);
            // Get the java type of this column
            mappedJavaType = fieldMappedJavaTypeList.get(i);

            switch (mappedJavaType) {
                case java_lang_Integer:
                    //                    jsonValue = rs.getInt(i + 1) + "";
                    boJso.put(fieldName, rs.getInt(i + 1));
                    break;

                case java_lang_Double:
                    //                    jsonValue = rs.getDouble(i + 1) + "";
                    boJso.put(fieldName, rs.getDouble(i + 1));
                    break;

                case java_lang_Long:
                    //                    jsonValue = rs.getLong(i + 1) + "";
                    boJso.put(fieldName, rs.getLong(i + 1));
                    break;

                case java_lang_Float:
                    //                    jsonValue = rs.getFloat(i + 1) + "";
                    boJso.put(fieldName, rs.getFloat(i + 1));
                    break;

                case java_math_BigDecimal:
                    //                    jsonValue = rs.getBigDecimal(i + 1) + "";
                    boJso.put(fieldName, rs.getBigDecimal(i + 1));
                    break;

                case java_lang_Boolean:
                    //                    jsonValue = "\"" + rs.getBoolean(i + 1) + "\"";
                    boJso.put(fieldName, rs.getBoolean(i + 1));
                    break;

                case java_lang_String:
                    //                    jsonValue = "\"" + rs.getString(i + 1) + "\"";
                    boJso.put(fieldName, rs.getString(i + 1));
                    break;

                case java_lang_Byte__Array:
                    //                    jsonValue = "\"" + rs.getBytes(i + 1) + "\"";
                    boJso.put(fieldName, rs.getBytes(i + 1));
                    break;

                case java_sql_Date:
                    //                    jsonValue = "\"" + rs.getDate(i + 1) + "\"";
                    boJso.put(fieldName, rs.getDate(i + 1));
                    break;

                default:
                    // CHARACTER, VARCHAR, LONGVARCHAR
                    //                    jsonValue = "null";
                    boJso.put(fieldName, "null");
                    break;
            }

        } // fin du for
        LOG.debug("boJso : " + boJso.toString(4));
        return boJso;
    }

    /**
     * @param rs
     * @return
     * @throws SQLException
     */
    private UtilEntry<List<String>, List<Enumeration.MappedJavaType>> extractFieldMetaData(
            ResultSet rs) throws SQLException {
        List<String> fieldNameList = new ArrayList<>();
        List<Enumeration.MappedJavaType> fieldTypeList = new ArrayList<>();
        UtilEntry<List<String>, List<Enumeration.MappedJavaType>> pairBO = new UtilEntry<>(fieldNameList,
                fieldTypeList);

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();

        for (int i = 1; i <= colCount; i++) {
            fieldNameList.add(rsmd.getColumnName(i));

            Enumeration.MappedJavaType javaType = computeMappedJavaType(rsmd.getColumnType(i));
            fieldTypeList.add(javaType);
        }

        return pairBO;
    }

    /**
     * @param columnType
     * @return
     */
    private Enumeration.MappedJavaType computeMappedJavaType(int columnType) {

        switch (columnType) {
            case Types.BIGINT:
                return MappedJavaType.java_lang_Long;


            case Types.NUMERIC:
                return MappedJavaType.java_math_BigDecimal;

            case Types.DECIMAL:
                return MappedJavaType.java_math_BigDecimal;

            case Types.BIT:
                return MappedJavaType.java_lang_Boolean;

            case Types.INTEGER:
                return MappedJavaType.java_lang_Integer;

            case Types.TINYINT:
                return MappedJavaType.java_lang_Integer;

            case Types.SMALLINT:
                return MappedJavaType.java_lang_Integer;

            case Types.REAL:
                return MappedJavaType.java_lang_Float;

            case Types.FLOAT:
                return MappedJavaType.java_lang_Double;

            case Types.DOUBLE:
                return MappedJavaType.java_lang_Double;

            case Types.LONGVARBINARY:
                return MappedJavaType.java_lang_Byte__Array;

            case Types.VARBINARY:
                return MappedJavaType.java_lang_Byte__Array;

            case Types.BINARY:
                return MappedJavaType.java_lang_Byte__Array;

            case Types.DATE:
                return MappedJavaType.java_sql_Date;

            case Types.TIME:
                return MappedJavaType.java_sql_Time;

            case Types.TIMESTAMP:
                return MappedJavaType.java_sql_TimeStamp;

            default:
                // CHARACTER, VARCHAR, LONGVARCHAR
                return MappedJavaType.java_lang_String;
        }
    }

    /**
     * @param columnType
     * @return
     */
    private Enumeration.MappedJsonType computeMappedJsonType(int columnType) {

        switch (columnType) {
            case Types.BIGINT:
                return MappedJsonType._number;

            case Types.NUMERIC:
                return MappedJsonType._number;

            case Types.DECIMAL:
                return MappedJsonType._number;

            case Types.BIT:
                return MappedJsonType._boolean;

            case Types.INTEGER:
                return MappedJsonType._number;

            case Types.TINYINT:
                return MappedJsonType._number;

            case Types.SMALLINT:
                return MappedJsonType._number;

            case Types.REAL:
                return MappedJsonType._number;

            case Types.FLOAT:
                return MappedJsonType._number;

            case Types.DOUBLE:
                return MappedJsonType._number;

            case Types.LONGVARBINARY:
                return MappedJsonType._null;

            case Types.VARBINARY:
                return MappedJsonType._null;

            case Types.BINARY:
                return MappedJsonType._null;

            case Types.DATE:
                return MappedJsonType._string;

            case Types.TIME:
                return MappedJsonType._string;

            case Types.TIMESTAMP:
                return MappedJsonType._string;

            default:
                // CHARACTER, VARCHAR, LONGVARCHAR
                return MappedJsonType._null;
        }
    }

    public Connection createDbConnection() throws SQLException {
        dbConn = null;

        // Class.forName("org.postgresql.Driver");

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", dbPwd);
        props.setProperty("ssl", "false");


        //: Register JDBC driver
        //STEP 3: Open a connection
        LOG.info("");
        LOG.info("Connecting to a selected database...");
        dbConn = DriverManager.getConnection(db_url, props);
        LOG.info("Connected database successfully...");

        return dbConn;
    }
}
