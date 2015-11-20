# Misc API

[Back to APIv2](./APIv2.md#api-v2)

[Account API](#account-api)

[Partition API](#partition-api)

[Miscellaneous API](#miscellaneous-api)

[Files API](#files-api)

# Account API

## Relevant classes

[ValidAccountDTO](../../src/main/java/com/superum/api/v2/account/ValidAccountDTO.java)

### Commands

#### Create admin
```
    POST  /account
    BODY  ValidAccountDTO
    RET   ValidAccountDTO
```

Creates a new admin account in the same partition as the request

It will fail if:
  * HTTP 400; username or password were not set;
  * HTTP 500; the username is already taken;

Returned ValidAccount will not contain the password

------

#### Update password
```
    PUT   /account
    BODY  ValidAccountDTO
    RET   ValidAccountDTO
```

Updates an existing account; only password field is updated;
you can only update the account using the credentials of the account being updated

It will fail if:
  * HTTP 400; username or password were not set;
  * HTTP 401; the request was made using another account's credentials;
  * HTTP 404; an account with given username does not exist;
  * HTTP 500; the username is already taken;

Returned ValidAccount contains all the fields as they were before updating, except password

### Queries

#### Read account
```
    GET  /account
    REQ  username       String         any, username.length() <= 60
    RET  ValidAccountDTO
```

Reads and returns an existing account

It will fail if:
  * HTTP 404; an account with given username does not exist;

Returned ValidAccount contains all the fields, except password

# Partition API

## Relevant classes

[ValidPartitionDTO](../../src/main/java/com/superum/api/v2/partition/ValidPartitionDTO.java)

### Commands

#### Create partition
```
    POST  /partition
    BODY  ValidPartitionDTO
    RET   ValidPartitionDTO
```

Creates a new partition; it is also returned

It will fail if:
  * HTTP 500; the id or name of partition is already taken;

### Queries

N/A

# Miscellaneous API

### Commands

N/A

### Queries

#### Read time zones
```
    GET  misc/time/zones
    RET  Set<String>
```

Returns the set of valid timezones, used by Java 8

It shouldn't fail under normal circumstances

------

#### Read time
```
    GET  misc/time
    RET  long
```

Returns current epoch milliseconds

It shouldn't fail under normal circumstances

------

#### Convert time
```
    GET  misc/time/convert
    OPT  time_zone      String          any time zone; DEF UTC
    OPT  date           String          any Date; DEF today evaluated at time_zone
    OPT  hour           int             0 <= hour; DEF 0
    OPT  minute         int             0 <= minute; DEF 0
    OPT  second         int             0 <= second; DEF 0
    OPT  millisecond    int             0 <= millisecond; DEF 0
    RET  long
```

Returns epoch milliseconds evaluated for given parameters, using JodaTime

It will fail if:
  * HTTP 500; hour, minute, second and millisecond add up to more than a day;

# Files API

## Info

THIS API DOES NOT USE THE DEFAULT PREFIX!
Instead, the prefix is

    http://{IP}:{PORT}/

### Commands

#### Upload file
```
    POST  /{folder}/upload
          folder         String         documents or pictures
    REQ   name           String         basic name of the file (should not contain extension, i.e. .txt)
    REQ   file           MultipartFile  file being uploaded
    RET   String
```

Uploads a file to the folder and returns its name, adjusted like this:

    name + "current epoch millis" + "file extension from MultipartFile"

If it fails, it will return an error message; it will fail if:
  * "documents" folder file exceeds 5MB;
  * "pictures" folder file exceeds 1MB;
  * couldn't save the file for some reason;
  
------
    
#### Delete file
```
    DELETE  /{folder}/{fileName}
            folder         String         documents or pictures
            fileName       String         name of the file
    RET     void
```

Deletes the file from the folder; full file name is required;

It will fail if:
  * HTTP 404; a file with given name does not exist in the folder;
  * HTTP 500; file deletion failed for some reason;

### Queries

#### Read file
```
    GET  /{folder}/{fileName}
         folder         String         documents or pictures
         fileName       String         name of the file
    RET  org.springframework.core.io.FileSystemResource
```

Returns the file from the folder; full file name is required;

It will fail if:
  * HTTP 404; a file with given name does not exist in the folder;

Pictures are returned in a way they can be displayed, whereas documents use **`application/octet-stream`** which allows them to be downloaded
