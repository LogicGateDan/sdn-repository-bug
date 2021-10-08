To run the tests, you'll need a local Neo4j instance available, and you'll need to configure the application to properly
connect to the database. The `application.yml` file is set up to populate the database configuration values from 
environment variables, but you can also edit the `application.yml` file directly if that seems easier.

If you choose to use environment variables, the following list shows what this might look like in an `.env` file. An 
`.env.default` is available to copy to make this set up as straightforward as possible.

- NEO4J_DATABASE=neo4j
- NEO4J_PASSWORD=<some password>
- NEO4J_USERNAME=neo4j
- NEO4J_URL=bolt://localhost:7687

Once your database configuration is complete you can run:

`./gradlew test`
