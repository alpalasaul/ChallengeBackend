From openjdk:11
copy ./target/argos-auth-1.jar argos-auth-1.jar
CMD ["java","-jar","argos-auth-1.jar"]
