#Cinema challenge

## Stack
* Kotlin.
* Ktor.
* Koin.
* DynamoDB.

##Run app:
```
./gradlew clean build
./gradlew installDist
docker-compose build
API_KEY=[your_IMDb_api_key] docker-compose up
```

##Create a daily showtime
To show a movie in the cinema's billboard, you should create a daily showtime
```
curl -X PUT \
  http://localhost:8080/movies/tt0232500/showtime/friday \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a9eda77c-8744-74d1-7ce3-0f60f3e7492f' \
  -d '{
    "showTimeRequests": [
    { 
        "startAt":"10:00:00",
      "price": "56"
        
    },
    { 
        "startAt":"16:00:00",
      "price": "56"
        
    }
    ]
}'
```

##Swagger UI
```
http://localhost:8080/
```

## Cinema endpoints 

### Administration
* Endpoint */movies* lets you list available movies to project.
* Endpoint */movies/{id}/showtime/{dayOfWeek}* lets you configure movie projection by day of week. 

### Cinema
* Endpoint */billboard* lets customer list movies in billboard.
* Endpoint */movies/{id}* lets customer list movie details.
* Endpoint */movies/{id}/times* lets customer list movie projections from week.
* Endpoint */movies/{id}/rate* lets customer rate a movie.

# Pending
* To add security layer for backoffice endpoints.
* Fetch user movie rate.