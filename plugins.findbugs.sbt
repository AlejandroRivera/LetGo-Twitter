findbugsSettings
findbugsReportPath := Some(target.value / "findbugs" / "report.xml")
findbugsExcludeFilters := Some(scala.xml.XML.loadFile("findbugs-exclude-filters.xml"))

// Prior to compiling tests, execute checkstyle-check for both prod and test files
(compile in Test) <<= (compile in Test) dependsOn (findbugsCheck)
