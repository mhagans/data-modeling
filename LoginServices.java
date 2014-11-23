package DM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * User: Evan
 * Date: 11/23/2014
 * Time: 1:18 PM
 */
public class LoginServices
{
    /**
     * Requests access to secured menu.
     *
     * @param accessLevel Access level requested. 0 = Faculty, 1 = Administrator.
     * @return Returns username if successful, otherwise null.
     */
    private static boolean login(int accessLevel)
    {
        String username = getUsername(accessLevel);
        if(username == null)
        {
            System.out.println("Login failed.");
            return false;
        }
        if(getPassword(username))
        {
            System.out.println("Login succeeded.");
            return true;
        }
        else
        {
            System.out.println("Login failed.");
            return false;
        }
    }

    /**
     * Requests and validates username.
     *
     * @param accessLevel Access level requested. 0 = Faculty, 1 = Administrator.
     * @return Username for user if successful, otherwise null.
     */
    private static String getUsername(int accessLevel)
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
                String query = "SELECT name, Permission FROM id WHERE name = '" + username + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if(rs.getString("name") != null) //Username found in database
                {
                    if(rs.getInt("Permission") == accessLevel) //User has appropriate access level.
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
        //Request username
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
                String query = "SELECT password FROM id WHERE name = '" + username + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if(password.equals(rs.getString("password"))) //Correct password entered
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
        //TODO: Request new password.
        //TODO: Store new password.
        return false;
    }
}
