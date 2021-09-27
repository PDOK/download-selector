FROM openjdk:8

COPY . /app/
WORKDIR /app

RUN apt-get -y update && \
    apt-get -y install --no-install-recommends \
    curl \
    zip

RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein
RUN chmod a+x /usr/local/bin/lein

RUN lein with-profile release compile

WORKDIR /app/resources/public/
RUN zip -r /app/download-selector.zip js release brt-download.html

