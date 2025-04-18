import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

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
                .baseUri(baseUrl + "/boards")
                .queryParam("name", "Rest Assured Board")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Board creation failed");

        if (response.getStatusCode() == 200) {
            boardId = response.jsonPath().getString("id");
            test.pass("Board created: " + boardId);
        } else {
            test.fail("Board creation failed.");
        }
    }

    @Test(priority = 2)
    public void getBoard() {
        test = report.createTest("Get Board");

        if (boardId == null) {
            test.fail("Board ID is null. Skipping testGetBoard.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve board");
        test.pass("Board fetched successfully");
    }

    @Test(priority = 3)
    public void createList() {
        test = report.createTest("Create List");

        if (boardId == null) {
            test.fail("Board ID is null. Skipping testCreateList.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/lists")
                .queryParam("name", "Rest Assured List")
                .queryParam("idBoard", boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "List creation failed");

        if (response.getStatusCode() == 200) {
            listId = response.jsonPath().getString("id");
            test.pass("List created: " + listId);
        } else {
            test.fail("List creation failed.");
        }
    }

    @Test(priority = 4)
    public void getList() {
        test = report.createTest("Get List");

        if (listId == null) {
            test.fail("List ID is null. Skipping testGetList.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/lists/" + listId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve list");
        test.pass("List fetched successfully");
    }

    @Test(priority = 5)
    public void createCard() {
        test = report.createTest("Create Card");

        if (listId == null) {
            test.fail("List ID is null. Skipping testCreateCard.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards")
                .queryParam("name", "Rest Assured Card")
                .queryParam("idList", listId)
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Card creation failed");

        if (response.getStatusCode() == 200) {
            cardId = response.jsonPath().getString("id");
            test.pass("Card created: " + cardId);
        } else {
            test.fail("Card creation failed.");
        }
    }

    @Test(priority = 6)
    public void getCard() {
        test = report.createTest("Get Card");

        if (cardId == null) {
            test.fail("Card ID is null. Skipping testGetCard.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve card");
        test.pass("Card fetched successfully");
    }

    @Test(priority = 7)
    public void createChecklist() {
        test = report.createTest("Create Checklist");

        if (cardId == null) {
            test.fail("Card ID is null. Skipping testCreateChecklist.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists")
                .queryParam("idCard", cardId)
                .queryParam("name", "Rest Assured Checklist")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Checklist creation failed");

        if (response.getStatusCode() == 200) {
            checklistId = response.jsonPath().getString("id");
            test.pass("Checklist created: " + checklistId);
        } else {
            test.fail("Checklist creation failed.");
        }
    }

    @Test(priority = 8)
    public void getChecklist() {
        test = report.createTest("Get Checklist");

        if (checklistId == null) {
            test.fail("Checklist ID is null. Skipping testGetChecklist.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().get()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve checklist");
        test.pass("Checklist fetched successfully");
    }

    @Test(priority = 9)
    public void addCheckItem() {
        test = report.createTest("Add CheckItem");

        if (checklistId == null) {
            test.fail("Checklist ID is null. Skipping testAddCheckItem.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems")
                .queryParam("name", "Item_1")
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .when().post()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "CheckItem creation failed");

        if (response.getStatusCode() == 200) {
            checkItemId = response.jsonPath().getString("id");
            test.pass("CheckItem created: " + checkItemId);
        } else {
            test.fail("CheckItem creation failed.");
        }
    }

    @Test(priority = 10)
    public void deleteCheckItem() {
        test = report.createTest("Delete CheckItem");

        if (checkItemId == null || checklistId == null) {
            test.fail("Required IDs are null. Skipping testDeleteCheckItem.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems/" + checkItemId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "CheckItem deletion failed");
        test.pass("CheckItem deleted");
    }

    @Test(priority = 11)
    public void deleteChecklist() {
        test = report.createTest("Delete Checklist");

        if (checklistId == null) {
            test.fail("Checklist ID is null. Skipping testDeleteChecklist.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Checklist deletion failed");
        test.pass("Checklist deleted");
    }

    @Test(priority = 12)
    public void xCheckItemOnDeletedChecklist() {
        test = report.createTest("Negative - Add CheckItem on Deleted Checklist");

        if (checklistId == null) {
            test.fail("Checklist ID is null. Skipping negative test.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/checklists/" + checklistId + "/checkItems")
                .queryParam("name", "NewItem")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().post()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "Unexpectedly succeeded");
        test.pass("Properly rejected adding item to deleted checklist.");
    }

    @Test(priority = 13)
    public void deleteCard() {
        test = report.createTest("Delete Card");

        if (cardId == null) {
            test.fail("Card ID is null. Skipping testDeleteCard.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Card deletion failed");
        test.pass("Card deleted");
    }

    @Test(priority = 14)
    public void xUpdateDeletedCard() {
        test = report.createTest("Negative - Update Deleted Card");

        if (cardId == null) {
            test.fail("Card ID is null. Skipping test.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/cards/" + cardId)
                .queryParam("name", "Updated Name")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().put()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "Unexpectedly updated a deleted card");
        test.pass("Correctly rejected update on deleted card.");
    }

    @Test(priority = 15)
    public void deleteBoard() {
        test = report.createTest("Delete Board");

        if (boardId == null) {
            test.fail("Board ID is null. Skipping testDeleteBoard.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .when().delete()
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Board deletion failed");
        test.pass("Board deleted successfully");
    }

    @Test(priority = 16)
    public void xUpdateDeletedBoard() {
        test = report.createTest("Negative - Update Deleted Board");

        if (boardId == null) {
            test.fail("Board ID is null. Skipping test.");
            return;
        }

        Response response = RestAssured.given()
                .baseUri(baseUrl + "/boards/" + boardId)
                .queryParam("name", "New Board Name")
                .queryParam("key", key)
                .queryParam("token", token)
                .when().put()
                .then().extract().response();

        Assert.assertNotEquals(response.getStatusCode(), 200, "Unexpectedly updated a deleted board");
        test.pass("Correctly rejected update on deleted board.");
    }
}
