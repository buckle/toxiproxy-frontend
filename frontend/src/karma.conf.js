// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function(config) {
  config.set({
               basePath: '',
               frameworks: ['jasmine', '@angular-devkit/build-angular'],
               plugins: [
                 require('karma-jasmine'),
                 require('karma-chrome-launcher'),
                 require('karma-jasmine-html-reporter'),
                 require('karma-coverage-istanbul-reporter'),
                 require('karma-istanbul-threshold'),
                 require('@angular-devkit/build-angular/plugins/karma')
               ],
               client: {
                 clearContext: false // leave Jasmine Spec Runner output visible in browser
               },
               istanbulThresholdReporter: {
                 dir: require('path').join(__dirname, '../coverage'),
                 src: require('path').join(__dirname, '../coverage/coverage-final.json'),
                 reports: ['html', 'lcovonly', 'json'],
                 fixWebpackSourcePaths: true,
                 thresholds: {
                   statements: 100,
                   branches: 100,
                   lines: 100,
                   functions: 100
                 }
               },
               reporters: ['progress', 'kjhtml', 'coverage-istanbul', 'istanbul-threshold'],
               port: 9876,
               colors: true,
               logLevel: config.LOG_INFO,
               autoWatch: true,
               browsers: ['Chrome'],
               customLaunchers: {
                 ChromeHeadlessCI: {
                   base: 'ChromeHeadless',
                   flags: ['--no-sandbox', '--disable-gpu']
                 }
               },
               singleRun: false
             });
};
