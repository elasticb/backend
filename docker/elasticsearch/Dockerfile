FROM elasticsearch:5.6.15

WORKDIR /usr/share/elasticsearch

RUN bin/elasticsearch-plugin install http://dl.bintray.com/content/imotov/elasticsearch-plugins/org/elasticsearch/elasticsearch-analysis-morphology/5.6.15/elasticsearch-analysis-morphology-5.6.15.zip

COPY ./config/elasticsearch.yml /etc/elasticsearch/elasticsearch.yml
