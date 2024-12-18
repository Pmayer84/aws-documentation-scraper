package aws

import io.scalastic.aws.{AwsDocumentation, AwsWebScraper}
import sttp.client3.UriContext
import scala.collection.mutable
import ujson.Obj
import java.io.PrintWriter
import scala.jdk.CollectionConverters._

object PageFactoryPaginationTest extends App with AwsWebScraper {

  def runTests(): Unit = {
    val baseUrl = AwsDocumentation.entrypointDocumentationUrl
    val maxDepth = 6

    // Initialize a mutable set to keep track of visited URLs
    val visitedUrls = mutable.Set[String]()

    // Initialize a mutable list to collect results
    val results = mutable.ListBuffer[ujson.Value]()

    // Function to recursively scrape pages up to the specified depth
    def scrapePage(url: String, depth: Int): Unit = {
      if (depth > maxDepth || visitedUrls.contains(url)) return

      visitedUrls += url

      try {
        val pageJson = AwsDocumentation.extractDocumentation(url)
        val page = identifyPageType(pageJson, url)

        println(s"Scraping URL: $url at depth $depth")
        println(s"Page Type: ${page.pageType}")
        println(s"Extracted JSON Content: $pageJson")

        // Collect the result
        results += ujson.Obj("url" -> url, "pageType" -> page.pageType)

        // Recursively process the documentation links
        val completeJson = AwsDocumentation.completeRootDocumentation(pageJson)
        val links = extractLinksFromJson(completeJson, baseUrl)
        println(s"Extracted Links from $url: ${links.mkString(", ")}")

        links.foreach { link =>
          println(s"Following link: $link")
          scrapePage(link, depth + 1)
        }
      } catch {
        case e: Exception => println(s"Failed to scrape $url: ${e.getMessage}")
      }
    }

    // Function to extract links from the processed JSON content
    def extractLinksFromJson(jsonContent: ujson.Value, baseUrl: String): Seq[String] = {
      jsonContent("bodyContent").str.split("\\s+").filter(_.startsWith(baseUrl)).toSeq
    }

    // Function to save the results to a JSON file
    def saveResultsToJson(): Unit = {
      val json = ujson.Arr(results.toSeq: _*)
      val writer = new PrintWriter("src/test/scala/aws/pagination_test.json")
      writer.write(json.render())
      writer.close()
    }

    // Start scraping from the base URL
    scrapePage(baseUrl, 0)

    // Save the collected results to a JSON file
    saveResultsToJson()

    println("Scraping completed!")
  }

  def identifyPageType(jsonContent: ujson.Value, url: String): DocumentationPage = {
    val metaTags = jsonContent("metaTags").obj
    val metaServiceName = metaTags.getOrElse("service-name", ujson.Str("")).str
    val metaGuideName = metaTags.getOrElse("guide-name", ujson.Str("")).str
    val metaThisDocGuide = metaTags.getOrElse("this_doc_guide", ujson.Str("")).str
    val metaGuide = metaTags.getOrElse("guide", ujson.Str("")).str

    if (metaServiceName == "Main Landing Page" && metaGuideName == "Landing Page") new MainPage(jsonContent, url)
    else if (metaServiceName == "Documentation" && metaGuideName == "Landing Page") new SubPage(jsonContent, url)
    else if (metaThisDocGuide == "User Guide") new UserGuidePage(jsonContent, url)
    else if (metaGuide == "Developer Guide") new DevGuidePage(jsonContent, url)
    else new UnknownPage(jsonContent, url)
  }

  runTests()
}

// Define a trait for pages
trait DocumentationPage {
  def extract: ujson.Value
  def pageType: String
}

// Define various implementations of DocumentationPage
class MainPage(val jsonContent: ujson.Value, val url: String) extends DocumentationPage {
  def extract: ujson.Value = jsonContent
  def pageType: String = "MainPage"
}

class SubPage(val jsonContent: ujson.Value, val url: String) extends DocumentationPage {
  def extract: ujson.Value = jsonContent
  def pageType: String = "SubPage"
}

class UserGuidePage(val jsonContent: ujson.Value, val url: String) extends DocumentationPage {
  def extract: ujson.Value = jsonContent
  def pageType: String = "UserGuidePage"
}

class DevGuidePage(val jsonContent: ujson.Value, val url: String) extends DocumentationPage {
  def extract: ujson.Value = jsonContent
  def pageType: String = "DevGuidePage"
}

class UnknownPage(val jsonContent: ujson.Value, val url: String) extends DocumentationPage {
  def extract: ujson.Value = jsonContent
  def pageType: String = "UnknownPage"
}
