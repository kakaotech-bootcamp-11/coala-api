FROM ubuntu:latest
LABEL authors="kongji"

ENTRYPOINT ["top", "-b"]