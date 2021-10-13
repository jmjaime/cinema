FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/cinema/ /app/
WORKDIR /app/bin
CMD ["./cinema"]