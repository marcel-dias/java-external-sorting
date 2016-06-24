FROM marceldiass/alpine-server-jre-8

ADD target/external-sorting.jar /home/
ADD run.sh /home/run.sh
WORKDIR /home
ENTRYPOINT "./run.sh"
