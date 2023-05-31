# STCTest
STC test the packages  
Tools and Libraries:

	This project using Selenium using Junit. On the other hand, I am using some tools that support this framework. The complete list of tools, you can see in the pom.xml file.

Requirements:

	Java Development Kit 19
	Maven
	WebDriver, using ChromeDriver

Running Tests:

	Download the project from the GitHub
	Open the project using any Java IDE
	Run the tests that are added to the class displayed to the user under the test folder in the class TestingFunctions

Test Results:

	Test report automatically generated on target folder (Reports) after finished the test execution
	See test report from STCTest-master\Reports\"Name of the project + test date and time".html

list of tested feature:

	Test the presence of all the elements that are displayed to the user in the main page.

	Test the presence of all the elements in each selected country from all aspects (currency of the plans, price of the plans, and all other elements displayed to the user in each country)

Test sequance:

	When reuning the test the user will be presented by a wizard that will present the function of this wizard and this test.
	The next page of the wizard will be displayed to the user with two selected fields for the user to select the browser that is needed for the test along with the country that the test will be performed on.
	The driver that is responsible about the selected brwoser will run and the test will start for the selected test, the selected brwoser and the selected country.

Code clarifications:

	The framework and the main elements used in this test are presented in the main class in the main package, the functions are the following:

	1. setupCreation responsible about intiating the reports and the driver by calling the function driverSettings and initializeReport.
	2. readLocator is responsible about reading the elements from the XML files added to the project.
	3. TestRule will apply thhe report and start it and determine that the test is passed or failed and take the needed action.
	4. screenShotOnFailuer will take a screen shot for the page that the test didnt pass on it.
	5. closeTest will end the test and close the driver and the windows that are opened during the test.
	6. readTags facilitate the reading of the elements using the readLocator function.
	7. inputDialog initiate the wizard that will be displayed to the user at the biggning of the test.
	8. driverSettings will intialize the driver according to the user selected value from the wizard for the brwoser.
	9. initializeReport create the report that is produced by the end of the test.
	
	The wizard pages are and classes are displayed to the user under the wizard package.
	
	The testing scenarios are displayed to the user under the test\java\TestingFunctions.java
