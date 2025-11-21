# TransactionsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createTransaction**](TransactionsApi.md#createTransaction) | **POST** /api/v1/merchants/{merchantId}/transactions | Create new transaction |
| [**getTransactions**](TransactionsApi.md#getTransactions) | **GET** /api/v1/merchants/{merchantId}/transactions | Get merchant transactions |


<a id="createTransaction"></a>
# **createTransaction**
> Map&lt;String, Object&gt; createTransaction(merchantId, transactionMaster)

Create new transaction

Creates a new transaction for a merchant. TODO: Add validation, error handling, and business logic.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TransactionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    TransactionsApi apiInstance = new TransactionsApi(defaultClient);
    String merchantId = "merchantId_example"; // String | 
    TransactionMaster transactionMaster = new TransactionMaster(); // TransactionMaster | 
    try {
      Map<String, Object> result = apiInstance.createTransaction(merchantId, transactionMaster);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TransactionsApi#createTransaction");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **merchantId** | **String**|  | |
| **transactionMaster** | [**TransactionMaster**](TransactionMaster.md)|  | |

### Return type

**Map&lt;String, Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | createTransaction 200 response |  -  |

<a id="getTransactions"></a>
# **getTransactions**
> Map&lt;String, Object&gt; getTransactions(merchantId, page, size, startDate, endDate, status)

Get merchant transactions

Returns paginated list of transactions for a merchant. TODO: Implement proper pagination, filtering, and database queries.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TransactionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    TransactionsApi apiInstance = new TransactionsApi(defaultClient);
    String merchantId = "merchantId_example"; // String | 
    Integer page = 56; // Integer | 
    Integer size = 56; // Integer | 
    String startDate = "startDate_example"; // String | 
    String endDate = "endDate_example"; // String | 
    String status = "status_example"; // String | 
    try {
      Map<String, Object> result = apiInstance.getTransactions(merchantId, page, size, startDate, endDate, status);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TransactionsApi#getTransactions");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **merchantId** | **String**|  | |
| **page** | **Integer**|  | [optional] |
| **size** | **Integer**|  | [optional] |
| **startDate** | **String**|  | [optional] |
| **endDate** | **String**|  | [optional] |
| **status** | **String**|  | [optional] |

### Return type

**Map&lt;String, Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | getTransactions 200 response |  -  |

