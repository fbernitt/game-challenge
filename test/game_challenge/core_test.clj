(ns game-challenge.core-test
  (:require [clojure.test :refer :all ]
            [game-challenge.core :refer :all ]
            [clojure.java.io :as io]))


(deftest test-find-git-in-path
  (testing "that path is searched for git"
    (is (= (io/file "/usr/bin/git") (find-in-path "git")))))


(deftest test-clone-project-repo
  (testing "that a repository is successfully cloned"
    (is (= 0 (:exit (clone-project! {:repository "https://github.com/TheCodEx/game-challenge.git" :name "game-challenge"}))))))

(deftest test-maven-build!
  (testing "that a maven build gets executed"
    (let [project {:repository "https://github.com/TheCodEx/logparser.git" :name "logparser"}
          repo (clone-project! project)]
      (is (= 0 (:exit (maven-build! project)))))))
