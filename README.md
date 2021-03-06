# services-in-one

All of NCL micro-services running under the same JVM.

![Build Status](https://github.com/nus-ncl/services-in-one/actions/workflows/gradle.yml/badge.svg)
[![Coverage Status][cs-img]][cs-lnk]
[![Quality Gate][qg-img]][qg-lnk]

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

[cs-img]: https://coveralls.io/repos/github/nus-ncl/services-in-one/badge.svg?branch=master
[cs-lnk]: https://coveralls.io/github/nus-ncl/services-in-one?branch=master
[qg-img]: https://sonarcloud.io/api/project_badges/measure?project=nus-ncl_services-in-one&metric=alert_status
[qg-lnk]: https://sonarcloud.io/dashboard?id=nus-ncl_services-in-one
