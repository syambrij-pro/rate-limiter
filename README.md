### General Details:

I created this project way back in 2019/2020 to learn rate limiting before implementing an actual enterprise project which was very complex.
Committed here in 2023 for my reference.

### Implementation Details:

1. The nature of this project is a web application. First limit needs to be configured 
   via a post call and then we can call throttling end point to check if that request is eligible to 
   be forwarded/throttled (With HTTP 429 standard).

2. The implementation classes of rate limiter like SlidingLogLimiter, SlidingWindowLimiter can be simply used in any filter/interceptor as well if needed.

### Technical details:

There are total 3 algorithms are implemented for rate limiting.

1. Token bucket Algorithm.
2. Sliding Log Algorithm.
3. Sliding Window Algorithm.

Each implementation is limited to single server as it is done in java only. But with clear concepts and ease of 
of implementation we can simply use Hazelcast or any other distributed map for our storage.

Implementation like sliding log algorithm can be extended with REDIS sorted set easily.

### Installation: 

##### Approach 1:
1. Go into directory of installed project. Run command to build the complete project.

```shell
mvn clean install 
```
It will take some time given there are test cases dependent on time intervals and thread's sleep method.
2. Run command
```shell
java -jar rate-limiter-0.0.1.jar
``` 

##### Approach 2: Unzip and then import project in IDE and run RateLimiterApplication class as main class.




### Running/Usage:

1. Either way, once installation is done execute below post request for limit configuration-

EndPoint: localhost:8080/throttle/limit/configure
POST JSON BODY:

```json
{
    "requestUrl" : "/premium/product/",
    "userId"  : "user1",
    "limitPerMinute" : 100
}
```

2. Now for each request to that endpoint we can use this endpoint to rateLimit.

Endpoint: localhost:8080/throttle/checkLimit
POST JSON BODY:

```json
{
    "requestUrl" : "/premium/product/",
    "userId"  : "user1",
    "timeout" : 100
}
```
