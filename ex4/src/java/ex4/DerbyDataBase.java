package ex4;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class is implimentation for database interface by derby SQL
 * @author Solange Karsenty, Edit By: Andrey Shamis AND Ilia Gaysinksy
 */
public class DerbyDataBase implements DataBase {

//=============================================================================  
    /** Creates a new instance of DerbyDataBase */
    public DerbyDataBase () throws Exception {
        this("ImageDataBase");
    }
//=============================================================================
    /**
     * Constructor function
     * @param dbName data base name 
     * @throws Exception used for get retrow exeption from prived functions
     */
    public DerbyDataBase(String dbName) throws Exception {
        this.dbName = dbName;
        setDBSystemDir();
        dbProperties = loadDBProperties();
        String driverName = dbProperties.getProperty("derby.driver"); 
        loadDatabaseDriver(driverName);
        //  Crate table if not exist
        if(!dbExists()) {
            createDatabase();
        }
    }
//=============================================================================
    /**
     * Function for check if data base exist
     * @return true if exist
     */
    private boolean dbExists() {
        boolean bExists = false;                    //  return value
        String dbLocation = getDatabaseLocation();  //  get database location
        File dbFileDir = new File(dbLocation);      //  get file of DB
        if (dbFileDir.exists()) {                   //  check if file exist
            bExists = true;                         //  set return value
        }
        return bExists;                             //  return value
    }
//=============================================================================
    /**
     * Prepating work with data base
     */
    private void setDBSystemDir() {
        // decide on the db system directory
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir;             //  set system dir
        System.setProperty("derby.system.home", systemDir);
        
        // create the db system directory
        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir(); 
    }
//============================================================================= 
    /**
     * Load the drivers need to connect
     * @param driverName driver name
     * @throws Exception ocured when cannot load driver
     */
    private void loadDatabaseDriver(String driverName) throws Exception {
        
        try {
            Class.forName(driverName);          // load Derby driver
        } catch (ClassNotFoundException ex) {
           throw new Exception("Cannot load DB driver More info:" 
                   + ex.getMessage());
        }
        
    }
//=============================================================================
    /**
     * Function wich load properties needed for db work
     * @return  the properties
     * @throws Exception  exception ocured when same problem in loading 
     * properties, the exeption send to instance who called 
     */
    private Properties loadDBProperties()  throws Exception{
        InputStream dbPropInputStream = null;
        dbPropInputStream = DerbyDataBase.class.getResourceAsStream("Configuration.properties");
        dbProperties = new Properties();
        try {
            dbProperties.load(dbPropInputStream);
        } catch (IOException ex) {
            throw new Exception("Cannot load DB prop. More info:" 
                    + ex.getMessage());
        }
        return dbProperties;
    }
    
//=============================================================================
    /**
     * Function wich create table
     * @param dbConnection connection
     * @return true if created
     * @throws Exception  when cannot create table
     */
    private boolean createTables(Connection dbConnection) throws Exception{
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            System.out.println("Start table creation");     //  user message
            statement = dbConnection.createStatement();     //  
            statement.execute(strCreateUrlTable);           //  Create table
            bCreatedTables = true;                          //  set ret value
            System.out.println("Table was created");        //  user message
        } catch (SQLException ex) {
            throw new Exception("Cannot crate table. More info:" 
                    + ex.getMessage());
        }
        
        return bCreatedTables;                              //  return value
    }
//=============================================================================
    /**
     * Function for creating database
     * @return true if created
     * @throws Exception when cannot create database
     */
    private boolean createDatabase()throws Exception {
        boolean bCreated = false;
        dbConnection = null;
        
        String dbUrl = getDatabaseUrl();
        dbProperties.put("create", "true");
        
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            bCreated = createTables(dbConnection);
        } catch (SQLException ex) {
            throw new Exception("Canot create data base:"+ex.getMessage());
        }
        dbProperties.remove("create");
        return bCreated;
    }
//============================================================================= 
    /**
     * Function wich provide connection to data base
     * @return true if connect success
     * @throws Exception ocured when the clas not ready to work with database
     * can happen when the problem in qery template
     */
    public boolean connect() throws Exception {
        String dbUrl = getDatabaseUrl();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            stmtSaveNewRecord = dbConnection.prepareStatement(strSaveUrl, 
                    Statement.RETURN_GENERATED_KEYS);
            stmtUpdateExistingRecord = dbConnection.prepareStatement(strUpdateUrl);
            isConnected = dbConnection != null;
        } 
        catch (SQLException ex) {
            //  Check if error code is error in case where table not exist
            if(ex.getErrorCode() == 30000){
                try{
                    createTables(dbConnection); //  call creating table
                }
                catch(Exception exc){
                    //  If table creation failed
                    throw new Exception("Cannot create table.");
                }
                connect();                  //  call self for test
            }
            else{
                isConnected = false;
                throw new Exception("ERROR: Cannot connect to data base.");
            }
        }
        return isConnected;
    }
//=============================================================================    
    /**
     * Function wich close the connection created by
     * funvtion connect. 
     */
    public void disconnect(){
        if(isConnected) {
            String dbUrl = getDatabaseUrl();
            dbProperties.put("shutdown", "true");
            try {
                DriverManager.getConnection(dbUrl, dbProperties);
            } catch (SQLException ex) {
            }
            isConnected = false;
        }
    }
    
//=============================================================================    
    /**
     * Function wich provide string to database location
     * @return the location
     */
    private String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") 
                + "/" + dbName;
        return dbLocation;
    }
    
//=============================================================================    
    /**
     * Function wich return data base url
     * @return the data base url
     */
    private String getDatabaseUrl() {
        String dbUrl = dbProperties.getProperty("derby.url") + dbName;
        return dbUrl;
    }
    
//=============================================================================
    /**
     * Function for save information to data base
     * @param records array of url and number of images
     * @return true if query executing success
     */
    public boolean saveRecord(String [] records) {
        boolean id = false;
        ResultSet results = null;
        try {
            stmtSaveNewRecord.clearParameters();
            stmtSaveNewRecord.setString(1, records[0]);
            stmtSaveNewRecord.setString(2, records[1]);
            int rowCount = stmtSaveNewRecord.executeUpdate();
            results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = true;
            }
             System.out.println("The new entry was added.");
        } catch(SQLException sqle) {
            //  Catch exeption if entry exist in db
            if("23505".equals(sqle.getSQLState()))
            {
                System.out.println("The URL entry ("+ records[0] +")"
                        + " already exist.");
            }
            else{
                System.out.println("ERROR: sql - write to data base. "
                        + "Result is " + sqle.getSQLState() + "." );
            }
        }
        return id;
    }
//=============================================================================
//    /**
//     * Function wich execute sql query for all entryes in table
//     * @return list of lists of strings
//     */
//    public ArrayList<ArrayList<String>> getListEntries()  throws Exception{
//        ArrayList<ArrayList<String>> listEntries = new ArrayList<>();
//        Statement queryStatement    = null;   //    result
//        ResultSet results           = null;   //    result
//        
//        try {
//            queryStatement = dbConnection.createStatement();
//            results = queryStatement.executeQuery(strGetListEntries);
//            while(results.next()) {
//                ArrayList<String> dbLine = new ArrayList<>();
//                dbLine.add(results.getString(1));   //  Set url entry
//                dbLine.add(results.getString(2));   //  set image entry
//                listEntries.add(dbLine);            //  put into list
//            }
//        } catch (SQLException sqle) {
//            throw new Exception("ERROR: sql - read from data base");
//        }
//        return listEntries;
//    }
//============================================================================= 
    /**
     * Function wich rovide otions to update the data base
     * Not used in version ex2. Still in this class for next exercises
     * @param records array of url and number of images
     * @return true if success
     */
    public boolean editRecord(String [] records) {
        boolean bEdited = false;
        try {
            stmtUpdateExistingRecord.clearParameters();
            stmtUpdateExistingRecord.setString(1, records[1]);
            stmtUpdateExistingRecord.setString(2, records[0]);
            stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
            System.out.println("ERROR: sql - update data base");
        }
        return bEdited;
    }
//=============================================================================
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    private PreparedStatement stmtSaveNewRecord;
    private PreparedStatement stmtUpdateExistingRecord;

    
    private static final String strCreateUrlTable =
            "create table APP.IMAGES (" +
            " URL VARCHAR(256) not null primary key," +
            " NUM_OF_IMG NUMERIC(11) default 0 not null)";
    
    
    private static final String strSaveUrl =
            "INSERT INTO APP.IMAGES " +
            "   (URL, NUM_OF_IMG) " +
            "VALUES (?, ?)";
    
    
    private static final String strGetListEntries =
            "SELECT * FROM APP.IMAGES "  +
            "ORDER BY URL ASC";
    private static final String strUpdateUrl =
            "UPDATE APP.IMAGES " +
            "SET NUM_OF_IMG = ? " +
            "WHERE URL = ? ";
}
//=============================================================================
//=============================================================================
//=============================================================================
    
    
  