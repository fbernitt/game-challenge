(ns game-challenge.core-test
  (:require [clojure.test :refer :all ]
            [game-challenge.core :refer :all ]
            [clojure.java.io :as io]))


(deftest test-find-git-in-path
  (testing "that path is searched for git"
    (is (= (io/file "/usr/bin/git") (find-git-in-path)))))


(deftest test-clone-project-repo
  (testing "that a repository is successfully cloned"
    (is (= 0 (:exit (clone-project! {:repository "https://github.com/TheCodEx/game-challenge.git" :name "game-challenge"}))))))
