# FitByte

Backend application for your fitness tracking friend. Track your progress, show your growth!

Built using Java 21, Springboot, PostgreSQL, and MinIO.

## Core Feature

- Track and save users' activity into calories burned
- Multi-tenant support

## Additional Feature

- User profile management

## Getting Started
### Installation

Clone this repository
```sh
git clone https://github.com/mghazian/fitbyte.git
```

### Setting up

#### Prerequisite

`podman` and `podman-compose`.

1. Install [podman](https://podman.io/).
2. For windows user, make sure the podman machine has been started.
3. Install podman-compose (ensure Python has been installed)
   ```shell
   pip install podman-compose
   podman-compose --version # Make sure the software installed successfully
   ```
   
#### Running the backend
Open terminal on this repository's directory, then run
```shell
podman-compose up -d
```

This will launch the backend, database, and blob server. If the process run successfully, the backend can be accessed via `http://localhost:8080`.

-----

## Usage Guide

The system offers several HTTP API summarized in below table.

| Step | Feature         | Method | Endpoint | Description |
| :--- |:----------------| :--- | :--- | :--- |
| **1** | Registration    | `POST` | `/v1/register` | Create a new user account. |
| **2** | User Login      | `POST` | `/v1/login` | Authenticate and receive a Bearer Token. |
| **3** | Check Profile   | `GET` | `/v1/user` | View initial profile data (authenticated). |
| **4** | Update Profile  | `PATCH` | `/v1/user` | Set preferences, metrics, and name (authenticated). |
| **5** | Add Activity    | `POST` | `/v1/activity` | Log a new activity and calculate calories (authenticated). |
| **6** | View Activities | `GET` | `/v1/activity` | Retrieve logged activities with optional filtering (authenticated). |
| **7** | Change Activity | `PATCH` | `/v1/activity/:activityId` | Update an activity record and recalculate calories (authenticated). |
| **8** | Delete Activity | `DELETE` | `/v1/activity/:activityId` | Remove an activity record (authenticated). |

All the API endpoints are relative to the base url `http://localhost:8080`. For example, usable API for registration is `http://localhost:8080/v1/register` 

### Registration

```
POST /v1/register
```

#### Request body format

The request requires a valid email and a password between 8 and 32 characters.

```json
{
  "email": "name@name.com",
  "password": "asdfasdf" 
}
```

#### Response format

On successful registration, the system returns the user's email and an authentication token.

```json
{
  "email": "<string: email>",
  "token": "<string: token>"
}
```

#### Example

```
POST /v1/register
```

**Request**

```json
{
  "email": "new_user@fitbyte.com",
  "password": "mysecurepassword123"
}
```

**Response (201 Created)**

```json
{
  "email": "new_user@fitbyte.com",
  "token": "aW4gc2VjcmV0IHdlIHRydXN0" 
}
```

-----

### User Login

The user provides their registered email and password to receive an authentication token.

```
POST /v1/login
```

#### Request body format


```json
{
  "email": "name@name.com",
  "password": "asdfasdf" 
}
```

#### Response format

On successful login, the system returns the user's email and a token. This **token** must be used in the `Authorization: Bearer <token>` header for all subsequent authenticated API calls.

```json
{
  "email": "<string: email>",
  "token": "<string: token>"
}
```

#### Example

```
POST /v1/login
```

**Request**

```json
{
  "email": "existing_user@fitbyte.com",
  "password": "mysecurepassword123"
}
```

**Response (200 OK)**

```json
{
  "email": "existing_user@fitbyte.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImZpdF91c2VyXzEiLCJpYXQiOjE2OTYxNjMxMDR9"
}
```

-----

Here is **Step 3: User Check Their Profile** based on the PRD. This is the first authenticated call, requiring the token obtained in Step 2.

-----

### Check User Profile

```
GET /v1/user
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

No request body is required for this endpoint.

#### Response format

Returns the user's current profile data. All fields are initially `null` until the user updates them.

```json
{
  "preference": "", // null when empty
  "weightUnit": "", // null when empty
  "heightUnit": "", // null when empty
  "weight": 1, // null when empty
  "height": 2, // null when empty
  "email": "name@name.com",
  "name": "", // null when empty
  "imageUri": "" // null when empty
}
```

#### Example

```
GET /v1/user
```

**Header**

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImZpdF91c2VyXzEiLCJpYXQiOjE2OTYxNjMxMDR9
```

**Response (200 OK)**

```json
{
  "preference": null,
  "weightUnit": null,
  "heightUnit": null,
  "weight": null,
  "height": null,
  "email": "existing_user@fitbyte.com",
  "name": null,
  "imageUri": null
}
```

-----

### User Image Profile Upload (Optional)

```
POST /v1/file
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

The request must be sent as **Multipart Form-Data**.

| Key | Value Type | Description |
| :--- | :--- | :--- |
| `file` | `file` | JPEG/JPG/PNG file, maximum 100KiB. |

#### Response format

Returns the URI where the uploaded file is stored (e.g., in AWS S3).

```json
{
  "uri": "<string: file_uri>"
}
```

#### Example

```
POST /v1/file
```

**Header**

```
Authorization: Bearer <token>
```

**Request** (Multipart Form-Data)

```
file: [binary content of user.jpg]
```

**Response (200 OK)**

```json
{
  "uri": "http://fitbyte.com/img/profile-image-123.jpg"
}
```

-----

### User Profile Update

```
PATCH /v1/user
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

Requires fields related to fitness preferences and metrics. Note that `name` and `imageUri` are optional, but all others are **required** for the PATCH call to succeed. General use case utilizes the image URL generated from "User Image Profile Upload (Optional)", although providing other source of URL is allowed.

```json
{
  "preference": "CARDIO", // enum ['CARDIO', 'WEIGHT']
  "weightUnit": "KG", // enum ['KG', 'LBS']
  "heightUnit": "CM", // enum ['CM', 'INCH']
  "weight": 75.5, // number (min: 10, max: 1000)
  "height": 178, // number (min: 3, max: 250)
  "name": "Jane Doe", // optional
  "imageUri": "http://fitbyte.com/img/profile-image-123.jpg" // optional
}
```

#### Response format

Returns the updated user profile object.

```json
{
  "preference": "",
  "weightUnit": "",
  "heightUnit": "",
  "weight": 1,
  "height": 2,
  "email": "name@name.com",
  "name": "",
  "imageUri": ""
}
```

#### Example

```
PATCH /v1/user
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "preference": "CARDIO",
  "weightUnit": "KG",
  "heightUnit": "CM",
  "weight": 72.8,
  "height": 175,
  "name": "FitByte User",
  "imageUri": "http://fitbyte.com/img/profile-image-123.jpg"
}
```

**Response (200 OK)**

```json
{
  "preference": "CARDIO",
  "weightUnit": "KG",
  "heightUnit": "CM",
  "weight": 72.8,
  "height": 175,
  "email": "existing_user@fitbyte.com",
  "name": "FitByte User",
  "imageUri": "http://fitbyte.com/img/profile-image-123.jpg"
}
```

-----

### Adding Activities

Activity is the exercise user does, which could be walking, running, etc. 
```
POST /v1/activity
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

The user logs a completed activity, specifying the type, when it was done, and the duration.

```json
{
  "activityType": "<string: enum>", // e.g., 'RUNNING', 'WALKING'
  "doneAt": "<string: ISO Date>", // The time the activity was done
  "durationInMinutes": 1 // number (min: 1)
}
```

The activity types (and its calories conversion for reference) are as follow.

| ActivityType (case sensitive) | Calories per minute |
| :---------------------------- | :------------------ |
| Walking                       | 4                   |
| Yoga                          | 4                   |
| Stretching                    | 4                   |
| Cycling                       | 8                   |
| Swimming                      | 8                   |
| Dancing                       | 8                   |
| Hiking                        | 10                  |
| Running                       | 10                  |
| HIIT                          | 10                  |
| JumpRope                      | 10                  |


#### Response format

Returns the newly created activity record, including the calculated `caloriesBurned` and system timestamps.

```json
{
  "activityId": "<string: id>",
  "activityType": "<string>",
  "doneAt": "<string: ISO Date>",
  "durationInMinutes": 1,
  "caloriesBurned": 1, // calculated value
  "createdAt": "<string: ISO Date>",
  "updatedAt": "<string: ISO Date>"
}
```

#### Example

```
POST /v1/activity
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "activityType": "RUNNING",
  "doneAt": "2025-10-17T09:30:00Z",
  "durationInMinutes": 30
}
```

**Response (201 Created)**

```json
{
  "activityId": "a-101",
  "activityType": "RUNNING",
  "doneAt": "2025-10-17T09:30:00Z",
  "durationInMinutes": 30,
  "caloriesBurned": 300,
  "createdAt": "2025-10-17T10:15:00Z",
  "updatedAt": "2025-10-17T10:15:00Z"
}
```

-----

### See Existing Activities

```
GET /v1/activity
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request parameter format

All parameters are optional and filter the result set using `AND` logic. Default pagination is `limit=5` and `offset=0`.

| Parameter | Type | Description | Example |
| :--- | :--- | :--- | :--- |
| `limit` | `number` | Max number of records to return. | `limit=10` |
| `offset` | `number` | Number of records to skip (for pagination). | `offset=20` |
| `activityType` | `string` | Filter by activity type (e.g., RUNNING). | `activityType=RUNNING` |
| `doneAtFrom` | `string` | Activities done on or after this ISO Date. | `doneAtFrom=2025-10-01T00:00:00Z` |
| `doneAtTo` | `string` | Activities done on or before this ISO Date. | `doneAtTo=2025-10-31T23:59:59Z` |
| `caloriesBurnedMin` | `number` | Activities with calories $\ge$ this value. | `caloriesBurnedMin=100` |
| `caloriesBurnedMax` | `number` | Activities with calories $\le$ this value. | `caloriesBurnedMax=500` |

#### Response format

Returns an array of activity objects matching the applied filters and pagination.

```json
[
  {
    "activityId": "<string: id>",
    "activityType": "<string>",
    "doneAt": "<string: ISO Date>",
    "durationInMinutes": 1,
    "caloriesBurned": 1, // calculated value
    "createdAt": "<string: ISO Date>"
  }
]
```

#### Example

Retrieve 10 running activities with over 200 calories burned, starting from October 1st.

```
GET /v1/activity?limit=10&activityType=RUNNING&doneAtFrom=2025-10-01T00:00:00Z&caloriesBurnedMin=200
```

**Header**

```
Authorization: Bearer <token>
```

**Response (200 OK)**

```json
[
  {
    "activityId": "a-101",
    "activityType": "RUNNING",
    "doneAt": "2025-10-17T09:30:00Z",
    "durationInMinutes": 30,
    "caloriesBurned": 300,
    "createdAt": "2025-10-17T10:15:00Z"
  },
  {
    "activityId": "a-102",
    "activityType": "RUNNING",
    "doneAt": "2025-10-15T18:00:00Z",
    "durationInMinutes": 45,
    "caloriesBurned": 450,
    "createdAt": "2025-10-15T18:45:00Z"
  }
]
```

-----

### Change Existing Activities

```
PATCH /v1/activity/:activityId
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

**Only the fields needing updates are required.** Note that `caloriesBurned` will be **re-calculated** by the backend based on the new `activityType` and/or `durationInMinutes`.

```json
{
  "activityType": "<string: enum>", // optional
  "doneAt": "<string: ISO Date>", // optional
  "durationInMinutes": 1 // optional (min: 1)
}
```

#### Response format

Returns the updated activity object with the new `caloriesBurned` value and an updated `updatedAt` timestamp.

```json
{
  "activityId": "<string: id>",
  "activityType": "<string>",
  "doneAt": "<string: ISO Date>",
  "durationInMinutes": 1,
  "caloriesBurned": 1, // RE-CALCULATED value
  "createdAt": "<string: ISO Date>",
  "updatedAt": "<string: ISO Date>" // Should be updated to now
}
```

#### Example

```
PATCH /v1/activity/a-101
```

**Header**

```
Authorization: Bearer <token>
```

**Request**

```json
{
  "durationInMinutes": 40
}
```

**Response (200 OK)**

```json
{
  "activityId": "a-101",
  "activityType": "RUNNING",
  "doneAt": "2025-10-17T09:30:00Z",
  "durationInMinutes": 40,
  "caloriesBurned": 400,
  "createdAt": "2025-10-17T10:15:00Z",
  "updatedAt": "2025-10-17T12:00:00Z"
}
```

-----

### Delete Existing Activities

```
DELETE /v1/activity/:activityId
```

#### Header requirement

The authentication token must be provided in the `Authorization` header.

| Key | Value |
| :--- | :--- |
| `Authorization` | `Bearer <token>` |

#### Request body format

No request body is required for this endpoint.

#### Response format

Returns a `200 OK` status code upon successful deletion. No content is expected in the response body.

```
(No Content)
```

#### Example

Delete the activity with ID `a-101`.

```
DELETE /v1/activity/a-101
```

**Header**

```
Authorization: Bearer <token>
```

**Response (200 OK)**

```
(Success, activity deleted)
```

-----