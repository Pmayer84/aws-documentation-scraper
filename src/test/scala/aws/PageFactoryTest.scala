package aws

import io.scalastic.aws.{PageFactory, Page}
import sttp.model.Uri
import scala.io.Source

object PageFactoryTest extends App {

  def runTests(): Unit = {
    val mainPageHtml = Source.fromFile("test_html/main_page.html").mkString
    val subPageHtml = Source.fromFile("test_html/sub_page.html").mkString
    val userGuidePageHtml = Source.fromFile("test_html/user_guide_page.html").mkString
    val devGuidePageHtml = Source.fromFile("test_html/dev_guide_page.html").mkString
    val unknownPageHtml = Source.fromFile("test_html/unknown_page.html").mkString

    // Mocking the PageFactory.build method to use local HTML files for testing
    def mockBuild(htmlContent: String, url: String): Page = {
      val jsonContent = PageFactory.processHtmlContent(htmlContent, url)
      PageFactory.identifyPageType(jsonContent, url)
    }

    // Printing detailed information for each test case
    def runTest(htmlContent: String, url: String, expectedPageType: String): Unit = {
      val page = mockBuild(htmlContent, url)
      println(s"URL: $url")
      println(s"Expected Page Type: $expectedPageType")
      println(s"Actual Page Type: ${page.pageType}")
      println(s"JSON Content: ${page.extract}")
      assert(page.pageType == expectedPageType, s"$expectedPageType Test Failed")
    }

    runTest(mainPageHtml, "http://example.com/main", "MainPage")
    runTest(subPageHtml, "http://example.com/sub", "SubPage")
    runTest(userGuidePageHtml, "http://example.com/userguide", "UserGuidePage")
    runTest(devGuidePageHtml, "http://example.com/devguide", "DevGuidePage")
    runTest(unknownPageHtml, "http://example.com/unknown", "UnknownPage")

    println("All tests passed!")
  }

  runTests()
}
