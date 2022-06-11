#1
FROM openjdk:11
EXPOSE 8080
ADD target/betterreads-0.0.1-SNAPSHOT.jar betterreads-service.jar 
ENTRYPOINT ["sh","-c","java -jar betterreads-service.jar"]

#2 exploded jar - extract jar and copy the extracted data to target/dependency folder
#FROM openjdk:11
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#EXPOSE 8080
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.springboot.betterreads.BetterReadsMainApp"]
#
##3 exploded jar with multistage docker
#FROM openjdk:11 as stage1
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
##create directory
#RUN mkdir -p /app/dependency
##make /app/dependency as working directory
#WORKDIR /app/dependency
##explode jar in working directory
#RUN jar -xf ../../app.jar
#
#FROM openjdk:11 as stage2
#ARG DEPENDENCY=app/dependency
#COPY --from=stage1 ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=stage1 ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=stage1 ${DEPENDENCY}/BOOT-INF/classes /app
#EXPOSE 8080
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.springboot.betterreads.BetterReadsMainApp"]
#
##4spring boot maven plugin layers approach
#FROM openjdk:11 as builder
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#RUN java -Djarmode=layertools -jar app.jar
#
#FROM openjdk:11 as runtime
#COPY --from=builder dependencies/ ./
#COPY --from=builder snapshot-dependencies/ ./
#COPY --from=builder spring-boot-loader/ ./
#COPY --from=builder application/ ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]