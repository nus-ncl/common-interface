# services-in-one

All NCL micro-services are running in the same process 

# Running the services

With Gradle, it is possible to run all services together in one process or just individual services. All services projects are named `services-*` where `*` is the name of the service.

## Running all services in one process

1. Use a console and enter the root of the project
2. Type `./gradlew bootRun`
3. The application should start up with all services running

## Running individualk services

1. Use a console and enter the root of the project
2. Type `./gradlew :service-<name>:bootRun` (where `<name>` is the name of the service)
3. The application should start up with the selected service
