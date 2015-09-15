(defproject occupy-pub "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0-alpha3"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [environ "1.0.0"]
                 [garden "1.2.5"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.4"]
                 [bouncer "0.3.3"]

                 [clj-jpa "0.1.0"]
                 [org.bouncycastle/bcprov-jdk15on "1.52"]
                 [org.bouncycastle/bcpkix-jdk15on "1.52"]
                 [org.eclipse.persistence/org.eclipse.persistence.jpa "2.5.2"]
                 [org.jboss.weld.se/weld-se "2.2.14.Final"]]
  :plugins [[lein-ring "0.9.6"]
            [lein-environ "1.0.0"]]
  :java-source-paths ["src/java"]
  :source-paths ["src/clj"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :ring {:handler occupy-pub.handler/app
         :init occupy-pub.handler/init
         :destroy occupy-pub.handler/destroy}
  :pom-plugins [[org.apache.maven.plugins/maven-compiler-plugin "3.3"
                                  {:configuration ([:source "1.7"] [:target "1.7"])}]]
  :profiles {:dev {:test-paths ["test/java"]
                   :dependencies [[com.h2database/h2 "1.4.188"]
                                  [junit "4.12"]]}})
