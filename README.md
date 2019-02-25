# DEPRECATED

Download selector
=================

Static webpages for dataset downloads

Quickstart
----------
Run `lein figwheel` and go to `http://localhost:3449/brt-download-dev.html?base=http%3A%2F%2Fgeodata.nationaalgeoregister.nl%2Ftop10nlv2%2Fextract%2Fkaartbladen%2FTOP10NL_%24SHEET.zip%3Fformaat%3Dgml`
in your web browser.


Development
-----------
Run `lein figwheel` and go to a dev webpage.
- BRT: `brt-download-dev.html?base=[urlencoded download location with $SHEET replacement`

Compile with `lein compile`, which has cljsbuild hooked.

Release
-------
Run `lein with-profile release compile`. [WIP]
