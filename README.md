# API links

## [APIv2](/API/v2/APIv2.md#api-v2)

## [APIv3](/API/v3/APIv3.md#api-v3) - only contains a few extensions to [APIv2](/API/v2/APIv2.md#api-v2)

# Dev instructions

1. Request properties files from a developer
  * persistence.properties
  * email.properties
2. Go to src/main/resources/
  * put the files in there
  * run 'sql/Create {version}.sql' to fill your MySQL db (WARNING! it will delete the old version if such exists)
3. Do some of these
  * gradlew generate -PschemaProp=NAME_OF_SCHEMA -PusrProp=DB_USERNAME -PpwdProp=DB_PASSWORD
  * gradlew idea
  * gradlew eclipse
  * gradlew build
  * gradlew run
