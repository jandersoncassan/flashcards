gradle clean
gradle bootRun
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080 -jar build/libs/reactive-flashcard-1.0.0.jar
#java -jar build/libs/reactive-flashcard-1.0.0.jar