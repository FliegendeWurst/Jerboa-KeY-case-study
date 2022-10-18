# jerboa-modeler-editor

## Build the code :

```console
mvn clean package -P release
```

## Run the code :

```console
java -jar ./target/jerboa-modeler-editor-{YOUR_VERSION}-SNAPSHOT-jar-with-dependencies.jar
```

## Re-gen cup code :


```console
mvn clean package -U -P cup
```