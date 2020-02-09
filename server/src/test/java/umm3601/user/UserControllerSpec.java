package umm3601.user;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import umm3601.Server;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

/**
 * Tests the logic of the UserController
 *
 * @throws IOException
 */
public class UserControllerSpec {

  private Context ctx = mock(Context.class);

  private UserController userController;
  private static Database db;

  @BeforeEach
  public void setUp() throws IOException {

    ctx.clearCookieStore();

    db = new Database(Server.USER_DATA_FILE);
    userController = new UserController(db);
  }

  @Test
  public void GET_to_request_all_users() throws IOException {

    userController.getUsers(ctx);

    verify(ctx).attribute("ReturnedUserCount", 10);
  }

  @Test
  public void GET_to_request_age_25_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("age", Arrays.asList(new String[]{"25"}));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    verify(ctx).attribute("ReturnedUserCount", 2);
  }

  @Test
  public void GET_to_request_company_OHMNET_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[]{"OHMNET"}));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    verify(ctx).attribute("ReturnedUserCount", 2);
  }

  @Test
  public void GET_to_request_company_OHMNET_age_25_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[]{"OHMNET"}));

    queryParams.put("age", Arrays.asList(new String[]{"25"}));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    verify(ctx).attribute("ReturnedUserCount", 1);
  }

  @Test
  public void GET_to_request_user_with_existent_id() throws IOException  {
    doReturn(new Validator<String>("588935f5c668650dc77df581", "")).when(ctx).pathParam("id", String.class);
    userController.getUser(ctx);
    verify(ctx).status(201);
  }

  @Test
  public void GET_to_request_user_with_nonexistent_id() throws IOException  {
    doReturn(new Validator<String>("nonexistent", "")).when(ctx).pathParam("id", String.class);
    Assertions.assertThrows(NotFoundResponse.class, () -> {
      userController.getUser(ctx);
    });
  }
}
