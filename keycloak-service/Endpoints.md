# API endpoints

These are the endpoints present in the Keycloak Service.

## GET
`your-host` [/api/userDetails](#get-apiuserdetails)  | Access: `Admin`, `User` <br/>
`your-host` [/api/userDetails/{id}](#get-apiuserdetailsid) | Access: `Admin` <br/>
`your-host` [/api/userDetails/batch](#get-apiuserdetailsbatch) | Access: `Admin` <br/>

## POST
`your-host` [/api/register](#post-apiregister) | Access: Anyone <br/>
`your-host` [/api/login](#post-apilogin) | Access: Anyone <br/>
___
### GET /api/userDetails

Get the current user's details.

#### **Parameters**

None

#### **Responses**

```
# Successful retrival of current user details

{
  "id": 9007199254740991,
  "subject": "string",
  "username": "string",
  "email": "string"
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T10:39:17.514+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/userDetails"
}
	
# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/userDetails"
}
```
___

### GET /api/userDetails/{id}

Get the user's details by their Id.

#### **Parameters**

|Name| Method | Required |  Type   |                    Description                    |
| --:|:------:|:--------:|:-------:| ------------------------------------------------- |
|`id`| `Path` |    Yes   | integer |The id of the user. <br/> Example values: `1`, `2`.|



#### **Responses**

```
# Successful retrival of User details with the provided Id

{
  "id": 9007199254740991,
  "subject": "string",
  "username": "string",
  "email": "string"
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T10:39:17.514+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/userDetails/1"
}

	
# Forbidden Access

{
  "timestamp": "2025-03-20T10:45:48.158+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/userDetails/1"
}

	
# No user found with the provided Id

{
  "timestamp": "2025-03-20T10:53:40.922848500Z",
  "status": 404,
  "error": "Not Found",
  "message": "User with Id: 100 not found",
  "path": "/api/userDetails/100"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/userDetails/100"
}
```
___

### GET /api/userDetails/batch

Get multiple users' details by their Ids.

#### **Parameters**

|  Name   | Method | Required |    Type      |                         Description                          |
| -------:|:------:|:--------:|:------------:| ------------------------------------------------------------ |
|`userIds`|`Query` |   Yes    |array[integer]|The ids of the users. <br/> Example values: `[1]`, `[2,4,13]`.|



#### **Responses**

```
# Successful retrival of multiple User details with the provided Ids

[
  {
    "id": 9007199254740991,
    "subject": "string",
    "username": "string",
    "email": "string"
  }
]

# Bad Request

{
	"timestamp": "2025-06-23T14:03:18.588646600Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Required request parameter 'userIds' for method parameter type List is not present",
	"path": "/api/userDetails/batch"
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T10:39:17.514+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/userDetails/batch"
}

	
# Forbidden Access

{
  "timestamp": "2025-03-20T10:45:48.158+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/userDetails/batch"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/userDetails/batch"
}
```
___

### POST /api/register

Register a user.

#### **Parameters**

|   Name    | Method | Required |  Type  |                         Description                             |
| ---------:|:------:|:--------:|:------:| ----------------------------------------------------------------|
|  `email`  | `Body` |    Yes   | string | The email of the user. <br/> Example values: `johnd@email.com`. |
|`username` | `Body` |    Yes   | string | The username of the user. <br/> Example values: `johnd`.        |
|`password` | `Body` |    Yes   | string | The password of the user. <br/> Example values: `some-password`.|
|`firstName`| `Body` |    No    | string | The first name of the user. <br/> Example values: `John`.       |
|`lastName` | `Body` |    No    | string | The last name of the user. <br/> Example values: `Doe`.         |

#### **Responses**

```
# Successful User registration

{
  "message": "User Created",
  "username": "johnd"
}

# Bad Request

{
  "timestamp": "2025-03-20T10:06:45.854780900Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid Request",
  "path": "/api/register"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/register"
}

# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service temporarily unavailable",
  "path": "/api/register"
}
```
___

### POST /api/login

Login with your username and password.

#### **Parameters**

|   Name   | Method | Required |  Type  |                          Description                            |
| --------:| :----: |:--------:|:------:| ----------------------------------------------------------------|
|`username`| `Body` |    Yes   | string | The username of the user. <br/> Example values: `johnd`.        |
|`password`| `Body` |    Yes   | string | The password of the user. <br/> Example values: `some-password`.|

#### **Responses**

```
# Successful User login

{
  "access_token": "string",
  "refresh_token": "string",
  "expires_in": 9007199254740991,
  "refresh_expires_in": 9007199254740991,
  "token_type": "string",
  "session_state": "string",
  "scope": "string"
}
	
# Invalid Details

{
  "timestamp": "2025-03-19T17:54:09.534161300Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid Credentials",
  "path": "/api/login"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/login"
}

# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service temporarily unavailable",
  "path": "/api/login"
}
```