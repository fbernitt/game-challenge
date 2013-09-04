(ns game-challenge.config-test
  (:require [clojure.test :refer :all ]
            [game-challenge.config :refer :all ]))

(deftest test-parse-config
  (testing "that config is parsed as EDN"
    (is (= {:foo "bar"} (parse-config "{ :foo \"bar\" }")))
    (is (= 'some (parse-config "some text")))))

(deftest test-validate-config
  (testing "that config data structure is validated for required parts"
    (is (thrown-with-msg? RuntimeException #"At least one project required!" (validate-config {})))
    (is (thrown-with-msg? RuntimeException #"At least one project required!" (validate-config {:foo "bar"})))
    (is (thrown-with-msg? RuntimeException #"At least one project required!" (validate-config {:projects []})))
    (is (thrown-with-msg? RuntimeException #"At least one project member required!" (validate-config {:projects [{}]})))
    (is (thrown-with-msg? RuntimeException #"At least one project member required!" (validate-config {:projects [{:members []}]})))
    (is (thrown-with-msg? RuntimeException #":repository required for project" (validate-config {:projects [{:members [{:name "tester" :email "tester@doesnotexist.com"}]}]})))
    (is (= {:projects [{:repository "foobar", :members [{:name "tester" :email "tester@doesnotexist.com"}]}]} (validate-config {:projects [{:repository "foobar", :members [{:name "tester" :email "tester@doesnotexist.com"}]}]})))
    ))

(deftest test-read-config
  "Tests load config from file"
  (is (= {:projects [{:repository "git@localhost:User/repo.git", :members [{:name "Tester", :email "test@test.de"}]}]} (read-config "test/example.edn"))))