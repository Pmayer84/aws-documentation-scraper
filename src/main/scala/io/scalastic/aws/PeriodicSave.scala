package io.scalastic.aws

import java.io.File
import ujson._

object PeriodicSave {

  // Method to write the data to a JSON file
  def writeResultsToFile(crawledData: Seq[(String, String)], documentationPath: String, outputFilename: String): Unit = {
    val outputPath = new File(documentationPath, outputFilename).getAbsolutePath
    println(s"Writing to file: $outputPath")

    // Write the JSON to the file using a PrintWriter
    val writer = new java.io.PrintWriter(new java.io.File(outputPath))
    try {
      writer.write(ujson.write(ujson.Arr(crawledData.map { case (url, category) =>
        ujson.Obj("url" -> url, "category" -> category)
      }: _*), indent = 2)) // Properly serialize with indentation
      println(s"Successfully wrote crawled data to $outputPath")
    } catch {
      case e: Exception => println(s"Error writing to file: ${e.getMessage}")
    } finally {
      writer.close()
    }
  }

  // Method to save data periodically
  def savePeriodically(crawledData: Seq[(String, String)], pageCount: Int, documentationPath: String, outputFilename: String): Seq[(String, String)] = {
    if (pageCount % 10 == 0) {
      writeResultsToFile(crawledData, documentationPath, outputFilename)
    }
    crawledData
  }
}
