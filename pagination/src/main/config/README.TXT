# Release to artifactory/check in release to github
mvn release:prepare -Darguments="-DskipTests"
mvn release:perform -Darguments="-DskipTests"

Test