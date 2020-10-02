FROM openjdk:11.0.8
ADD build/libs/water-level-monitor-0.0.1-SNAPSHOT.jar water-level-monitor.jar
RUN apt-get -q update && apt-get -qy install netcat
ADD https://raw.githubusercontent.com/capripot/wait-for/master/wait-for wait-for.sh
RUN chmod +x wait-for.sh
ENTRYPOINT ["java", "-jar", "water-level-monitor.jar"]