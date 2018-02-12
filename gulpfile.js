var gulp = require('gulp');
var jshint = require('gulp-jshint');
var freemarker = require("gulp-freemarker");
var bs = require('browser-sync').create(); // create a browser sync instance.

var  watch =  [
            './src/main/resources/static/js/app/*.js',
            './src/main/resources/static/templates/*.ftl',
            './src/main/resources/static/css/*.css',
            './target/classses/static/js/app/*.js',
            './target/classses/static/templates/*.ftl',
            './target/classses/static/css/*.css'
        ];



gulp.task('browser-sync', function() {
    bs.init({
        proxy: {
        target: "localhost:8080/Pras", // can be [virtual host, sub-directory, localhost with port]
        ws: true // enables websockets
        }
    });

    gulp.watch( watch ).on( 'change', bs.reload );

  //  gulp.watch('./src/main/resources/static/js/app/*.js').on("change", bs.reload);

  //  gulp.watch('./src/main/resources/static/templates/*.ftl').on("change", bs.reload);
});

gulp.task('ftl', function () {
gulp.src("./target/classses/static/templates/")
	.pipe(freemarker({
		viewRoot: "WEB-INF/views/",
		options: {}
	}))
	.pipe(gulp.dest("./www"));
  });

    gulp.task('scripts', function () {
        gulp.src('./src/main/resources/static/js/app/*.js')
            .pipe(jshint())
            .pipe(jshint.reporter('default'));
    });
