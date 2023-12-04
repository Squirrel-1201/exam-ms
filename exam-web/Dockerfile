FROM 172.17.1.213/library/centos7-jre8
MAINTAINER exam
ARG JAR_FILE
COPY ${JAR_FILE} /exam/exam-web.jar
WORKDIR /exam

ENV LC_ALL=zh_CN.utf8
ENV LANG=zh_CN.utf8
ENV LANGUAGE=zh_CN.utf8
RUN localedef -c -f UTF-8 -i zh_CN zh_CN.utf8

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java", "-jar", "exam-web.jar"]

