FROM ubuntu:20.04
RUN apt update
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./bind9 /etc/default/
RUN DEBIAN_FRONTEND='noninteractive' apt-get install -y --no-install-recommends net-tools bind9 bind9utils bind9-doc iputils-ping nano
