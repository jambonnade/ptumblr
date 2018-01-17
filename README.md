# ptumblr

A WebApp to browse your tumblr dashboard in a paginated way. It also saves your position.

To be used in replacement of the official tumblr dashboard when you scroll a lot : it takes a lot of resources and becomes unusable at some point. Note that reimplementing the dashboard is against the [API terms](https://www.tumblr.com/docs/en/api_agreement).

This WebApp is linked to a tumblr app, so you'll need to [register an app](https://www.tumblr.com/oauth/apps).

## How to compile

You'll have to activate one or more Maven profiles during build depending on the case and the database type you choose. The existing profiles are :
* **dev** : adds Springboot development utilities
* **h2** : adds H2 embedded database support
* **mysql** : adds mysql support

Example of build command :

	mvn clean package -P dev,h2

## How to run

You'll need to pass configuration using [the various ways allowed by Springboot](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/htmlsingle/#boot-features-external-config), at least to provide your tumblr API keys. Check `src/main/resources/config/application.properties` to see what *can* be configured and what *needs* to be configured.

Some predefined **application.properties** files are embedded in the app and can be activated (in addition to the base application.properties) using Springboot's profile system (`spring.profiles.active` configuration).

### Run manually

Here's an example to launch the app in a persistent way using H2 database (app.varDir is where the app can write files like the database) :

	java -jar target/ptumblr-1.0.1-dev-h2.jar  --spring.profiles.active=h2,dev --app.varDir="$(pwd)/ptumblr-var" --tumblr.api.consumerKey=your-key --tumblr.api.consumerSecret=your-key-secret

Check the app at [http://localhost:8080/](http://localhost:8080/) .

### With MySQL

A Docker stack example with MySQL is given in `docker/prod-stack`. It also shows how to add a reverse proxy to use a different base URL.

This stack gives configuration to Springboot through an **application.properties** file. Copy `application.properties.sample` to `application.properties` and edit it at least to provide tumblr keys.

This stack stores data in directories mounted in the host system : `mysql-var` and `ptumblr-var`, use `make-var-dirs.sh` to create them.

Run the stack with `docker stack deploy -c docker-compose.yml ptumblr`.
