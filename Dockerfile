FROM catface996/centos7-with-tools:v2
VOLUME /tmp
WORKDIR ./
COPY /target/spring-cloud-gateway-istio-0.0.1-SNAPSHOT.jar /root/app/example-app.jar

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN alias ll="ls -al"

ENV JAVA_HOME /usr/local/java
ENV PATH $PATH:$JAVA_HOME/bin
ENV CLASSPATH .:$JAVA_HOME/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV JAVA_OPTS "-Xmx512M -Xms512M"
ENV SPRING_OPTS "--spring.profiles.active=local"
CMD ["cd /root/app/"]
ENTRYPOINT ["sh","-c","java -Dfile.encoding=UTF-8 ${JAVA_OPTS} -jar /root/app/example-app.jar ${SPRING_OPTS}"]

