FROM navikt/java:12
COPY target/pam-xmlstilling-admin-*-jar-with-dependencies.jar /app/app.jar
EXPOSE 9024
ENV JAVA_OPTS="-Xms768m -Xmx1024m"
