LetGo's Twitter application
              by Alejandro Rivera

=================================

To execute in Dev mode:
```
$ ./activator run
```

To execute in Prod mode:
```
$ ./activator testProd
```

Once the application is running, query the service via HTTP request, like so:
```
$ curl "http://localhost:9000/twitter/users/letgo/tweets?max_tweets=5"
```