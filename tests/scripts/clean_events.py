from elasticsearch import Elasticsearch

es = Elasticsearch('dashdb.vip.checkmate.com:9200')

indexName = 'dashboard'

payload = {'query':{"match": {'author': 'Release Maester'} }}
events = es.search(index=indexName, doc_type="event-type", body=payload)

for label, value in events.items():
  if label == 'hits':
    for hit in value['hits']:
      print(hit)
      es.delete(index=indexName, doc_type="event-type", id=hit['_id'])
