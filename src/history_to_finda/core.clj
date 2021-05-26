(ns history-to-finda.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.java.jdbc :as j])
  (:gen-class))

(def queries
  {:all_entries (str "select distinct u.title, u.url"
                     " from urls u"
                     " inner join visits v on u.urlid = v.urlid"
                     " order by v.visit_time desc")})

(defn expanduser
  [^String s]
  (s/replace s #"^~" (System/getProperty "user.home")))

;; @note If you have multiple profiles we DON'T CARE! Just kidding, we do care
;; but supporting that is not high priority.
(def base-path (expanduser "~/Library/Application Support/Google/Chrome/Default/databases/"))

(comment
  (-> base-path
      (io/as-file)
      (.exists)))

(defn get-db-filepath
  []
  (-> base-path
      (io/as-file)
      (.list)
      (->> (filter #(s/starts-with? % "chrome-extension_pnmchffiealhkdloeffcdnbgdnedheme"))
           (first))
      (as-> $ (when $
                (let [ext-path (str base-path $)
                      file-name (-> ext-path (io/as-file) (.list) (first))
                      file-path (str ext-path "/" file-name)]
                  file-path)))))

(defn get-db []
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (get-db-filepath)
   :mode "ro"})

(comment (get-db))

(comment
  (let [db (get-db)]
    (j/query db [(str (:all_entries queries) " limit 10")])))

(defn print-help
  []
  (println "Try `populate`."))

(defn to-finda
  [x]
  {:label (str (:title x) " " (:url x)) ;; This is the same format finda normally uses for browser hist
   :command (str "open '" (:url x) "'")})

(defn -main
  "I don't do a whole lot ... yet."
  ([] (print-help))
  ([command & args]
   (case command
     "populate"
     (let [db (get-db)
           xs (j/query db [(str (:all_entries queries) #_" limit 10")])]
       (-> (map to-finda xs)
           (json/write-str :escape-unicode false :escape-slash false)
           (->> (spit (expanduser "~/bin/dotfiles/finda/external_data/augmented_browser_history.json"))))
       (println "Populated" (count xs) "results."))

     (do (println command "command not supported.")
         (print-help)))))

(comment
  (-main "populate"))
