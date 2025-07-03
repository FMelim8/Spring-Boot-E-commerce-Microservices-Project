# API endpoints

These are the endpoints present in the Game Catalog Service.

## GET
`your-host` [/api/order/{orderId}](#get-apiorderorderid)  | Access: `Admin`, `User` <br/>
`your-host` [/api/order/currentUser](#get-apiordercurrentuser) | Access: `User` <br/>
`your-host` [/api/order/all](#get-apiorderall) | Access: `Admin` <br/>

## POST
`your-host` [/api/order/placeOrder](#post-apiorderplaceorder) | Access: `User` <br/>

___
### GET /api/order/{orderId}

Get an order by its Id.

#### **Parameters**

|   Name  | Method | Required |  Type   |                    Description                    |
| -------:|:------:|:--------:|:-------:| ------------------------------------------------- |
|`orderId`| `Path` |    Yes   | integer |The id of the game. <br/> Example values: `1`, `2`.|

#### **Responses**

```
# Obtained and order by its Id successfully

{
  "orderId": 402,
  "orderDate": "2025-03-12T16:59:06.123737Z",
  "status": "PLACED",
  "totalAmount": 179.97,
  "gameDetails": {
    "gameId": 7,
    "title": "Hogwarts Legacy",
    "price": 59.99,
    "quantity": 3
  },
  "paymentDetails": {
    "paymentId": 252,
    "paymentMode": "CASH",
    "paymentDate": "2025-03-12T16:59:06.742359Z",
    "status": "SUCCESS"
  },
  "userDetails": {
    "userId": 2,
    "username": "user1",
    "email": "user1@email.com"
  }
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T11:26:05.272+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/order/1"
}

# Forbidden Access

{
  "timestamp": "2025-03-20T12:45:04.747+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/order/1"
}

# No Order with the provided Id

{
  "timestamp": "2025-03-20T12:50:39.340709300Z",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found with the id: 100",
  "path": "/api/order/100"
}
    
# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/order/100"
}
	
# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service GAME-CATALOG-SERVICE is unavailable",
  "path": "/api/order/100"
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

### GET /api/order/currentUser

Get a paginated result of the current user's orders.

#### **Parameters**

|    Name   | Method | Required |  Type   | Default Value |                                                      Description                                                        |
| ---------:|:------:|:--------:|:-------:|:-------------:| ----------------------------------------------------------------------------------------------------------------------- |
|   `page`  |`Query` |    No    | integer |     `1`       |                          The page of the search (Starts from 1). <br/> Example values: `1`, `4`.                        |
|   `size`  |`Query` |    No    | integer |     `10`      |                          The size of page of the search. <br/> Example values: `6`, `10`.                               |
|`sortField`|`Query` |    No    | string  |   `ORDERID`   |The field to sort by. <br/> Available values: `ORDERID`, `GAMEID`, `USERID`,<br/> `ORDERDATE`, `QUANTITY`, `TOTALAMOUNT`.|
|`direction`|`Query` |    No    | string  |     `ASC`     |                          The direction of the sort. <br/> Available values: `Asc`, `DESC`.                              |

#### **Responses**

```	
# Successfully obtain current User's Orders

{
  "content": [
    {
      "orderId": 2,
      "orderDate": "2025-06-18T13:49:01.465294Z",
      "status": "SUCCESS",
      "totalAmount": 279.96,
      "gameDetails": {
        "id": 3,
        "title": "The Legend of Zelda: Tears of the Kingdom",
        "price": 69.99
      },
      "paymentDetails": {
        "paymentId": 2,
        "paymentMode": "DEBIT_CARD",
        "paymentDate": "2025-06-18T13:49:01.469546Z",
        "status": "SUCCESS"
      },
      "userDetails": {
        "userId": 3,
        "username": "user2",
        "email": "user2@email.com"
      },
      "quantity": 4
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "page": 1,
  "empty": false
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T11:28:54.737+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/order/currentUser"
}

# Forbidden Access

{
  "timestamp": "2025-03-20T11:45:36.885+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/order/currentUser"
}

# Too Many Requests

{
  "timestamp": "2025-04-09T14:23:15.239281200Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Service Temporarily Unavailable",
  "path": "/api/order/currentUser"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/order/currentUser"
}

# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service GAME-CATALOG-SERVICE is unavailable",
  "path": "/api/order/currentUser"
}
```
___

### GET /api/order/all

Get a paginated result of all orders.

#### **Parameters**

|    Name   | Method | Required |  Type   | Default Value |                                                      Description                                                        |
| ---------:|:------:|:--------:|:-------:|:-------------:| ----------------------------------------------------------------------------------------------------------------------- |
|   `page`  |`Query` |    No    | integer |     `1`       |                          The page of the search (Starts from 1). <br/> Example values: `1`, `4`.                        |
|   `size`  |`Query` |    No    | integer |     `10`      |                          The size of page of the search. <br/> Example values: `6`, `10`.                               |
|`sortField`|`Query` |    No    | string  |   `ORDERID`   |The field to sort by. <br/> Available values: `ORDERID`, `GAMEID`, `USERID`,<br/> `ORDERDATE`, `QUANTITY`, `TOTALAMOUNT`.|
|`direction`|`Query` |    No    | string  |     `ASC`     |                          The direction of the sort. <br/> Available values: `Asc`, `DESC`.                              |

#### **Responses**

```	
# Successfully obtain current User's Orders

{
  "content": [
    {
      "orderId": 2,
      "orderDate": "2025-06-18T13:49:01.465294Z",
      "status": "SUCCESS",
      "totalAmount": 279.96,
      "gameDetails": {
        "id": 3,
        "title": "The Legend of Zelda: Tears of the Kingdom",
        "price": 69.99
      },
      "paymentDetails": {
        "paymentId": 2,
        "paymentMode": "DEBIT_CARD",
        "paymentDate": "2025-06-18T13:49:01.469546Z",
        "status": "SUCCESS"
      },
      "userDetails": {
        "userId": 3,
        "username": "user2",
        "email": "user2@email.com"
      },
      "quantity": 4
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "page": 1,
  "empty": false
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T11:28:54.737+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/order/currentUser"
}

# Forbidden Access

{
  "timestamp": "2025-03-20T11:45:36.885+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/order/currentUser"
}

# Too Many Requests

{
  "timestamp": "2025-04-09T14:23:15.239281200Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Service Temporarily Unavailable",
  "path": "/api/order/currentUser"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/order/currentUser"
}

# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service GAME-CATALOG-SERVICE is unavailable",
  "path": "/api/order/currentUser"
}
```
___

### POST /api/order/placeOrder

Create an order for a game.

#### **Parameters**

|     Name    | Method | Required |  Type   |                                                  Description                                                 |
| -----------:|:------:|:--------:|:-------:| ------------------------------------------------------------------------------------------------------------ |
|  `gameId`   | `Body` |    Yes   | integer |                          The id of the game to buy. <br/> Example values: `1`, `14`.                         |
| `quantity`  | `Body` |    Yes   | integer |                          The quantity of the game to buy. <br/> Example values: `1`, `2`.                    |
|`paymentMode`| `Body` |    Yes   | string  | The payment method. <br/> Available values: `CASH`, `PAYPAL`,<br/> `DEBIT_CARD`, `CREDIT_CARD`, `APPLE_PAY`. |

#### **Responses**

```	
# Successfully buy a game

{
  "id": 3855,
  "gameId": 6,
  "quantity": 7,
  "status": "CREATED",
  "totalAmount": 489.93
}

# Bad Request

{
  "timestamp": "2025-03-20T12:43:45.542809800Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid Request",
  "path": "/api/order/placeorder"
}

# Unauthorized Access

{
  "timestamp": "2025-03-20T11:23:42.153+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/order/placeorder"
}

# Forbidden Access

{
  "timestamp": "2025-03-20T11:45:36.885+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/order/placeorder"
}

# No Game found with the provided Game Id

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with the id: 100",
  "path": "/api/order/placeorder"
}

# Too Many Requests

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Service Temporarily Unavailable",
  "path": "/api/order/placeorder"
}

# Internal Server Error

{
  "timestamp": "2025-03-20T11:49:18.422482600Z",
  "status": 500,
  "error": "Internal Serval Error",
  "message": "message",
  "path": "/api/order/placeorder"
}

# Service Unavailable

{
  "timestamp": "2025-03-20T17:17:06.597301900Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service GAME-CATALOG-SERVICE is unavailable",
  "path": "/api/order/placeorder"
}
```