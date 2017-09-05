package com.piotrwalkusz.lebrb.steps

import com.piotrwalkusz.lebrb.LEBRBApplication
import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.security.UserRepository
import com.piotrwalkusz.lebrb.security.UserService
import com.piotrwalkusz.lebrb.security.UserService.UserServiceException
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepository
import cucumber.api.DataTable
import cucumber.api.PendingException
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.jsoup.Jsoup
import org.junit.Assert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext

@ContextConfiguration(classes = LEBRBApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StepDefinitions {

    @Autowired private UserService userService
    @Autowired private UserRepository userRepository
    @Autowired private WordsRepository wordsRepository

    @Autowired private WebDriver driver

    private final def ROOT_URL = "http://localhost:8080"

    @Given('^I am a guest user$')
    void i_am_a_guest_user() {
        driver.get(ROOT_URL + '/logout')
    }

    @Given('^I visit the \"([^"]*)\" site$')
    void i_visit_the_site(String url) {
        driver.get(ROOT_URL + url)
    }

    @When('^I click the \"([^"]*)\" button$')
    void i_click_the_login_button(String btnName) {
        driver.findElementByClassName("$btnName-btn").click()
    }

    @Then('^I should be on the \"([^"]*)\" site$')
    void i_should_be_on_the_site(String url) {
        Assert.assertEquals(ROOT_URL + url, driver.getCurrentUrl())
    }

    @Given('^I am a logged in user(?: with a username "([^"]*)")?(?: and)?(?: with a password "([^\"]*)")?$')
    void i_am_a_logged_in_user(String username, String password) {
        username = username ?: 'username'
        password = password ?: 'password'

        try {
            userService.registerUser(username, password)
        }
        catch (UserServiceException ignored) {}

        driver.get(ROOT_URL + '/login')
        driver.findElement(By.name('username')).sendKeys(username)
        driver.findElement(By.name('password')).sendKeys(password)
        driver.findElement(By.name('submit')).click()
    }

    @Then('^I should see a \"([^"]*)\" text$')
    void i_should_see_text(String text) {
        driver.findElementByXPath("//*[contains(text(), '$text')]")
    }

    @Then('^I should not see a \"([^"]*)\" text$')
    void i_should_not_see_text(String text) {
        try {
            driver.findElementByXPath("//*[contains(text(), '$text')]")
            Assert.fail()
        }
        catch (org.openqa.selenium.NoSuchElementException ignore) {}
    }

    @Then('I click the submit button')
    void i_click_the_submit_button() {
        driver.findElementByXPath('//input[@type="submit"]').click()
    }

    @When('I upload any file')
    void i_upload_a_file() {
        uploadFile('/steps/any_file')
    }

    @When('^I upload a valid file$')
    void i_upload_a_valid_file() {
        uploadFile('/steps/valid.pdf')
    }

    @When('^I upload a file with invalid extension$')
    void i_upload_a_file_with_invalid_extension() {
        uploadFile('/steps/invalid_extension.png')
    }

    @When('^I upload a file with content$')
    void i_upload_a_file_with_content(String content) {
        File tmpFile = File.createTempFile('file-with-custom-content', '.txt')
        tmpFile.write(content)
        driver.findElementByXPath('//input[@type="file"]').sendKeys(tmpFile.absolutePath)
    }

    @When('^I upload a file with size (\\d+) bytes$')
    void i_upload_a_file_with_size(int size) {
        File tmpFile = File.createTempFile('file-with-custom-content', '.txt')
        RandomAccessFile file = new RandomAccessFile(tmpFile, "rw")
        file.setLength(size)

        driver.findElementByXPath('//input[@type="file"]').sendKeys(tmpFile.path)
    }

    @Then('^I should see a table$')
    void i_should_see_a_table(){
        driver.findElementByTagName('table')
    }

    @Then('^I should see a chart$')
    void i_should_see_a_chart() {
        driver.findElement(By.id('chart'))
    }

    @Then('^I should see a table whose body looks like$')
    void i_should_see_a_table_whose_body_looks_like(DataTable dataTable) {
        def tables = driver.findElements(By.tagName('table'))
        for (def table : tables) {
            if (table.isDisplayed()) {
                diffTable(dataTable, table)
            }
        }
    }

    @Then('^I should see a linear chart that looks like$')
    void i_should_see_a_linear_chart_that_looks_like(DataTable chartData) {
        def table = driver.findElement(By.xpath(
                '//div[@aria-label="A tabular representation of the data in the chart."]//table'))

        diffTable(chartData, table)
    }

    @Given('^The user \"([^"]*)\" knows \"([^"]*)\" words$')
    void user_knows_words(String username, String words) {
        def wordsIndices = []
        def dictionary = wordsRepository.getWordsByLanguage(Language.English)
        for (def word : words.tokenize(' ,')) {
            int index = dictionary.indexOf(word)
            if (index == -1) {
                throw new IllegalStateException("The dictionary doesn't contain the \"$word\" word")
            }
            else {
                wordsIndices.add(index)
            }
        }
        def userEntity = userRepository.findByUsernameAndFetchKnownWords(username)
        userEntity.knownWords.compute(Language.English, { _, v -> v ? v + wordsIndices : wordsIndices})
        userRepository.save(userEntity)
    }

    @Then('^A row containing a cell with "([^"]*)" text should have "([^"]*)" class$')
    void row_containing_cell_with_text_should_have_class(String text, String className) {
        def row = driver.findElement(By.xpath("//td[contains(text(), '$text')]/.."))
        assert row.getAttribute('class').tokenize(' ,').contains(className)
    }

    @Then('^A row containing a cell with "([^"]*)" text should not have "([^"]*)" class$')
    void row_containing_cell_with_text_should_not_have_class(String text, String className) {
        def row = driver.findElement(By.xpath("//td[contains(text(), '$text')]/.."))
        assert ! row.getAttribute('class').tokenize(' ,').contains(className)
    }

    @When('^I click the button in row containing cell with "([^"]*)" text$')
    void i_click_the_button_in_row_containing_cell_with_text(String text) {
        def row = driver.findElement(By.xpath("//td[contains(text(), '$text')]/.."))
        def button = row.findElement(By.xpath("//*[contains(@class, '-btn')]"))
        button.click()
    }

    @Then('^I should download a file with content$')
    void i_should_download_a_file_with_content(String arg1) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    private void uploadFile(String name) {
        URL url = getClass().getResource(name)
        driver.findElementByXPath('//input[@type="file"]').sendKeys(url.path)
    }

    private static void diffTable(DataTable dataTable, WebElement elementTable) {
        def html = Jsoup.parse(elementTable.getAttribute('outerHTML'))
        def parsedTable = new ArrayList<ArrayList<String>>()

        for (def row : html.select('tr')) {
            def parsedRow = new ArrayList<String>()
            for (def cell : row.children()) {
                parsedRow.add(cell.text())
            }
            parsedTable.add(parsedRow)
        }

        dataTable.diff(parsedTable)
    }
}