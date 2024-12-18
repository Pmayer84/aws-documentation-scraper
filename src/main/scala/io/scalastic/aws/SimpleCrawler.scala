package io.scalastic.aws

import scala.util.{Failure, Success}
import sttp.model.Uri
import java.io.{File, IOException}
import java.net.URLDecoder
import scala.xml.XML
import sttp.client3._
import ujson._

object SimpleCrawler extends AwsWebScraper with IO with App {

  val rootDocumentationUrl: String = "https://docs.aws.amazon.com"
  val entrypointDocumentationUrl: String = rootDocumentationUrl
  val documentationPath: String = "./data/"
  val outputFilename: String = "SimpleCrawler.json"
  val outputDir = "C:\\Users\\petermayer\\OneDrive - Microsoft\\Documents\\Desktop\\scraping_projects\\aws_scraping\\aws_scraper_github\\aws-documentation-scraper\\data\\test"

  var pageCount = 0 // Global counter for the number of pages crawled
  var crawledData = Seq.empty[(String, String)] // Store results as they are fetched

  // Create the directory if it doesn't exist
  val directory = new File(documentationPath)
  if (!directory.exists()) {
    println(s"Creating directory: $documentationPath")
    directory.mkdirs()
  } else {
    println(s"Directory already exists: $documentationPath")
  }

  def crawlAndCategorize(url: String, depth: Int): Seq[(String, String)] = {
    if (depth > 5) return Seq() // Stop condition to prevent infinite recursion

    pageCount += 1 // Increment the page count
    println(s"Page Count: $pageCount") // Log the page count

    try {
      val pageUrl: String = if (url.startsWith("http")) url else rootDocumentationUrl + url
      println(s"Processing URL (Depth $depth): $pageUrl")
      val htmlContent = fetchHtmlFromUrl(pageUrl)
      if (htmlContent.isEmpty) {
        println(s"Failed to fetch HTML content from $pageUrl")
        return Seq()
      }

      // Use PageFactory to identify the page type
      val page = WebPageFactory.build(Uri.unsafeParse(pageUrl))
      val pageType = page.extract("pageType").str

      // Skip pages that are classified as "Unknown"
      if (pageType == "Unknown") {
        println(s"Skipping page due to Unknown type: $pageUrl")
        return Seq() // Skip this page and do not add to results
      }

      // Log page type
      println(s"Page Type for $pageUrl: $pageType")

      // Add the current page's result to crawledData
      crawledData :+= (pageUrl, pageType)

      // Save the results periodically every 10 pages
      crawledData = PeriodicSave.savePeriodically(crawledData, pageCount, documentationPath, outputFilename)

      // Find links in the current page and follow them recursively
      val links = findLinksInPage(htmlContent)
      println(s"Found Links: $links")

      val categorizedLinks = links.flatMap { link =>
        val absoluteLink = if (link.startsWith("http")) link else rootDocumentationUrl + link
        crawlAndCategorize(absoluteLink, depth + 1)
      }

      // Return results for this page and its links
      crawledData

    } catch {
      case e: IllegalArgumentException =>
        println(s"Error processing URL $url: Invalid URL format - ${e.getMessage}")
        Seq()
      case e: IOException =>
        println(s"Error processing URL $url: I/O Error - ${e.getMessage}")
        Seq()
      case e: Exception =>
        println(s"Error processing URL $url: ${e.getMessage}")
        Seq()
    }
  }

  // Start the crawl process
  crawledData = crawlAndCategorize(entrypointDocumentationUrl, 0)
  println(s"Total pages crawled: $pageCount")
  println(s"Crawled data: $crawledData")

  // Write the final results to a JSON file if any data was crawled
  if (crawledData.nonEmpty) {
    PeriodicSave.writeResultsToFile(crawledData, documentationPath, outputFilename)
  } else {
    println("No data to write")
  }

  // Function to find links in the HTML content
  override def findLinksInPage(htmlContent: String): Seq[String] = {
    // Extract links from <a> tags
    val linkPattern = """<a\s+href=["']([^"']+)["']""".r
    val aLinks = linkPattern.findAllMatchIn(htmlContent).map(_.group(1)).toSeq
    println(s"Extracted Links from <a> tags: $aLinks")

    // Extract and decode links from <input> tags
    val inputPattern = """<input[^>]*id=["']landing-page-xml["'][^>]*value=["']([^"']+)["'][^>]*>""".r
    val inputLinks = inputPattern.findAllMatchIn(htmlContent).flatMap { m =>
      val encodedValue = m.group(1)
      try {
        val decodedValue = URLDecoder.decode(encodedValue, "UTF-8")
        println(s"Decoded XML: ${decodedValue.take(200)}") // Print first 200 characters for inspection

        val xmlContent = XML.loadString(decodedValue)
        (xmlContent \\ "list-card-item").map(node => (node \ "@href").text)
      } catch {
        case e: Exception =>
          println(s"Error decoding or parsing XML: ${e.getMessage}")
          Seq()
      }
    }.toSeq
    println(s"Extracted Links from <input> tags: $inputLinks")

    val allLinks = aLinks ++ inputLinks
    println(s"All Extracted Links: $allLinks")
    allLinks
  }

  // Function to fetch HTML content from URL
  override def fetchHtmlFromUrl(url: String): String = {
    val backend = HttpURLConnectionBackend()
    val response = basicRequest.get(Uri.unsafeParse(url)).send(backend)
    response.body match {
      case Right(html) => html
      case Left(error) =>
        println(s"Failed to fetch HTML content: $error")
        ""
    }
  }
}
