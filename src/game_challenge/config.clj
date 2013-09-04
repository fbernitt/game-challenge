(ns game-challenge.config
  (:require [clojure.edn :as edn])
  (:import (clojure.lang Reflector)))

(declare throw-if)

(defn parse-config
  [str]
  (edn/read-string str))

(defn- validate-project
  [project]
  (throw-if (not (contains? project :members )) RuntimeException "At least one project member required!")
  (throw-if (empty? (:members project)) RuntimeException "At least one project member required!")
  (throw-if (not (contains? project :repository )) RuntimeException ":repository required for project")
  project)

(defn validate-config
  [cfg]
  (throw-if (not (contains? cfg :projects )) RuntimeException "At least one project required!")
  (throw-if (empty? (:projects cfg)) RuntimeException "At least one project required!")
  (doseq [project (:projects cfg)] (validate-project project))
  cfg)

(defn read-config
  [file]
  (validate-config (parse-config (slurp file))))

(declare throwable)

(defn- throw-if
  "Throws an Exception or Error if test is true. args are those documented 3 for throwf."
  [test & args]
  (when test
    (throw (throwable args))))

(defn- throwable?
  "Returns true if x is a Throwable"
  [x]
  (instance? Throwable x))

(defn- throwable
  "Constructs a Throwable with optional cause and formatted message. Its
  stack trace will begin with our caller's caller. Args are as described
  for throwf except throwable accepts them as list rather than inline."
  [args]
  (let [[arg] args
        [class & args] (if (class? arg) args (cons Exception args))
        [arg] args
        [cause & args] (if (throwable? arg) args (cons nil args))
        message (when args (apply format args))
        ctor-args (into-array Object
                    (cond (and message cause) [message cause]
                      message [message]
                      cause [cause]))
        throwable (Reflector/invokeConstructor class ctor-args)
        our-prefix "game-challenge.config$throwable"
        not-us? #(not (.startsWith (.getClassName %) our-prefix))
        raw-trace (.getStackTrace throwable)
        edited-trace (into-array StackTraceElement
                       (drop 3 (drop-while not-us? raw-trace)))]
    (.setStackTrace throwable edited-trace)
    throwable))
