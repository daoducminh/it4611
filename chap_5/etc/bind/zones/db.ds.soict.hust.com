$TTL    604800
@   IN  SOA ns1.ds.soict.hust.com.  admin.ds.soict.hust.com. (
    3; Serial
    604800; Refresh
    86400; Retry
    2419200; Expire
    604800
); Negative Cache TTL
; name servers - NS records
IN  NS  ns1.ds.soict.hust.com.
IN  NS  ns2.ds.soict.hust.com.
; name servers - A records
ns1.ds.soict.hust.com.  IN  A   172.19.0.20
ns2.ds.soict.hust.com.  IN  A   172.19.0.21
; 172.19.0.0/24 - A records
host1.ds.soict.hust.com.    IN  A   172.19.0.100
host2.ds.soict.hust.com.    IN  A   172.19.0.101
