package umm3601.user;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests umm3601.user.Database listUsers
 * with _age_ and _company_ query parameters
 */
public class FilterUsersByCombinedFiltersFromDB {

  @Test
  public void listUsersWithCombinedFilters() throws IOException {
    Database db = new Database("src/main/data/users.json");
    Map<String, String[]> queryParams = new HashMap<>();

    queryParams.put("age", new String[]{"25"});
    User[] age25Users = db.listUsers(queryParams);
    assertEquals("Incorrect number of users with age 25", 2, age25Users.length);

    queryParams.clear();
    queryParams.put("company", new String[]{"OHMNET"});
    User[] ohmnetUsers = db.listUsers(queryParams);
    assertEquals("Incorrect number of users with company OHMNET", 2, ohmnetUsers.length);

    queryParams.clear();
    queryParams.put("age", new String[]{"25"});
    queryParams.put("company", new String[]{"OHMNET"});
    User[] ohmnetAge25Users = db.listUsers(queryParams);
    assertEquals("Incorrect number of users with company OHMNET and age 25", 1, ohmnetAge25Users.length);
  }
}
