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
        this("ex4");
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
            statement.execute(strCreateUsersTable);           //  Create table
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
    @Override
    public boolean connect() throws Exception {
        String dbUrl = getDatabaseUrl();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            stmtSaveNewRecord = dbConnection.prepareStatement(strSaveUser, 
                    Statement.RETURN_GENERATED_KEYS);
            stmtUpdateExistingRecord = dbConnection.prepareStatement(strUpdateUser);
            stmtGetUserByLogin =dbConnection.prepareStatement(strGetListEntries);
            isConnected = dbConnection != null;
        }catch (SQLException ex) {
            //  Check if error code is error in case where table not exist
            if(ex.getErrorCode() == 30000){
                try{
                    createTables(dbConnection); //  call creating table
                }catch(Exception exc){
                    //  If table creation failed
                    throw new Exception("Cannot create table." + exc.getMessage() + ":" + ex.getMessage());
                }
                connect();                  //  call self for test
            }else{
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
    @Override
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
    @Override
    public boolean saveRecord(String [] records) throws Exception{
        boolean id = false;
        ResultSet results = null;
        try {
            stmtSaveNewRecord.clearParameters();
            stmtSaveNewRecord.setString(1, records[0]);
            stmtSaveNewRecord.setString(2, records[1]);
            stmtSaveNewRecord.setString(3, records[2]);
            stmtSaveNewRecord.setString(4, records[3]);
            stmtSaveNewRecord.setString(5, records[4]);
            int rowCount = stmtSaveNewRecord.executeUpdate();
            results = stmtSaveNewRecord.getGeneratedKeys();
            if (results.next()) {
                id = true;
            }
        } catch(SQLException sqle) {
            //  Catch exeption if entry exist in db
            if("23505".equals(sqle.getSQLState())){
                 throw new Exception("The Login Name already exist with this name.");
            } else{
                throw new Exception("ERROR: sql - write to data base. "
                        + "Result is " + sqle.getSQLState() + "." );
            }
        }
        return id;
    }
//============================================================================= 
    /**
     * Function wich check if user exist in data base by user login name
     * and password
     * @param loginName Login name
     * @param pass      Password
     * @return          Return true if exist
     * @throws Exception Exception wich can be in sql
     */
    @Override
    public boolean isItAxistAtBD(String loginName, String pass) throws Exception
    {
        ResultSet results           = null;   //    result

        try {
            stmtGetUserByLogin.clearParameters();
            stmtGetUserByLogin.setString(1, loginName);
            stmtGetUserByLogin.setString(2, pass);
            results = stmtGetUserByLogin.executeQuery();
            if(results.next()) {
               return true;
            }
        } catch (SQLException sqle) {
            throw new Exception(sqle.getMessage());
        }
        return false;
    }
           
//============================================================================= 
    /**
     * Function wich rovide otions to update the data base
     * Not used in version ex2. Still in this class for next exercises
     * @param records array of url and number of images
     * @return true if success
     */
    @Override
    public boolean editRecord(String [] records) {
        boolean bEdited = false;
        try {
            stmtUpdateExistingRecord.clearParameters();
            stmtUpdateExistingRecord.setString(1, records[1]);
            stmtUpdateExistingRecord.setString(2, records[0]);
            stmtUpdateExistingRecord.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
        }
        return bEdited;
    }
//=============================================================================
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    private PreparedStatement stmtSaveNewRecord;
    private PreparedStatement stmtGetUserByLogin;
    private PreparedStatement stmtUpdateExistingRecord;

    
    private static final String strCreateUsersTable =
            "create table APP.USERS (" +
            " id_login VARCHAR(256) not null primary key," +
            " id_mail VARCHAR(256) not null," +
            " id_pass VARCHAR(256) not null," +
            " id_fname VARCHAR(256) ," +
            " id_lname VARCHAR(256) )";
    
    
    private static final String strSaveUser =
            "INSERT INTO APP.USERS " +
            "   (id_login,id_mail, id_pass, id_fname, id_lname) " +
            "VALUES (?, ?, ?, ?, ?)";
    
    
    private static final String strGetListEntries =
            "SELECT * FROM APP.USERS "  +
            " WHERE id_login=? and id_pass=? ";
    private static final String strUpdateUser =
            "UPDATE APP.USERS " +
            "SET id_fname = ? " +
            "WHERE id_login = ? ";
}
//=============================================================================
//=============================================================================
//=============================================================================
    
    
  