var gulp = require('gulp');
var jshint = require('gulp-jshint');
var bs = require('browser-sync').create(); // create a browser sync instance.
var dirSync = require('gulp-directory-sync');


var  watch =  [
            './target/classes/**/*.js',
            './target/classes/**/*.ftl',
            './target/classes/static/**/*.css',
            './target/classes/**/*.class',
            './target/classes/**/*.yml',
            './target/classes/**/*.properties',
            './src/classes/**/*.yml'
        ];

gulp.task('sync', function () {
    return gulp.src('./')
            .pipe(dirSync('./src/main/resources/static/js/app', './target/classes/static/js/app', {ignore: []}, 'nodelete'))
            .pipe(dirSync('./src/main/resources/static/css', './target/classes/static/css', {ignore: []}, 'nodelete'));
});


gulp.task('browser-sync', ['sync'], function() {
    bs.init({
      reloadDelay: 5000,
        proxy: {
        target: "localhost:8080/Pras", // can be [virtual host, sub-directory, localhost with port]
        ws: true // enables websockets
        }
    });

    gulp.watch( watch ).on( 'change', bs.reload );

});
