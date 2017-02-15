# Upload compiled, source, and javadoc jar files to github
mvn clean install

# Generate site and upload to github
mvn site

# Release to artifactory/check in release to github
mvn release:prepare -Darguments="-DskipTests"
mvn release:perform -Darguments="-DskipTests"