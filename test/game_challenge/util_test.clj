(ns game-challenge.util-test
  (:require [clojure.test :refer :all ]
            [game-challenge.util :refer :all ]))

(deftest test-mk-tmp-dir!
  (testing "that tempdir gets created"
    (is (= true (.exists (mk-tmp-dir!))))))
