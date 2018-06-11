# LiveCycle REST Client Component (AEM 6.2)

currently only supports HTTP(S) GET, POST, PUT, DELETE requests with optional Basic Authentication

## Deployment

build and package this component with [Maven][mvn]

```sh
$ mvn clean package
```

and install the component archive ```lc-rest-client-<VERSION>.jar``` with the Workbench
as described in the [Programming with LiveCycle ES4][lc] documentation.

## Usage

![screenshot](https://github.com/dgf/lc-rest-client/raw/master/WorkbenchRestClient62.png)

[mvn]: http://maven.apache.org
[lc]: http://help.adobe.com/en_US/livecycle/11.0/ProgramLC/index.html
