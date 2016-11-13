# LetGo's Twitter application
_by Alejandro Rivera (alejandro.rivera.lopez@gmailcom)_ 

------

## Quick start:

To build the project:
```
$ sbt clean jacoco:check it-jacoco:cover
```
This will execute compilation, checkstyle, findbugs, tests, integration tests and code-coverage enforcement. 

To run the application in Dev mode (while allowing for re-loading of classes):
```
$ sbt run
```

To execute in Prod mode:
```
$ sbt testProd
```

Once the application is running, query the service via HTTP request, like so:
```
$ curl "http://localhost:9000/twitter/users/letgo/tweets?max_tweets=5"
```

## Explanation

### Technologies chosen:
1. *Play Framework:*
  It's a modern JVM-friendly framework based on Akka, which is extremely fast and efficient. I've chosen this framework because 
  LetGo uses it and I'm also experienced in it. 

1. *Guava's Cache:*
  It's a component of a Google library which offers a powerful in-memory caching solution.
  I picked this to provide the caching functionality for a single JVM instance as an example, but for a more realistic solution,
  I'd have picked another distributed caching solution, perhaps something Redis.

1. *Twitter4J*
  An open source library that exposes Twitter's API for the JVM. For the purpose of this application, it's a bit of an overkill 
  since it offers a lot more functionality (async/streaming/publishing/etc.), but it seemed the right decision to avoid 
  re-inventing the wheel.
   
1. *CheckStyle, FindBugs and JaCoCo*:
  Well known Java quality checking tools. CheckStyle enforces adherence to code-style guidelines. FindBugs checks for common bugs 
  using byte-code inspection/patterns. JaCoCo enforces code coverage during execution of tests.
  All 3 of these tools have been integrated into the build system, but to execute them individually:
  ```
  $ sbt checkstyle
  $ sbt findbugs
  $ sbt jacoco:check it-jacoco:cover
  ```
  Find reports under the `/target/` folder
  
## Issues Found:

* I used the latest version of Play 2.5.9 but this became a small challenge 
  because i'm used to v2.4.X which stil relies on Play's custom `F.Promise` instead of Java 8's `CompletionStage`. 
  While both "frameworks" offer the same functionality, there are slight differences that caught me off-guard. 

* At times, Play behaves erratically and tests will fail with messges like: 
  ```
  java.lang.IllegalStateException: Attempted to call materialize() after the ActorMaterializer has been shut down.
  ```

## To-Do's:

- [ ] Improve Twitter query to retrieve exactly what's needed. 
- [ ] Improve logging (tracking/dtracing) on Web to track each request individually.
- [ ] Further configure Play to make this be a 100% backend/API project, not a Web app.
- [ ] Add monitoring/metrics to the application to track performance.
- [ ] Load test the application to fine-tune Play/Akka/Twitter4J/application settings.
- [ ] Add distributed caching.
- [ ] Enhance the configuration for the Jackson Object-Mapper.
- [ ] ...