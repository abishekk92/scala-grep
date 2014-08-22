package com.abishekk92

import org.yaml.snakeyaml._
import scala.io.Source

class ConfigParser {

  def getConfig() : String =
      Source.fromFile("config.yml").mkString

  def parse(){
    try { 
      val config_string = getConfig();
      val yaml          = new Yaml();
      wrap_config(yaml.load(config_string))
    } catch {
      case fnf:java.io.FileNotFoundException => println("Please check if the file is present.")
    }
  }
}
