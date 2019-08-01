FROM nginx

MAINTAINER xiaohe

ENV RUN_USER nginx
ENV RUN_GROUP nginx
ENV DATA_DIR /web
ENV LOG_DIR /log

RUN mkdir /log -p
RUN chown nginx.nginx -R /log

ADD ./etc /etc
ADD ./log /log
ADD ./www /web
ADD ./conf.d/app.conf  /etc/nginx/nginx.conf
EXPOSE 80
EXPOSE 443
ENTRYPOINT nginx -g "daemon off;"
