package StepDefinitions;

import Utilities.TestConfig;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.restassured.RestAssured;

public class Hooks {

    @BeforeAll
    public static void beforeScenario() 
    {
        System.out.println("Before scenario");
        RestAssured.baseURI = TestConfig.getBaseUri();
        // this method will allow us to bypass SSL certificate validation if there are org level network blockers
//        RestAssured.useRelaxedHTTPSValidation();
        
    }

    @AfterAll
    public static void afterScenario() 
    {
        System.out.println("After scenario");        
        RestAssured.reset();
    }

}