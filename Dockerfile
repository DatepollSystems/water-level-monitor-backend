FROM openjdk:11.0.8-slim-buster
ADD build/libs/water-level-monitor-0.0.1.jar water-level-monitor.jar
ENTRYPOINT ["java", "-jar", "water-level-monitor.jar", "-Djava.security.egd=file:/dev/urandom"]