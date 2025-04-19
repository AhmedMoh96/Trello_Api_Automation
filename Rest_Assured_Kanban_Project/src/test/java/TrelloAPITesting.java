import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TrelloAPITesting extends TestBase {

    String key = configReader.getProperty("key");
    String token = configReader.getProperty("token");
    String baseUrl = configReader.getProperty("baseUrl");

    static String boardId;
    static String listId;
    static String cardId;
    static String checklistId;
    static String checkItemId;


    @Test(priority = 1)
    public void createBoard() {
        test = report.createTest("Create Board");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/")
                .queryParam("name", "Rest Assured Board")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Board creation failed: Status code not 200");
        boardId = response.jsonPath().getString("id");
        test.pass("Board created successfully: " + boardId);
    }

    @Test(priority = 2)
    public void getBoard() {
        test = report.createTest("Get Board");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Get board failed: Status code not 200");
        test.pass("Board fetched successfully");
    }

    @Test(priority = 3)
    public void createList() {
        test = report.createTest("Create List");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/lists")
                .queryParam("name", "Rest Assured List")
                .queryParam("idBoard", boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "List creation failed: Status code not 200");
        listId = response.jsonPath().getString("id");
        test.pass("List created successfully: " + listId);
    }

    @Test(priority = 4)
    public void getList() {
        test = report.createTest("Get List");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/lists/" + listId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Get list failed: Status code not 200");
        test.pass("List fetched successfully");
    }

    @Test(priority = 5)
    public void createCard() {
        test = report.createTest("Create Card");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards")
                .queryParam("name", "Rest Assured Card")
                .queryParam("idList", listId)
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Card creation failed: Status code not 200");
        cardId = response.jsonPath().getString("id");
        test.pass("Card created successfully: " + cardId);
    }

    @Test(priority = 6)
    public void getCard() {
        test = report.createTest("Get Card");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Get card failed: Status code not 200");
        test.pass("Card fetched successfully");
    }

    @Test(priority = 7)
    public void createChecklist() {
        test = report.createTest("Create Checklist");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists")
                .queryParam("idCard", cardId)
                .queryParam("name", "Rest Assured Checklist")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Checklist creation failed: Status code not 200");
        checklistId = response.jsonPath().getString("id");
        test.pass("Checklist created successfully: " + checklistId);
    }

    @Test(priority = 8)
    public void getChecklist() {
        test = report.createTest("Get Checklist");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Get checklist failed: Status code not 200");
        test.pass("Checklist fetched successfully");
    }

    @Test(priority = 9)
    public void addCheckItem() {
        test = report.createTest("Add CheckItem");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems")
                .queryParam("name", "Item_1")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "CheckItem creation failed: Status code not 200");
        checkItemId = response.jsonPath().getString("id");
        test.pass("CheckItem added successfully: " + checkItemId);
    }

    @Test(priority = 10)
    public void updateCheckItem() {
        test = report.createTest("Update CheckItem");
        String updatedName = "Updated CheckItem Name";

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId + "/checkItem/" + checkItemId)
                .queryParam("name", updatedName)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().put()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "CheckItem update failed: Status code not 200");

        Assert.assertEquals(response.jsonPath().getString("name"), updatedName, "CheckItem name was not updated in response");
        Assert.assertEquals(response.jsonPath().getString("id"), checkItemId, "CheckItem ID changed during update");
        test.pass("CheckItem updated successfully: Name set to '" + updatedName + "'");
    }

    @Test(priority = 11)
    public void getUpdatedCheckItem() {
        test = report.createTest("Get Updated CheckItem");
        String expectedName = "Updated CheckItem Name";

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Get updated checkitem failed: Status code not 200");

        List<String> allCheckItemNames = response.jsonPath().getList("name");
        Assert.assertTrue(allCheckItemNames.contains(expectedName), "Expected item not found in list");
        test.pass("Fetched updated CheckItem successfully with correct name: '" + expectedName + "'");
    }

    @Test(priority = 12)
    public void deleteCheckItem() {
        test = report.createTest("Delete CheckItem");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems/" + checkItemId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "CheckItem deletion failed: Status code not 200");
        test.pass("CheckItem deleted successfully");
    }

    @Test(priority = 13)
    public void deleteChecklist() {
        test = report.createTest("Delete Checklist");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Checklist deletion failed: Status code not 200");
        test.pass("Checklist deleted successfully");
    }

    @Test(priority = 14)
    public void deleteCard() {
        test = report.createTest("Delete Card");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Card deletion failed: Status code not 200");
        test.pass("Card deleted successfully");
    }

    @Test(priority = 15)
    public void deleteBoard() {
        test = report.createTest("Delete Board");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Board deletion failed: Status code not 200");
        test.pass("Board deleted successfully");
    }


    @Test(priority = 16)
    public void xAddCheckItemOnDeletedChecklist() {
        test = report.createTest("Negative - Add CheckItem on Deleted Checklist");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems")
                .queryParam("name", "NewItem")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().post()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "CheckItem was added to a deleted checklist unexpectedly");
        test.pass("Correctly rejected adding item to deleted checklist (Status: " + response.getStatusCode() + ")");
    }


    @Test(priority = 17)
    public void xUpdateDeletedCard() {
        test = report.createTest("Negative - Update Deleted Card");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("name", "Updated Name")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().put()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "Card was updated after deletion unexpectedly");
        test.pass("Correctly rejected update on deleted card (Status: " + response.getStatusCode() + ")");
    }

    @Test(priority = 18)
    public void xUpdateDeletedBoard() {
        test = report.createTest("Negative - Update Deleted Board");

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("name", "New Board Name")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().put()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "Board was updated after deletion unexpectedly");
        test.pass("Correctly rejected update on deleted board (Status: " + response.getStatusCode() + ")");
    }
}
