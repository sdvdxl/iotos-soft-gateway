[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------------< com.example:mqtt >--------------------------
[INFO] Building mqtt 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-dependency-plugin:3.1.2:tree (default-cli) @ mqtt ---
Downloading from central: https://maven.aliyun.com/nexus/content/groups/public/org/hdrhistogram/HdrHistogram/2.1.9/HdrHistogram-2.1.9.jar
Downloaded from central: https://maven.aliyun.com/nexus/content/groups/public/org/hdrhistogram/HdrHistogram/2.1.9/HdrHistogram-2.1.9.jar (0 B at 0 B/s)
[INFO] com.example:mqtt:jar:0.0.1-SNAPSHOT
[INFO] +- org.springframework.boot:spring-boot-starter:jar:2.3.0.RELEASE:compile
[INFO] |  +- org.springframework.boot:spring-boot:jar:2.3.0.RELEASE:compile
[INFO] |  |  \- org.springframework:spring-context:jar:5.2.6.RELEASE:compile
[INFO] |  +- org.springframework.boot:spring-boot-autoconfigure:jar:2.3.0.RELEASE:compile
[INFO] |  +- org.springframework.boot:spring-boot-starter-logging:jar:2.3.0.RELEASE:compile
[INFO] |  |  +- ch.qos.logback:logback-classic:jar:1.2.3:compile
[INFO] |  |  |  \- ch.qos.logback:logback-core:jar:1.2.3:compile
[INFO] |  |  +- org.apache.logging.log4j:log4j-to-slf4j:jar:2.13.2:compile
[INFO] |  |  |  \- org.apache.logging.log4j:log4j-api:jar:2.13.2:compile
[INFO] |  |  \- org.slf4j:jul-to-slf4j:jar:1.7.30:compile
[INFO] |  +- jakarta.annotation:jakarta.annotation-api:jar:1.3.5:compile
[INFO] |  +- org.springframework:spring-core:jar:5.2.6.RELEASE:compile
[INFO] |  |  \- org.springframework:spring-jcl:jar:5.2.6.RELEASE:compile
[INFO] |  \- org.yaml:snakeyaml:jar:1.26:compile
[INFO] +- org.projectlombok:lombok:jar:1.18.12:compile (optional) 
[INFO] +- org.springframework.boot:spring-boot-starter-web:jar:2.3.0.RELEASE:compile
[INFO] |  +- org.springframework.boot:spring-boot-starter-json:jar:2.3.0.RELEASE:compile
[INFO] |  |  +- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.11.0:compile
[INFO] |  |  +- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.11.0:compile
[INFO] |  |  \- com.fasterxml.jackson.module:jackson-module-parameter-names:jar:2.11.0:compile
[INFO] |  +- org.springframework.boot:spring-boot-starter-tomcat:jar:2.3.0.RELEASE:compile
[INFO] |  |  +- org.apache.tomcat.embed:tomcat-embed-core:jar:9.0.35:compile
[INFO] |  |  +- org.glassfish:jakarta.el:jar:3.0.3:compile
[INFO] |  |  \- org.apache.tomcat.embed:tomcat-embed-websocket:jar:9.0.35:compile
[INFO] |  +- org.springframework:spring-web:jar:5.2.6.RELEASE:compile
[INFO] |  |  \- org.springframework:spring-beans:jar:5.2.6.RELEASE:compile
[INFO] |  \- org.springframework:spring-webmvc:jar:5.2.6.RELEASE:compile
[INFO] |     +- org.springframework:spring-aop:jar:5.2.6.RELEASE:compile
[INFO] |     \- org.springframework:spring-expression:jar:5.2.6.RELEASE:compile
[INFO] +- org.t-io:tio-core-spring-boot-starter:jar:3.6.0.v20200315-RELEASE:compile
[INFO] |  +- org.t-io:tio-core:jar:3.6.0.v20200315-RELEASE:compile
[INFO] |  |  \- org.t-io:tio-utils:jar:3.6.0.v20200315-RELEASE:compile
[INFO] |  |     +- com.alibaba:fastjson:jar:1.2.66:compile
[INFO] |  |     \- com.github.ben-manes.caffeine:caffeine:jar:2.8.2:compile
[INFO] |  \- org.t-io:tio-spring-boot-common:jar:3.6.0.v20200315-RELEASE:compile
[INFO] +- org.eclipse.paho:org.eclipse.paho.client.mqttv3:jar:1.2.0:compile
[INFO] +- org.springframework.boot:spring-boot-starter-test:jar:2.3.0.RELEASE:test
[INFO] |  +- org.springframework.boot:spring-boot-test:jar:2.3.0.RELEASE:test
[INFO] |  +- org.springframework.boot:spring-boot-test-autoconfigure:jar:2.3.0.RELEASE:test
[INFO] |  +- com.jayway.jsonpath:json-path:jar:2.4.0:test
[INFO] |  |  \- net.minidev:json-smart:jar:2.3:test
[INFO] |  |     \- net.minidev:accessors-smart:jar:1.2:test
[INFO] |  |        \- org.ow2.asm:asm:jar:5.0.4:test
[INFO] |  +- jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3:test
[INFO] |  |  \- jakarta.activation:jakarta.activation-api:jar:1.2.2:test
[INFO] |  +- org.assertj:assertj-core:jar:3.16.1:test
[INFO] |  +- org.hamcrest:hamcrest:jar:2.2:test
[INFO] |  +- org.junit.jupiter:junit-jupiter:jar:5.6.2:test
[INFO] |  |  +- org.junit.jupiter:junit-jupiter-api:jar:5.6.2:test
[INFO] |  |  |  +- org.apiguardian:apiguardian-api:jar:1.1.0:test
[INFO] |  |  |  +- org.opentest4j:opentest4j:jar:1.2.0:test
[INFO] |  |  |  \- org.junit.platform:junit-platform-commons:jar:1.6.2:test
[INFO] |  |  +- org.junit.jupiter:junit-jupiter-params:jar:5.6.2:test
[INFO] |  |  \- org.junit.jupiter:junit-jupiter-engine:jar:5.6.2:test
[INFO] |  |     \- org.junit.platform:junit-platform-engine:jar:1.6.2:test
[INFO] |  +- org.mockito:mockito-core:jar:3.3.3:test
[INFO] |  |  +- net.bytebuddy:byte-buddy:jar:1.10.10:compile
[INFO] |  |  +- net.bytebuddy:byte-buddy-agent:jar:1.10.10:test
[INFO] |  |  \- org.objenesis:objenesis:jar:2.6:compile
[INFO] |  +- org.mockito:mockito-junit-jupiter:jar:3.3.3:test
[INFO] |  +- org.skyscreamer:jsonassert:jar:1.5.0:test
[INFO] |  |  \- com.vaadin.external.google:android-json:jar:0.0.20131108.vaadin1:test
[INFO] |  +- org.springframework:spring-test:jar:5.2.6.RELEASE:test
[INFO] |  \- org.xmlunit:xmlunit-core:jar:2.7.0:test
[INFO] +- commons-codec:commons-codec:jar:1.10:compile
[INFO] +- iot.cloud.os:cloud-os-common-redis:jar:0.0.2.10-SNAPSHOT:compile
[INFO] |  +- org.slf4j:slf4j-api:jar:1.7.30:compile
[INFO] |  +- org.springframework.boot:spring-boot-starter-data-redis:jar:2.3.0.RELEASE:compile
[INFO] |  |  +- org.springframework.data:spring-data-redis:jar:2.3.0.RELEASE:compile
[INFO] |  |  |  +- org.springframework.data:spring-data-keyvalue:jar:2.3.0.RELEASE:compile
[INFO] |  |  |  +- org.springframework:spring-tx:jar:5.2.6.RELEASE:compile
[INFO] |  |  |  +- org.springframework:spring-oxm:jar:5.2.6.RELEASE:compile
[INFO] |  |  |  \- org.springframework:spring-context-support:jar:5.2.6.RELEASE:compile
[INFO] |  |  \- io.lettuce:lettuce-core:jar:5.3.0.RELEASE:compile
[INFO] |  +- org.redisson:redisson:jar:3.11.6:compile
[INFO] |  |  +- io.netty:netty-common:jar:4.1.49.Final:compile
[INFO] |  |  +- io.netty:netty-codec:jar:4.1.49.Final:compile
[INFO] |  |  +- io.netty:netty-buffer:jar:4.1.49.Final:compile
[INFO] |  |  +- io.netty:netty-transport:jar:4.1.49.Final:compile
[INFO] |  |  |  \- io.netty:netty-resolver:jar:4.1.49.Final:compile
[INFO] |  |  +- io.netty:netty-resolver-dns:jar:4.1.49.Final:compile
[INFO] |  |  |  \- io.netty:netty-codec-dns:jar:4.1.49.Final:compile
[INFO] |  |  +- io.netty:netty-handler:jar:4.1.49.Final:compile
[INFO] |  |  +- javax.cache:cache-api:jar:1.1.1:compile
[INFO] |  |  +- io.projectreactor:reactor-core:jar:3.3.5.RELEASE:compile
[INFO] |  |  |  \- org.reactivestreams:reactive-streams:jar:1.0.3:compile
[INFO] |  |  +- io.reactivex.rxjava2:rxjava:jar:2.2.19:compile
[INFO] |  |  +- de.ruedigermoeller:fst:jar:2.57:compile
[INFO] |  |  |  \- org.javassist:javassist:jar:3.21.0-GA:compile
[INFO] |  |  +- com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:jar:2.11.0:compile
[INFO] |  |  +- com.fasterxml.jackson.core:jackson-core:jar:2.11.0:compile
[INFO] |  |  \- org.jodd:jodd-bean:jar:5.0.13:compile
[INFO] |  |     \- org.jodd:jodd-core:jar:5.0.13:compile
[INFO] |  \- org.apache.commons:commons-lang3:jar:3.10:compile
[INFO] +- iot.cloud.os:cloud-os-core-api:jar:0.0.1.49-SNAPSHOT:compile
[INFO] |  +- org.springframework.cloud:spring-cloud-starter-openfeign:jar:2.1.2.RELEASE:compile
[INFO] |  |  +- org.springframework.cloud:spring-cloud-starter:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  +- org.springframework.cloud:spring-cloud-context:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  \- org.springframework.security:spring-security-rsa:jar:1.0.7.RELEASE:compile
[INFO] |  |  |     \- org.bouncycastle:bcpkix-jdk15on:jar:1.60:compile
[INFO] |  |  |        \- org.bouncycastle:bcprov-jdk15on:jar:1.60:compile
[INFO] |  |  +- org.springframework.cloud:spring-cloud-openfeign-core:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  +- org.springframework.boot:spring-boot-starter-aop:jar:2.3.0.RELEASE:compile
[INFO] |  |  |  |  \- org.aspectj:aspectjweaver:jar:1.9.5:compile
[INFO] |  |  |  \- io.github.openfeign.form:feign-form-spring:jar:3.8.0:compile
[INFO] |  |  |     +- io.github.openfeign.form:feign-form:jar:3.8.0:compile
[INFO] |  |  |     \- commons-fileupload:commons-fileupload:jar:1.4:compile
[INFO] |  |  |        \- commons-io:commons-io:jar:2.2:compile
[INFO] |  |  +- org.springframework.cloud:spring-cloud-commons:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  \- org.springframework.security:spring-security-crypto:jar:5.3.2.RELEASE:compile
[INFO] |  |  +- io.github.openfeign:feign-core:jar:10.2.3:compile
[INFO] |  |  +- io.github.openfeign:feign-slf4j:jar:10.2.3:compile
[INFO] |  |  \- io.github.openfeign:feign-hystrix:jar:10.2.3:compile
[INFO] |  |     +- com.netflix.archaius:archaius-core:jar:0.7.6:compile
[INFO] |  |     \- com.netflix.hystrix:hystrix-core:jar:1.5.18:compile
[INFO] |  |        \- org.hdrhistogram:HdrHistogram:jar:2.1.9:compile
[INFO] |  +- org.springframework.cloud:spring-cloud-starter-netflix-ribbon:jar:2.1.2.RELEASE:compile
[INFO] |  |  +- org.springframework.cloud:spring-cloud-netflix-ribbon:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  \- org.springframework.cloud:spring-cloud-netflix-archaius:jar:2.1.2.RELEASE:compile
[INFO] |  |  +- org.springframework.cloud:spring-cloud-starter-netflix-archaius:jar:2.1.2.RELEASE:compile
[INFO] |  |  |  \- commons-configuration:commons-configuration:jar:1.8:compile
[INFO] |  |  +- com.netflix.ribbon:ribbon:jar:2.3.0:compile
[INFO] |  |  |  +- com.netflix.ribbon:ribbon-transport:jar:2.3.0:runtime
[INFO] |  |  |  |  +- io.reactivex:rxnetty-contexts:jar:0.4.9:runtime
[INFO] |  |  |  |  \- io.reactivex:rxnetty-servo:jar:0.4.9:runtime
[INFO] |  |  |  +- javax.inject:javax.inject:jar:1:runtime
[INFO] |  |  |  \- io.reactivex:rxnetty:jar:0.4.9:runtime
[INFO] |  |  +- com.netflix.ribbon:ribbon-core:jar:2.3.0:compile
[INFO] |  |  |  \- commons-lang:commons-lang:jar:2.6:compile
[INFO] |  |  +- com.netflix.ribbon:ribbon-httpclient:jar:2.3.0:compile
[INFO] |  |  |  +- commons-collections:commons-collections:jar:3.2.2:runtime
[INFO] |  |  |  +- org.apache.httpcomponents:httpclient:jar:4.5.12:compile
[INFO] |  |  |  +- com.sun.jersey:jersey-client:jar:1.19.1:runtime
[INFO] |  |  |  |  \- com.sun.jersey:jersey-core:jar:1.19.1:runtime
[INFO] |  |  |  |     \- javax.ws.rs:jsr311-api:jar:1.1.1:runtime
[INFO] |  |  |  +- com.sun.jersey.contribs:jersey-apache-client4:jar:1.19.1:runtime
[INFO] |  |  |  +- com.netflix.servo:servo-core:jar:0.10.1:runtime
[INFO] |  |  |  |  \- com.netflix.servo:servo-internal:jar:0.10.1:runtime
[INFO] |  |  |  \- com.netflix.netflix-commons:netflix-commons-util:jar:0.1.1:runtime
[INFO] |  |  +- com.netflix.ribbon:ribbon-loadbalancer:jar:2.3.0:compile
[INFO] |  |  |  \- com.netflix.netflix-commons:netflix-statistics:jar:0.1.1:runtime
[INFO] |  |  \- io.reactivex:rxjava:jar:1.3.8:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.11.0:compile
[INFO] |  \- org.hibernate.validator:hibernate-validator:jar:6.1.5.Final:compile
[INFO] |     +- jakarta.validation:jakarta.validation-api:jar:2.0.2:compile
[INFO] |     +- org.jboss.logging:jboss-logging:jar:3.4.1.Final:compile
[INFO] |     \- com.fasterxml:classmate:jar:1.5.1:compile
[INFO] +- iot.cloud.os.ext:cloud-os-console-api:jar:0.0.1.1-SNAPSHOT:compile
[INFO] |  +- iot.cloud.os.data:cloud-os-data-api:jar:0.0.1.10-SNAPSHOT:compile
[INFO] |  +- iot.cloud.os:cloud-os-connect-nb-api:jar:0.0.1-SNAPSHOT:compile
[INFO] |  +- me.hekr:s3:jar:4.2.9.3:compile
[INFO] |  |  +- com.amazonaws:aws-java-sdk-s3:jar:1.11.132:compile
[INFO] |  |  |  +- com.amazonaws:aws-java-sdk-kms:jar:1.11.132:compile
[INFO] |  |  |  +- com.amazonaws:aws-java-sdk-core:jar:1.11.132:compile
[INFO] |  |  |  |  +- commons-logging:commons-logging:jar:1.1.3:compile
[INFO] |  |  |  |  +- software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO] |  |  |  |  \- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.11.0:compile
[INFO] |  |  |  \- com.amazonaws:jmespath-java:jar:1.11.132:compile
[INFO] |  |  +- com.aliyun.oss:aliyun-sdk-oss:jar:2.8.3:compile
[INFO] |  |  |  \- org.jdom:jdom:jar:1.1:compile
[INFO] |  |  +- cn.hutool:hutool-all:jar:4.1.17:compile
[INFO] |  |  \- io.minio:minio:jar:6.0.10:compile
[INFO] |  |     +- com.google.http-client:google-http-client-xml:jar:1.24.1:compile
[INFO] |  |     |  +- com.google.http-client:google-http-client:jar:1.24.1:compile
[INFO] |  |     |  \- xpp3:xpp3:jar:1.1.4c:compile
[INFO] |  |     +- com.squareup.okhttp3:okhttp:jar:3.14.8:compile
[INFO] |  |     +- com.squareup.okio:okio:jar:1.17.2:compile
[INFO] |  |     \- com.google.code.findbugs:annotations:jar:3.0.1:compile
[INFO] |  |        \- net.jcip:jcip-annotations:jar:1.0:compile
[INFO] |  \- org.springframework.cloud:spring-cloud-starter-consul-discovery:jar:2.1.2.RELEASE:compile
[INFO] |     +- org.springframework.cloud:spring-cloud-starter-consul:jar:2.1.2.RELEASE:compile
[INFO] |     |  +- org.springframework.cloud:spring-cloud-consul-core:jar:2.1.2.RELEASE:compile
[INFO] |     |  +- com.ecwid.consul:consul-api:jar:1.4.1:compile
[INFO] |     |  +- com.google.code.gson:gson:jar:2.8.6:compile
[INFO] |     |  \- org.apache.httpcomponents:httpcore:jar:4.4.13:compile
[INFO] |     +- org.springframework.cloud:spring-cloud-consul-discovery:jar:2.1.2.RELEASE:compile
[INFO] |     +- org.springframework.cloud:spring-cloud-netflix-hystrix:jar:2.1.2.RELEASE:compile
[INFO] |     \- joda-time:joda-time:jar:2.10.2:compile
[INFO] +- iot.cloud.os:cloud-os-common-data:jar:0.0.1-SNAPSHOT:compile
[INFO] |  \- org.springframework.data:spring-data-commons:jar:2.3.0.RELEASE:compile
[INFO] \- iot.cloud.os:cloud-os-common-utils:jar:0.0.8-SNAPSHOT:compile
[INFO]    +- com.fasterxml.jackson.core:jackson-databind:jar:2.11.0:compile
[INFO]    +- com.google.guava:guava:jar:28.0-jre:compile
[INFO]    |  +- com.google.guava:failureaccess:jar:1.0.1:compile
[INFO]    |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
[INFO]    |  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile
[INFO]    |  +- org.checkerframework:checker-qual:jar:2.8.1:compile
[INFO]    |  +- com.google.errorprone:error_prone_annotations:jar:2.3.2:compile
[INFO]    |  +- com.google.j2objc:j2objc-annotations:jar:1.3:compile
[INFO]    |  \- org.codehaus.mojo:animal-sniffer-annotations:jar:1.17:compile
[INFO]    \- org.lionsoul:ip2region:jar:1.7.2:compile
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.782 s
[INFO] Finished at: 2020-06-18T16:23:07+08:00
[INFO] ------------------------------------------------------------------------
