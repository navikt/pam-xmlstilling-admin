FROM navikt/java:11
COPY target/pam-xmlstilling-admin-*-jar-with-dependencies.jar /app/app.jar
EXPOSE 9024
