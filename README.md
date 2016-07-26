# services-in-one

All of NCL micro-services running under the same JVM.

[![Build Status][ci-img]][ci-lnk] [![Coverage Status][co-img]][co-lnk]

# Running the services

With Gradle, it is possible to run all services together in one process or just individual services. All services projects are named `services-*` where `*` is the name of the service.

## Running all services in one process

1. Use a console and enter the root of the project
2. Type `./gradlew bootRun`
3. The application should start up with all services running

## Running individual services

1. Use a console and enter the root of the project
2. Type `./gradlew :service-<name>:bootRun` (where `<name>` is the name of the service)
3. The application should start up with the selected service

[ci-img]: https://travis-ci.org/nus-ncl/services-in-one.svg?branch=master
[ci-lnk]: https://travis-ci.org/nus-ncl/services-in-one
[co-img]: https://coveralls.io/repos/github/nus-ncl/services-in-one/badge.svg?branch=master
[co-lnk]: https://coveralls.io/github/nus-ncl/services-in-one?branch=master
