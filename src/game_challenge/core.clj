(ns game-challenge.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [cemerick.pomegranate.aether :as aether]
            [cemerick.pomegranate :as pomegranate]
            [pedantic.core :as pedantic]))

(defn find-git-in-path
  []
  (let [paths (string/split (System/getenv "PATH") #":")
        candidates (map #(io/file % "git") paths)]
    (first (filter #(.exists %) candidates))))

(def ^:dynamic *git* (find-git-in-path))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
4