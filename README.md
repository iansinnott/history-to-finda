# history-to-finda

If you use the Chrome History Unlimited browser plugin you will have a database on your computer containing all your browser history. ALL OF IT. All browsers I know of (Chrome, FF, etc) limit the amount of browser history to roughly 3 months. This is a travesty, because often the sites you most want to visit are ones you've already visited at some point in the past.

The aforementioned browsser plugin stores all your history in a sqlite database. This is great, but how do you search that history? You can search it via the extension's built-in UI, but I wanted something not tied to the browser at all.

This simple Clojure script will grab your history from that database and dump it into a JSON file which can be read by Finda.

NB: This is thus far coded for my personal use, so there may be hardcoded paths which are not applicable on your system.

## Installation

Clone this repo.

## Usage

From within the repo, run:

```
lein install
lein run -- populate
```

You will need Leiningen installed.

## Options

* `populate`: Right now this is the only command and it will copy data from the db into json.

## License

Copyright © 2021 MIT