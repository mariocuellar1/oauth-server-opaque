# README

## Spring Boot OAuth 2.0 Server using MongoDb Token Store

OAuth 2.0 is the next evolution of the OAuth protocol which was originally created in late 2006. OAuth 2.0 focuses on client developer simplicity while providing specific authorization flows for web applications, desktop applications, mobile phones, and living room devices. This specification is being developed within the [IETF OAuth WG.](https://www.ietf.org/mailman/listinfo/oauth)

This project is a simple but functional OAuth2 Authorization Server implemented using Spring Boot and MongoDB as client, user and token store. 

### How to install
It's a eclipse project, just import it and run.

### Parameters & Configuration
* **application.properties**
  * **server.port**: to change the application server port
  * **spring.data.mongodb.host** : to change the database server host location, by default localhost
  * **spring.data.mongodb.port** : to change the database server port location, by default 27017
  * **spring.data.mongodb.database** : to change the database server database name, by default test
  * **spring.data.mongodb.username** : to change the database server access username
  * **spring.data.mongodb.password** : to change the database server access password
  * **myapp.accesstoken.validity** : To set the default validity (in seconds) of the access token.
  * **myapp.refreshtoken.validity** : To set the default validity (in seconds) of the refresh token.

### How to test

Remember this projects only provide Authorization Server, not resource server or consumer application.

1. Configure application (application.properties)
2. Start mongoDB if it isn't
3. Start oAuth server [rigth-clic, run  :) ]
4. Go to database and add users and client. To test using postman project attached has to add:
   * In collection app_clients:  acme/acmesecret
   ```json
    {
      "_id" : "acme",
      "client_secret" : "$2a$10$Q2509xjkOVEkw96LLuAtNeTzCZNtIRr4JR8Tc0IykmPYS//046uHa",
      "scopes" : "read,write",
      "grant_types" : "client_credentials,password,refresh_token,authorization_code"
    }
    ```
   * In collection app_users:  user1/password1
    ```json
    {
      "_id" : "user1",
      "password" : "$2a$10$wzyQPSxj5OyNMqWCKD02tOxsWhpSzBlLqIVxK1ugBrx5iGtKJufsG",
      "roles" : "ADMIN,USER"
    }
    ```
5. Import postman project *OpaqueOauthServerAndResourceTest.postman_collection.json* to postman
   * To test Grant Type: Client Credentials (client_credetials)
   ```
   Use test "Token - client_credentials" just press "Send". 
   If you change client_id/client_secret in your database, 
   you has to set new credentials in Authorization header (Basic authentication). 
   ```
   * To test Grant Type: Resource Owner Password Credentials (password)
   ```
   Use test "Token - password" just press "Send". 
   If you change client_id/client_secret in your database, 
    you have to set new credentials in Authorization header (Basic authentication).
   If you change user/password in your database,
    you have to set them in body parameters *username* and *password*
   ```
   * To test Grant Type: Authorization Code (authorization_code)
   ```
   This flow it's a little more complicated to test:
   1. Copy URL in "Autorize" test in postman and use it in a browser. Change if needed host or server port.
   2. Provide user and password.
   3. Approve scopes
   4. You get a 404 Status code, this is because we not provide a valid "redirect_uri", 
       but no problem you get also a code in URL (...login?code=<generated code>) you need that code to next step.
   5. Use "Token - code" test in postman, change body parameter code using the code get above and press "Send"
   
   To understand how this oauth flow works I recommend 
   https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2
   
   ```
All this test have to provide an access token, and some of them a refresh token.

* To test access token: Using "Token - checktoken" test in postman change body parameter *token* using the token to test in response you will get client and/or user information in token. This method will be use by Resource servers.

* To renew your token using a refresh token: Using "Token - refresh_token" test in postman change body parameter *refresh_token* using the refresh token get above.

   
And you Done !!!!

Notes:


   
   
