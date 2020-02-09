package umm3601;

import umm3601.user.Database;
import umm3601.user.UserController;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.io.IOException;

public class Server {

  public static final String CLIENT_DIRECTORY = "../client";
  public static final String USER_DATA_FILE = "src/main/data/users.json";
  private static Database userDatabase;

  public static void main(String[] args) {

    // Initialize dependencies
    UserController userController = buildUserController();

    Javalin server = Javalin.create(config -> {
      config.addStaticFiles(CLIENT_DIRECTORY, Location.EXTERNAL);
    }).start(4567);

    // Specify where client assets are stored
    // (all client-side HTML, CSS, JS, images, etc)
    // staticFiles.externalLocation(CLIENT_DIRECTORY);

    // Simple example route
    server.get("/hello", ctx -> ctx.result("Hello World"));

    // Redirects to create simpler URLs
    server.get("/about", ctx -> ctx.redirect("/about.html"));
    server.get("/users", ctx -> ctx.redirect("/users.html"));

    // API endpoints

    // Get specific user
    server.get("api/users/:id", ctx -> userController.getUser(ctx));

    // List users, filtered using query parameters
    server.get("api/users", ctx -> userController.getUsers(ctx));

    // An example of throwing an unhandled exception so you can see how the
    // Java Spark debugger displays errors like this.
    server.get("api/error", ctx -> {
      throw new RuntimeException("A demonstration error");
    });
  }

  /***
   * Create a database using the json fie, use it as
   * data source for a new UserController
   *
   * Constructing the controller might throw an IOException if
   * there are problems reading from the JSON "database" file.
   * If that happens we'll print out an error message and shut
   * the server down.
   * @throws IOException if we can't open or read the user data file
   */
  private static UserController buildUserController() {
    UserController userController = null;

    try {
      userDatabase = new Database(USER_DATA_FILE);
      userController = new UserController(userDatabase);
    } catch (IOException e) {
      System.err.println("The server failed to load the user data; shutting down.");
      e.printStackTrace(System.err);

      // Shut the server down
      // stop();
      System.exit(1);
    }

    return userController;
  }
}
