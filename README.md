## Run STUB Pact Server:

Roda com o contrato gerado na pasta /target/pacts

```
docker run -t -p 8080:8080 -v "$(pwd)/target/pacts/:/app/pacts" pactfoundation/pact-stub-server -p 8080 -d pacts
```

## Para publicar o contrato no Provider:

```
mvn clean test pact:publish pact:verify
```
