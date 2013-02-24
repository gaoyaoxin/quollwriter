CREATE VIEW character_v
AS
SELECT c.dbkey dbkey,
       c.aliases aliases,
       n.name name,
       n.description description,
       n.lastmodified lastmodified,
       n.objecttype objecttype,
       n.datecreated datecreated,
       n.properties  properties,
       c.projectdbkey projectdbkey
FROM   namedobject_v n,
       character     c
WHERE  c.dbkey = n.dbkey