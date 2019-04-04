[![CircleCI](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master.svg?style=shield)](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master)
# ToxiproxyFrontend
ToxiproxyFrontend is an Angular 7 application that is designed to make creating proxies and toxics easier. It is used in conjunction with 
[Shopify's Toxiproxy](https://github.com/Shopify/toxiproxy) to make it easier to test varying network conditions.

## Contributions

#### Code Coverage
We place heavy emphasis on code that is clean, tested, and maintainable. The start of the project boasts 100% test coverage and the build will fail
if the coverage decreases. 

#### Style
Being an Angular project, the [Angular recommended style](https://angular.io/guide/styleguide) should be followed.


## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Building
### Development

Run `ng build` to build for development purposes. 

### Deployed Environments

Run `npm run build -- --prod` for deployed environments. You'll need an environment variable set called `toxiproxyHost` for the build process to inject that so the appropriate
toxiproxy server is configured. Otherwise it will default to `http://localhost:8474`. 

Example:
`toxiproxyHost=http://toxiproxy.domain.tld:8474 npm run build -- --prod`

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

Run `ng test --code-coverage` to view code coverage. 


## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
