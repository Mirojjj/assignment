# API Documentation

## Overview
This document provides details on the APIs exposed by `TransactionController`.

## Base URL
`/api/v1/merchants`

## Endpoints

### 1. Get Merchant Transactions
Retrieves a paginated list of transactions for a specific merchant.

- **URL**: `/{merchantId}/transactions`
- **Method**: `GET`
- **Path Parameters**:
    - `merchantId` (String): The unique identifier of the merchant.
- **Query Parameters**:
    - `page` (Integer, optional): Page number (0-indexed).
    - `size` (Integer, optional): Number of items per page.
    - `startDate` (String, optional): Start date for filtering (format: YYYY-MM-DD).
    - `endDate` (String, optional): End date for filtering (format: YYYY-MM-DD).
    - `status` (String, optional): Transaction status filter.
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: List of transactions.
- **Error Responses**:
    - **Code**: 400 Bad Request (Validation error)
    - **Code**: 404 Not Found (No transactions found)

### 2. Create Transaction
Creates a new transaction for a merchant.

- **URL**: `/transactions`
- **Method**: `POST`
- **Request Body**: `CreateTransactionRequestPayload`
    ```json
    {
      "merchantId": "string",
      "gpAcquirerId": 0,
      "gpIssuerId": 0,
      "amount": 0,
      "currency": "string",
      "cardType": "string",
      "cardLast4": "string",
      "authCode": "string",
      "responseCode": "string"
    }
    ```
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: Created transaction details.
- **Error Responses**:
    - **Code**: 400 Bad Request (Error inserting data)

### 3. Get All Merchants
Retrieves a list of all merchants.

- **URL**: `/getAllMerchants`
- **Method**: `GET`
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: List of all merchants.
- **Error Responses**:
    - **Code**: 400 Bad Request

### 4. Get Merchant Detail
Retrieves details for a specific merchant.

- **URL**: `/{merchantId}`
- **Method**: `GET`
- **Path Parameters**:
    - `merchantId` (String): The unique identifier of the merchant.
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: Merchant details.
- **Error Responses**:
    - **Code**: 400 Bad Request

### 5. Create Merchant
Creates a new merchant.

- **URL**: `/`
- **Method**: `POST`
- **Request Body**: `CreateMerchantPayload`
    ```json
    {
      "merchantName": "string",
      "merchantStatus": "string",
      "contactInfo": "string",
      "merchantCategory": "string",
      "merchantLocation": "string",
      "paymentMethod": "string",
      "merchantWebsite": "string",
      "merchantType": "string",
      "merchantTags": ["string"]
    }
    ```
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: Created merchant details.
- **Error Responses**:
    - **Code**: 400 Bad Request

### 6. Update Merchant
Updates an existing merchant.

- **URL**: `/{merchantId}`
- **Method**: `PUT`
- **Path Parameters**:
    - `merchantId` (String): The unique identifier of the merchant.
- **Request Body**: `UpdateMerchantPayload`
    ```json
    {
      "merchantName": "string",
      "merchantStatus": "string",
      "contactInfo": "string",
      "merchantCategory": "string",
      "merchantLocation": "string",
      "merchantRating": 0,
      "numOrders": 0,
      "paymentMethod": "string",
      "merchantLogo": "string",
      "merchantWebsite": "string",
      "merchantType": "string",
      "merchantTags": ["string"]
    }
    ```
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: Updated merchant details.
- **Error Responses**:
    - **Code**: 400 Bad Request

### 7. Delete Merchant
Deletes a merchant.

- **URL**: `/{merchantId}`
- **Method**: `DELETE`
- **Path Parameters**:
    - `merchantId` (String): The unique identifier of the merchant.
- **Success Response**:
    - **Code**: 200 OK
    - **Content**: Deletion confirmation.
- **Error Responses**:
    - **Code**: 400 Bad Request
