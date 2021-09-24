package edu.jsu.mcis.cs310;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {
    
    public JSONArray getJSONData() {
        
        JSONArray results = null;
        
        try {
            // INSERT YOUR CODE HERE
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return results;
        
    }

    public void dbTest() {
        
        Connection conn;
        ResultSetMetaData metadata;
        ResultSet resultset = null;        
        PreparedStatement pstSelect = null, pstUpdate = null;
        
        String query, key, value;
        String newFirstName = "Alfred", newLastName = "Neuman";
        
        boolean hasresults;
        int resultCount, columnCount, updateCount;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/cs310_dbtest_1");
            String username = "root";
            String password = "CS488";
            System.out.println("Connecting to " + server + "...");
            
            /* Open Connection (MySQL JDBC driver must be on the classpath!) */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                
                // Prepare Update Query
                
                query = "INSERT INTO people (firstname, lastname) VALUES (?, ?)";
                pstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstUpdate.setString(1, newFirstName);
                pstUpdate.setString(2, newLastName);
                
                // Execute Update Query
                
                updateCount = pstUpdate.executeUpdate();
                
                // If Update Successful, Get New Key and Print To Console
                
                if (updateCount > 0) {
            
                    resultset = pstUpdate.getGeneratedKeys();

                    if (resultset.next()) {

                        System.out.print("Update Successful!  New Key: ");
                        System.out.println(resultset.getInt(1));

                    }

                }
                
                
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstSelect.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        /* Get Column Names; Print as Table Header */
                        
                        for (int i = 1; i <= columnCount; i++) {

                            key = metadata.getColumnLabel(i);

                            System.out.format("%20s", key);

                        }
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                            for (int i = 1; i <= columnCount; i++) {

                                value = resultset.getString(i);

                                if (resultset.wasNull()) {
                                    System.out.format("%20s", "NULL");
                                }

                                else {
                                    System.out.format("%20s", value);
                                }

                            }

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            conn.close();
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstSelect != null) { try { pstSelect.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstUpdate != null) { try { pstUpdate.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
    }
    
}