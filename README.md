# TrendRepo
## An Android application which fetches and displays a list of trending github repositories.

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
TrendRepoViewModel
RxNetwork

UI test cases and Database based methods haven't been covered.

