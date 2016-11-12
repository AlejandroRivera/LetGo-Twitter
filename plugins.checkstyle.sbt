checkstyleConfigLocation := CheckstyleConfigLocation.File("checkstyle-config.xml")

checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Warning)

checkstyle in IntegrationTest <<= checkstyleTask(IntegrationTest)

// Prior to compiling tests, execute checkstyle-check for both prod and test files
(checkstyle in Compile)         <<= (checkstyle in Compile)         triggeredBy (compile in Compile)
(checkstyle in Test)            <<= (checkstyle in Test)            triggeredBy (compile in Test)
(checkstyle in IntegrationTest) <<= (checkstyle in IntegrationTest) triggeredBy (compile in IntegrationTest)
