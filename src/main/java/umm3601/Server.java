package umm3601;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import umm3601.user.User;
import umm3601.user.UserController;

import java.io.IOException;

import static spark.Spark.*;

public class Server {

  public static final String USER_DATA_FILE = "src/main/data/users.json";

  public static void main(String[] args) {
    Server server = new Server();

    staticFiles.location("/public");

    Gson gson = new Gson();

    final UserController userController = buildUserController();

    // Simple example route
    get("/hello", (req, res) -> "Hello World");

    // Redirects for the "home" page
    redirect.get("", "/");
    redirect.get("/", "/index.html");

    // Redirect for the "about" page
    redirect.get("/about", "/about.html");

    // Redirect for the Users Form
    redirect.get("/users", "/users.html");

    // List users
    get("api/users", (req, res) -> {
      res.type("application/json");
      return wrapInJson("users", gson.toJsonTree(userController.listUsers(req.queryMap().toMap())));
    });

    // See specific user
    get("api/users/:id", (req, res) -> {
      return getUser(gson, userController, req, res);
    });
  }

  private static Object getUser(Gson gson, UserController userController, Request req, Response res) {
    res.type("application/json");
    String id = req.params("id");
    User user = userController.getUser(id);
    if (user != null) {
      return gson.toJson(userController.getUser(id));
    } else {
      res.type("application/json");
      return "{\"message\":\"User not found: User with ID " + id + " wasn't found.\"}";
    }
  }

  /*
   * Constructing the controller might throw an IOException if
   * there are problems reading from the JSON "database" file.
   * If that happens we'll print out an error message and shut
   * the server down.
   */
  private static UserController buildUserController() {
    UserController userController = null;

    try {
      userController = new UserController(USER_DATA_FILE);
    } catch (IOException e) {
      System.err.println("The server failed to load the user data; shutting down.");
      e.printStackTrace(System.err);

      // Shut the server down
      stop();
      System.exit(1);
    }

    return userController;
  }

  private static JsonObject wrapInJson(String name, JsonElement jsonElement) {
    JsonObject result = new JsonObject();
    result.add(name, jsonElement);
    return result;
  }

}
