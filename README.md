# TestCasePractices
## An Android application which fetches and displays a list of trending github repositories.
## This project demonstrates the following:
### 1. best practices for writing testable code
### 2. setting up jacoco for test coverage report
### 3. writing test cases using various mocking libraries and frameworks

## Author:
Sumit Kumar Pradhan

## Branch for build:
development

## Language:
Kotlin

## Minimum Android API level: 19

## Architecture: 
MVVM architecture has been implemented

## Dependecy Injection:
Koin has been used for dependecy injection

## Network
Used Retrofit for network calls

## Asynchronous Calls
Used RxJava for making asynchronous calls

## Cache
Realm Database has been used for caching the data fetched from API. The expiration time of cache is two hours.

## Test Cases: 
Unit Test cases for non-UI methods are covered:
ListAdapter
FetchViewModel
RxNetwork

UI test cases and Database based methods haven't been covered.

