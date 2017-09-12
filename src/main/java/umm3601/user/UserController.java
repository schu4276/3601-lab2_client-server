package umm3601.user;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Controller that manages requests for info about users.
 */
public class UserController {

  private User[] users;

  /**
   * Construct a controller for users.
   *
   * This loads the "database" of user info from a JSON file and
   * stores that internally so that (subsets of) users can be returned
   * in response to requests.
   *
   * @throws IOException if we can't open or read the user data file
   * @param userDataFile the name of the file with the user data in JSON format
   */
  public UserController(String userDataFile) throws IOException {
    Gson gson = new Gson();
    FileReader reader = new FileReader(userDataFile);
    users = gson.fromJson(reader, User[].class);
  }

  // List users
  public User[] listUsers(Map<String, String[]> queryParams) {
    User[] filteredUsers = users;

    // Filter age if defined
    if(queryParams.containsKey("age")) {
      int age = Integer.parseInt(queryParams.get("age")[0]);
      filteredUsers = filterUsersByAge(filteredUsers, age);
    }

    return filteredUsers;
  }

  // Filter users by age
  public User[] filterUsersByAge(User[] filteredUsers, int age) {
    return Arrays.stream(filteredUsers).filter(x -> x.age == age).toArray(User[]::new);
  }

  // Get a single user
  public User getUser(String id) {
    return Arrays.stream(users).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

}
