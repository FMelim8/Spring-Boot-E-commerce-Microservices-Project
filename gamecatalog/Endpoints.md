# API endpoints

These are the endpoints present in the Game Catalog Service.

## GET 
`your-host` [/api/games/{gameId}](#get-apigamesgameid) | Access: Anyone <br/> 
`your-host` [/api/games/batch](#get-apigamesbatch) | Access: Anyone  <br/>
`your-host` [/api/games/all](#get-apigamesall) | Access: Anyone  <br/>

## POST
`your-host` [/api/games/create](#post-apigamescreate) | Access: `Admin` <br/>

## PUT
`your-host` [/api/games/update/{gameId}](#put-apigamesupdategameid) | Access: `Admin` <br/>
`your-host` [/api/games/reduceQuantity/{gameId}](#put-apigamesreducequantitygameid)  | Access: `Admin`<br/>

## DELETE
`your-host` [/api/games/{gameId}](#delete-apigamesgameid) | Access: `Admin` <br/>
___
### GET /api/games/{gameId}

Get a game by its Id.

#### **Parameters**

|  Name  | Method | Required |  Type   |                    Description                    |
| ------:|:------:|:--------:|:-------:| ------------------------------------------------- |
|`gameId`| `Path` |    Yes   | integer |The id of the game. <br/> Example values: `1`, `2`.|

#### **Responses**

```
# Successful search

{
  "id": 1,
  "title": "Elden Ring",
  "description": "An open-world action RPG set in the Lands Between.",
  "genre": "Action",
  "type": "PS5",
  "price": 39.99,
  "stock": 88,
  "releaseDate": "2021-08-20"
}

# No Game found with the provided Id

{
  "timestamp": "2025-03-19T16:43:21.156306500Z",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with the id: 100",
  "path": "/api/games/100"
}
    
# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/100"
}
```
___

### GET /api/games/batch

Get multiple games' details by their Ids.

#### **Parameters**

|  Name   | Method | Required |      Type      |                        Description                           |
| -------:|:------:|:--------:|:--------------:| ------------------------------------------------------------ |
|`gameIds`|`Query` |   Yes    | array[integer] |The ids of the games. <br/> Example values: `[1]`, `[2,4,13]`.|



#### **Responses**

```
# Successful retrival of multiple Game details with the provided Ids

[
  {
    "id": 1,
    "title": "Elden Ring",
    "description": "An open-world action RPG set in the Lands Between.",
    "genre": "RPG",
    "type": "PC",
    "price": 59.99,
    "stock": 100
  }
]

# Bad Request

{
	"timestamp": "2025-06-23T14:03:18.588646600Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Required request parameter 'gameIds' for method parameter type List is not present",
	"path": "/api/games/batch"
}
    
# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/batch"
}
```
___

### GET /api/games/all

Get a paginated result of all games.

#### **Parameters**

|    Name   | Method | Required |  Type   | Default Value |                                                   Description                                                     |
| ---------:|:------:|:--------:|:-------:|:-------------:| ----------------------------------------------------------------------------------------------------------------- |
|   `page`  |`Query` |    No    | integer |      `1`      |                       The page of the search (Starts from 1). <br/> Example values: `1`, `4`.                     |
|   `size`  |`Query` |    No    | integer |      `10`     |                       The size of page of the search. <br/> Example values: `6`, `10`.                            |
|`sortField`|`Query` |    No    | string  |      `ID`     |The field to sort by. <br/> Available values: `ID`, `TITLE`, `PRICE`,<br/> `GENRE`, `TYPE`, `STOCK`, `RELEASEDATE`.|
|`direction`|`Query` |    No    | string  |      `ASC`    |                       The direction of the sort. <br/> Available values: `Asc`, `DESC`.                           |

#### **Responses**

```
# Successful Search

{
  "content": [
    {
      "id": 1,
      "title": "Elden Ring",
      "description": "An open-world action RPG set in the Lands Between.",
      "genre": "Action",
      "type": "PS5",
      "price": 39.99,
      "stock": 88,
      "releaseDate": "2021-08-20"
    },
    {
      "id": 2,
      "title": "God of War Ragnarok",
      "description": "Kratos and Atreus face Ragnarok in this Norse mythology epic.",
      "genre": "Action",
      "type": "PS5",
      "price": 69.99,
      "stock": 70,
      "releaseDate": "2022-11-09"
    }
  ],
  "totalPages": 14,
  "totalElements": 28,
  "size": 2,
  "page": 1,
  "empty": false
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/all"
}
```
___

### POST /api/games/create

Create a game.

#### **Parameters**

|     Name    | Method | Required  |  Type   |                              Description                              |
| -----------:|:------:|:---------:|:-------:| ----------------------------------------------------------------------|
|   `title`   | `Body` |    yes    | string  |     The title of the game. <br/> Example values: `some-title`.        |
|`description`| `Body` |    yes    | string  | The description of the game. <br/> Example values: `some-description`.|
|   `genre`   | `Body` |    yes    | string  |           The genre of the game. <br/> Example values: `Action`.      |
|   `type`    | `Body` |    yes    | string  |     The platform type of the game. <br/> Example values: `PS5`.       |
|   `price`   | `Body` |    yes    | float   |         The price of the game. <br/> Example values: `59.99`.         |
|   `stock`   | `Body` |    yes    | integer |           The stock of the game. <br/> Example values: `100`.         |
|`releaseDate`| `Body` |    yes    | string  |   The release date of the game. <br/> Example values: `2021-08-20`.   |

#### **Responses**

```	
# Successfully added a Game

{
  "id": 31,
  "title": "Ghost of Tsushima",
  "description": "An open-world samurai action-adventure set in feudal Japan.",
  "genre": "Action",
  "type": "PS5",
  "price": 59.99,
  "stock": 100
}

# Bad Request, incorrect format

{
  "timestamp": "2025-03-19T16:37:58.913142600Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request format",
  "path": "/api/games/create"
}

# Unauthorized Access

{
  "timestamp": "2025-03-19T16:07:56.883+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/games/create"
}

# Forbidden Access

{
  "timestamp": "2025-03-19T16:08:47.712+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/games/create"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/create"
}
```
___

### PUT /api/games/update/{gameId}

Update the details of a game.

#### **Parameters**

|     Name    | Method | Required |  Type   |                              Description                              |
| -----------:|:------:|:--------:|:-------:| ----------------------------------------------------------------------|
|   `gameId`  | `Path` |    Yes   | integer |         Id of the game to update. <br/> Example values: `1`.          |
|   `title`   | `Body` |    No    | string  |     The title of the game. <br/> Example values: `some-title`.        |
|`description`| `Body` |    No    | string  | The description of the game. <br/> Example values: `some-description`.|
|   `genre`   | `Body` |    No    | string  |           The genre of the game. <br/> Example values: `Action`.      |
|   `type`    | `Body` |    No    | string  |     The platform type of the game. <br/> Example values: `PS5`.       |
|   `price`   | `Body` |    No    | float   |         The price of the game. <br/> Example values: `59.99`.         |
|   `stock`   | `Body` |    No    | integer |           The stock of the game. <br/> Example values: `100`.         |
|`releaseDate`| `Body` |    No    | string  |   The release date of the game. <br/> Example values: `2021-08-20`.   |

#### **Responses**

```
# Successfully updated the details of a Game

{
  "id": 1,
  "title": "Elden Ring",
  "description": "An open-world action RPG set in the Lands Between.",
  "genre": "Action",
  "type": "PS5",
  "price": 39.99,
  "stock": 100,
  "releaseDate": "2021-08-20"
}
    
# Invalid Details

{
  "timestamp": "2025-06-23T15:46:07.861292034Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request format",
  "path": "/api/games/update/1"
}

# Unauthorized Access

{
  "timestamp": "2025-03-19T15:38:48.895+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/games/update/2"
}
	
# Forbidden Access

{
  "timestamp": "2025-03-19T15:41:16.588+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/games/update/2"
}
	
# No Game found with the provided Id

{
  "timestamp": "2025-03-19T15:42:58.976844800Z",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with the id: 100",
  "path": "/api/games/update/100"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/update/100"
}
```
___

### PUT /api/games/reduceQuantity/{gameId}

Reduce the stock quantity of a game.

#### **Parameters**

|   Name    | Method | Required |  Type  |                         Description                             |
| ---------:|:------:|:--------:|:------:| ----------------------------------------------------------------|
|  `gameId`  | `Path` |    Yes   | integer | Id of the game to update. <br/> Example values: `1`. |
|  `quantity`  | `Query` |    Yes   | integer | Quantity of a game to subtract. <br/> Example values: `5`. |

#### **Responses**

```
# Successfully updated the details of a Game

{
  "id": 1,
  "title": "Elden Ring",
  "description": "An open-world action RPG set in the Lands Between.",
  "genre": "RPG",
  "type": "PC",
  "price": 59.99,
  "stock": 14
}
	
# Insufficient Stock

{
  "timestamp": "2025-03-19T16:00:25.608396700Z",
  "status": 400,
  "error": "Insufficient Stock",
  "message": "Not enough stock",
  "path": "/api/games/reduceQuantity/1"
}

# Unauthorized Access

{
  "timestamp": "2025-03-19T15:57:28.754+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/games/reduceQuantity/1"
}
	
# Forbidden Access

{
  "timestamp": "2025-03-19T16:01:28.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/games/reduceQuantity/1"
}
	
# No Game found with the provided Id

{
  "timestamp": "2025-03-19T16:03:27.737633100Z",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with the id: 100",
  "path": "/api/games/reduceQuantity/100"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/reduceQuantity/100"
}
```
___

### DELETE /api/games/{gameId}

Delete a game's details (Soft Delete).

#### **Parameters**

|   Name    | Method | Required |   Type  |                         Description                             |
| ---------:|:------:|:--------:|:-------:| ----------------------------------------------------------------|
|  `gameId` | `Path` |    Yes   | integer |       Id of the game to delete. <br/> Example values: `1`.      |

#### **Responses**

```
# Successful deletion of a Game

{
  "id": 31
}
	
# Insufficient Stock

{
  "timestamp": "2025-03-19T16:00:25.608396700Z",
  "status": 400,
  "error": "Insufficient Stock",
  "message": "Not enough stock",
  "path": "/api/games/reduceQuantity/1"
}

# Unauthorized Access

{
  "timestamp": "2025-03-19T16:49:03.903+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/games/14"
}
	
# Forbidden Access

{
  "timestamp": "2025-03-19T16:52:47.051+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/games/14"
}
	
# No Game found with the provided Id

{
  "timestamp": "2025-03-19T16:53:53.110536500Z",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with the id: 100",
  "path": "/api/games/100"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/games/100"
}
```