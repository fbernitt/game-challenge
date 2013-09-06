(ns game-challenge.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [game-challenge.util :as util]))

(defn find-git-in-path
  "Searches for git executable in $PATH"
  []
  (let [paths (string/split (System/getenv "PATH") #":")
        candidates (map #(io/file % "git") paths)]
    (first (filter #(.exists %) candidates))))

(def ^:dynamic *git* (.getAbsolutePath (find-git-in-path)))

(def ^:dynamic *build-dir* (util/mk-tmp-dir!))

(defn git
  [command & args]
  (apply shell/sh (cons *git* (cons command args))))

(defn- project-build-dir
  [project]
  (let [name (:name project)]
    (.getAbsolutePath (io/file *build-dir* name))))

(defn clone-project!
  [project]
  (let [repo (:repository project)
        target (project-build-dir project)]
    (git "clone" repo target)))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
