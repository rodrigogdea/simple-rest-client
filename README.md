#Simple Rest Client#

Is a simple rest client wrapping apache http client.

##Installing##
```
//To be define... 
```

##Usage##

###GET###

```java
    URL url = new URL("https://somehost:3334");
    Response response = new SimpleRestClient(url, true).get("/").request();

    assertTrue(response.code() == 200);
```

###POST###

```java
        URL url = new URL("http://other:3333");
        Response response = new SimpleRestClient(url, false)
                .post("contactos")
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("{\"value\":3}")
                .request();

        String result = response.asString();
```

###PUT###

```java
        URL url = new URL("http://localhost:3333");
        Response response = new SimpleRestClient(url, false)
                .put("contactos", "23")
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("{\"value\":3}")
                .request();

        String result = response.asString();
```
###With Authentication###

```java
        URL url = new URL("http://localhost:3333");
        Response response =  new SimpleRestClient(url)
                 .withAuth("user", "pass")
                 .get("").request();

        String result = response.asString();
```

