FROM openjdk:11.0.8
ADD build/libs/water-level-monitor-0.0.1-SNAPSHOT.jar water-level-monitor-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "water-level-monitor-0.0.1-SNAPSHOT.jar"]