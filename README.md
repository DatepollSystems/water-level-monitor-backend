# Water Level Monitor
[![coverage report](https://git-iit.fh-joanneum.at/kauerale20/water-level-monitor/badges/main/coverage.svg)](https://git-iit.fh-joanneum.at/kauerale20/water-level-monitor/-/commits/main)
[![test status](https://git-iit.fh-joanneum.at/kauerale20/water-level-monitor/badges/main/pipeline.svg)](https://git-iit.fh-joanneum.at/kauerale20/water-level-monitor/-/commits/main)
## About

Water Level Monitor is an ReST Full Api for logging of water levels.
It collects water level measurement for presenting it later on a website.
The user is able to create different rivers/lakes and related locations.
Those locations collect the actual measurements and send them to the server.
(For this demo we will skipp the collecting part and will fill the server with demo data)

### Authors

- Alexander Kauer
- Fabian Schedler

## Getting started

> To create your own Ktor-Project have a look on [ktor.io](https://ktor.io/create/)
> and [Ktor-docs](https://ktor.io/docs/intellij-idea.html).

### Prerequisites

- IDE with Kotlin support (e.g. IntelliJ IDEA)
- JDK >= 11 installed (or bundled with IDE)
- Local MongoDB running on default port (27017) and accessible without credentials (or change the dbUri in the configs)

### First steps

- Clone the repository
- Do a gradle sync
- Start the server
  - With the IntelliJ IDEA you can use the preconfigured `Server` run configuration
  - You can also start the server with the command `./gradlew run`
- Navigate to [http://localhost:8080/api](http://localhost:8080/api) - You should
  see `Running WaterLevelMonitor-API! ( ͡° ͜ʖ ͡°)`

### Configuration

The project comes preconfigured for **DEVELOPMENT**. You can change the default configuration with environment
variables.
For available options have a look at [.env.sample](.env.sample) and [Config.kt](src/main/kotlin/at/fhj/core/Config.kt)

> **!!!ATTENTION!!!**  
> The default configurations are not for use in production environments!
> Provide custom config for your production environment and also make sure to set the `ENVIRONMENT` variable
> to `production`.

## Technology

We will use [Kotlin](https://kotlinlang.org/) as language and [Ktor](https://ktor.io/) as server framework.
Furthermore, we will use [MongoDB](https://www.mongodb.com/) for storing all our data.

You can find a short introduction into the technology in the provided [slides](Slides.pdf).

### Tech stack:

- [Kotlin](https://kotlinlang.org/)
- [Ktor](https://ktor.io/)
  - Ktor Auth
  - Ktor ContentNegotiation with [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [MongoDB](https://www.mongodb.com/) with [Katerbase](https://github.com/studoverse/katerbase) driver
- JSON as content format

### Features:

- JWT based authentication
- ReST Full API
- Mongo Database
- automated tests with Gitlab CI/CD

## API Docu

For Ktor at current state it is not so simple to automatically generate OpenAPI documentation from code directly. This
is also due to the frameworks flexibility which does not force you to use a specific design, structure or architecture.
Nevertheless, we created a Postman collection which can be imported in Postman. From there it can be used to test or
play with some endpoints and also to view the documentation.

- [Postman import collection](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-postman-data)
- [Postman view collection documentation](https://learning.postman.com/docs/publishing-your-api/documenting-your-api/#viewing-schema-documentation)
- [Postman collection file](postman_collection.json)