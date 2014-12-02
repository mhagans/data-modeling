package DM;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

/**
 * User: Evan
 * Date: 11/23/2014
 * Time: 1:18 PM
 */
public class LoginServices
{
    private static final String salt = "&y81*d5jp8dn4n0@-$u-_)w30+j9*lksh)r$c&2v(bu#%$8!2t";
    public enum AccessLevel
    {
        FACULTY, ADMIN
    }

    /**
     * Create a new user.
     *
     * @param username unique identifier for new user.
     * @param name Name of user.
     * @param degree Area of focus.
     * @param password Password for new user.
     * @param accessLevel Access level for new user.
     */
    public static void createUser(String username, String name, String degree, String password, AccessLevel accessLevel)
    {
        //TODO: Major refactoring
        try
        {
            String query = String.format("INSERT INTO id (id, name, degree, password, Permission) values %s %s %s %s %i;",
                    username, name, degree, password, accessLevel.ordinal());

            Connection connection = ConnectDB.getConn();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("User was created successfully");

            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }    

    /**
     * Requests access to secured menu.
     *
     * @param accessLevel Access level requested.
     * @return Returns username if successful, otherwise null.
     */
    public static String login(AccessLevel accessLevel)
    {
        String username = getUsername(accessLevel);
        if(username == null)
        {
            System.out.println("Login failed.");
            return null;
        }
        if(getPassword(username))
        {
            System.out.println("Login succeeded.");
            return username;
        }
        else
        {
            System.out.println("Login failed.");
            return null;
        }
    }

    /**
     * Requests and validates username.
     *
     * @param accessLevel Access level requested. 0 = Faculty, 1 = Administrator.
     * @return Username for user if successful, otherwise null.
     */
    private static String getUsername(AccessLevel accessLevel)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter your username: ");
        String username = input.nextLine();

        //Open connection and check for username in database. Validate correct access level.
        try
        {
            Connection conn = ConnectDB.getConn();
            if(conn != null) //Successfully connected to database
            {
                PreparedStatement statement = conn.prepareStatement("SELECT id, Permission FROM id WHERE id = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.getString("id") != null) //Username found in database
                {
                    if(resultSet.getInt("Permission") == accessLevel.ordinal()) //User has appropriate access level.
                    {
                        conn.close();
                        return username;
                    }
                    else //User lacks appropriate access level.
                    {
                        System.out.println("Invalid access permissions.");
                        conn.close();
                        return null;
                    }
                }
                else //Username not found in database
                {
                    System.out.println("Username not found.");
                    conn.close();
                    return null;
                }
            }
            else //Failed to connect to database
            {
                System.out.println("Unable to connect to login server.");
                return null;
            }
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    /**
     * Requests and validates password.
     *
     * @param username Unique ID of user requesting access.
     * @return Boolean representing success of password input.
     */
    private static boolean getPassword(String username)
    {
        //Request password
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter your password: ");
        String password = input.nextLine();

        //Validate password
        try
        {
            Connection conn = ConnectDB.getConn();
            if(conn != null) //Successfully connected to database
            {
                //Pull user password from database
                PreparedStatement statement = conn.prepareStatement("SELECT password FROM id WHERE id = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                try
                {
                    if(encryptPassword(password).equals(resultSet.getString("password"))) //Correct password entered
                    {
                        conn.close();
                        return true;
                    }
                    else //Incorrect password entered.
                    {
                        System.out.println("Invalid password.");
                        conn.close();
                        return false;
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Invalid password.");
                    conn.close();
                    return false;
                }
            }
            else //Failed to connect to database
            {
                System.out.println("Unable to connect to login server.");
                return false;
            }
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Updates a user's password.
     *
     * @param username Unique ID of user requesting to update password.
     * @return Boolean representing success of update request.
     */
    private static boolean updatePassword(String username)
    {
        //Request new password
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter your password: ");
        String newPassword = input.nextLine();

        //Store new password.
        try
        {
            Connection conn = ConnectDB.getConn();
            if(conn != null) //Successfully connected to database
            {
                PreparedStatement statement = conn.prepareStatement("UPDATE id SET password = ? WHERE id = ?");
                try
                {
                    statement.setString(1, encryptPassword(newPassword));
                    statement.setString(2, username);
                    statement.executeUpdate();
                }
                catch(Exception e)
                {
                    System.out.println("Error updating password.");
                    conn.close();
                    return false;
                }
                conn.close();
                return true;
            }
            else //Failed to connect to database
            {
                System.out.println("Unable to connect to login server.");
                return false;
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return false;
    }

    /**
     * Encrypts a given password.
     *
     * @param password Password to encrypt.
     * @return Encrypted hash of given password.
     */
    private static String encryptPassword(String password)
    {
        try
        {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update((password.concat(salt)).getBytes(), 0, password.length() + salt.length());

            //Converts message digest value in base 16 (hex)
            return new BigInteger(1, digest.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
