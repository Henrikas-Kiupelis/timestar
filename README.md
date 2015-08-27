# API links

## [APIv2](/API/v2/APIv2.md)

## [APIv3](/API/v3/APIv3.md) - only contains a few extensions to [APIv2](/API/v2/APIv2.md)

# Dev instructions

1. Go to src/main/resources/persistence.properties.fill
  1. Copy as persistence.properties;
  2. Fill in passwords/schemas/etc;
2. Go to src/main/resources/email.properties.fill
  1. Copy as email.properties;
  2. Fill in username/password;
3. Go to src/main/resources/sql;
  * Make the database and fill it with tables from .sql files;
4. Do some of these:
  * gradlew generate -PschemaProp=NAME_OF_SCHEMA -PusrProp=DB_USERNAME -PpwdProp=DB_PASSWORD
  * gradlew idea
  * gradlew eclipse
  * gradlew build
  * gradlew run
