(ns game-challenge.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [game-challenge.util :as util]))

(defn find-in-path
  "Searches for git executable in $PATH"
  [executable]
  (let [paths (string/split (System/getenv "PATH") #":")
        candidates (map #(io/file % executable) paths)]
    (first (filter #(.exists %) candidates))))

(def ^:dynamic *git* (.getAbsolutePath (find-in-path "git")))
(def ^:dynamic *mvn* (.getAbsolutePath (find-in-path "mvn")))

(def ^:dynamic *build-dir* (util/mk-tmp-dir!))

(defn- git
  [command & args]
  (apply shell/sh (cons *git* (cons command args))))

(defn- maven
  [command & args]
  (apply shell/sh (cons *mvn* (cons command args))))

(defn project-build-dir
  [project]
  (let [name (:name project)]
    (.getAbsolutePath (io/file *build-dir* name))))

(defn clone-project!
  [project]
  (let [repo (:repository project)
        target (project-build-dir project)]
    (git "clone" repo target)))

(defn maven-build!
  [project]
  (let [build-dir (project-build-dir project)]
    (shell/with-sh-dir build-dir
      (maven "package"))))
